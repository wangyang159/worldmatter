package com.wy.worldmatter.service.impl;

import com.aspose.pdf.Document;
import com.aspose.pdf.SaveFormat;
import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import com.wy.utils.Base64Util;
import com.wy.utils.FileUtil;
import com.wy.worldmatter.service.PDFSService;
import com.wy.worldmatter.utils.UploadAndDownloadUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 作者: wangyang <br/>
 * 创建时间: 2022/11/8 <br/>
 * 描述: <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;PDFSServiceImpl
 * 使用两种技术aspose-pdf和spire.pdf，并没有选择使用apache-poi，因为它的操作复杂度和效果很不理想
 * 多PDF转Word功能，只会保留压缩后的结果文件，原文件会在装换后删除
 */
@Service
public class PDFSServiceImpl implements PDFSService {

    /**
     * 中间文件的保存基准路径
     */
    @Value("${pdf.to.word.result.path}")
    private String resultPath;

    /**
     * 一个批次最大上传的文件个数
     */
    @Value("${pdf.files.num.max}")
    private Integer max_files;

    /**
     * 用定长线程池处理文件以及另存它的线程总数
     */
    private static ExecutorService pdfToWordThreadPool;
    private static Integer task_num;

    /**
     * 初始化线程池
     */
    @Value("${pdf.task.num}")
    public void setTask_num(Integer num) {
        //初始化线程池并保留线程个数
        PDFSServiceImpl.pdfToWordThreadPool = Executors.newFixedThreadPool(num);
        PDFSServiceImpl.task_num = num;
    }

    @Override
    public Map apPdfToDocx(MultipartFile[] files) {
        //返回结果的Map文件、运行中存储文件名的buffer
        Map result = new HashMap<String, Object>();
        //使用线程安全的StringBuffer存储程序中的字符串
        StringBuffer bu = new StringBuffer();

        //1、配置文件中单次可转文件数不能小于等于0
        int free_num = max_files.intValue() > 0 ? max_files.intValue() : 1;
        //这个是线程池正在运行的任务数，后面用来提交任务前检查剩余线程是否足够
        int threadCount = ((ThreadPoolExecutor) pdfToWordThreadPool).getActiveCount();

        //2、判断接收的数据是否正常，不能是0或者超过最大值
        if (files.length <= 0) {
            result.put("resultCode", 0);
            result.put("msg", "文件接收异常联系系统管理员");
            return result;
        } else if (files.length > free_num) {
            //道理来讲这一步判断不需要，因为前端也要做判断，但是为了防止意外后端也做一次效验
            result.put("resultCode", 1);
            result.put("msg", "文件个数超出目前支持的最大个数请检查");
            return result;
        } else if (task_num - threadCount < files.length) {
            //如果线程的资源不足， 返回错误
            result.put("resultCode", 6);
            result.put("msg", "当前剩余资源数" + (task_num - threadCount) + ",现有剩余资源不足，请联系管理员");
            return result;
        }


        //3、上传文件，并将上传完的文件写在buffer里，并英文逗号分隔
        try {
            for (int i = 0; i < files.length; i++) {
                String s = UploadAndDownloadUtil.saveFile(files[i], resultPath);
                //通过判断让末尾没有文件名的分隔符
                if (i == (files.length - 1)) {
                    bu.append(s);
                } else {
                    bu.append(s + ",");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            result.put("resultCode", 2);
            result.put("msg", "上传文件至服务器发生异常请联系管理员");
            return result;
        }

        //4、文件名提取出来
        String[] yuan_files = bu.toString().split(",");
        //清空buffer里的原文件名
        bu.delete(0, bu.length());

        //5、线程伴生对象用来阻塞主线程
        CountDownLatch countDownLatch = new CountDownLatch(files.length);

        //6、给线程池提交任务：转换原文件--》把转换后的文件名写给buffer
        final boolean[] hasExc = {false};//如果发生意外断掉程序，所以这里准备一个是否有异常的旗帜，默认false，没有异常
        for (int i = 0; i < files.length; i++) {
            //创建任务对象
            int finalI = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Document doc = null;
                    FileOutputStream os = null;
                    File f = null;
                    try {
                        //处理结果文件名
                        String wordPath = yuan_files[finalI].replace("原文件", "结果文件").replace(".pdf", ".docx");
                        //获取结果word文档的输出流
                        os = new FileOutputStream(wordPath);
                        //这个临时原文件后面要删除
                        f = new File(yuan_files[finalI]);
                        //ap提供了文档对象加载原文件
                        doc = new Document(f.getPath());
                        //全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
                        doc.save(os, SaveFormat.DocX);

                        //保存文件名
                        if (countDownLatch.getCount() == 1) {
                            bu.append(wordPath);
                        } else {
                            bu.append(wordPath + ",");
                        }

                        countDownLatch.countDown();
                    } catch (FileNotFoundException e) {
                        hasExc[0] = true;
                        e.printStackTrace();
                    } finally {
                        try {
                            os.close();
                            doc.close();
                            //转换完把原文件删了
                            f.delete();
                        } catch (IOException e) {
                            e.printStackTrace();
                            hasExc[0] = true;
                        }
                    }
                }

            };

            //提交任务的同时判断程序是否安全，如果发生了意外就断掉程序
            if (hasExc[0]) {
                result.put("resultCode", 3);
                result.put("msg", "文件转换发生异常请联系管理员");
                return result;
            }
            // 将任务交给线程池管理
            pdfToWordThreadPool.execute(runnable);

        }

        //7、任务提交，主进程等待
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            result.put("resultCode", 4);
            result.put("msg", "服务器发生线程意外,请立刻联系管理员");
            return result;
        }

        //8、任务全部结束后把结果文件的放入数组容器
        String[] jieguo_files_path = bu.toString().split(",");
        File[] jieguo_files_arr = new File[jieguo_files_path.length];
        for (int i = 0; i < jieguo_files_path.length; i++) {
            jieguo_files_arr[i] = new File(jieguo_files_path[i]);
        }

        //9、文件打包成zip
        File zipResult = new File(resultPath, UUID.randomUUID() + ".zip");
        FileUtil.zipFiles(jieguo_files_arr, zipResult, true);

        //10、文件处理完成，给前端返回压缩包的加密文件名
        result.put("resultCode", 5);
        result.put("msg", Base64Util.base64Encoder(zipResult.getPath()));
        return result;
    }

    @Override
    public Map srPdfToDocx(MultipartFile[] files) {
        //返回结果的Map文件、运行中存储文件名的buffer
        Map result = new HashMap<String, Object>();
        //使用线程安全的StringBuffer存储程序中的字符串
        StringBuffer bu = new StringBuffer();

        //配置文件中单次可转文件数不能小于等于0
        int free_num = max_files.intValue() > 0 ? max_files.intValue() : 1;
        //这个是线程池正在运行的任务数，后面用来提交任务前检查剩余线程是否足够
        int threadCount = ((ThreadPoolExecutor) pdfToWordThreadPool).getActiveCount();

        //判断接收的数据是否正常
        if (files.length <= 0) {
            result.put("resultCode", 0);
            result.put("msg", "文件接收异常联系系统管理员");
            return result;
        } else if (files.length > free_num) {
            //道理来讲这一步判断不需要，因为前端也要做判断，但是为了防止意外后端也做一次效验
            result.put("resultCode", 1);
            result.put("msg", "文件个数超出目前支持的最大个数请检查");
            return result;
        } else if (task_num - threadCount < files.length) {
            result.put("resultCode", 6);
            result.put("msg", "当前剩余资源数" + (task_num - threadCount) + ",现有剩余资源不足，请联系管理员");
            return result;
        }

        //上传文件，并将上传完的文件写在buffer里，并英文逗号分隔
        try {
            for (int i = 0; i < files.length; i++) {
                String s = UploadAndDownloadUtil.saveFile(files[i], resultPath);
                //通过判断让末尾没有文件名的分隔符
                if (i == (files.length - 1)) {
                    bu.append(s);
                } else {
                    bu.append(s + ",");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            result.put("resultCode", 2);
            result.put("msg", "上传文件至服务器发生异常请联系管理员");
            return result;
        }

        //文件名提取出来
        String[] yuan_files = bu.toString().split(",");
        //清空buffer里的原文件名
        bu.delete(0, bu.length());

        CountDownLatch countDownLatch = new CountDownLatch(files.length);

        //给线程池提交任务：转换原文件--》把转换后的文件名写给buffer
        final boolean[] hasExc = {false};//发生意外断掉程序
        for (int i = 0; i < files.length; i++) {
            //创建任务对象
            int finalI = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    PdfDocument pdfDocument = null;
                    File f = null;
                    String wordPath = null;
                    try {
                        //结果文件名
                        wordPath = yuan_files[finalI].replace("原文件", "结果文件").replace(".pdf", ".docx");
                        f = new File(yuan_files[finalI]);
                        //转换
                        pdfDocument = new PdfDocument();
                        pdfDocument.loadFromFile(f.getPath());
                        pdfDocument.saveToFile(wordPath, FileFormat.DOCX);
                        //保存文件名
                        if (countDownLatch.getCount() == 1) {
                            bu.append(wordPath);
                        } else {
                            bu.append(wordPath + ",");
                        }
                        countDownLatch.countDown();
                    } catch (Exception e) {
                        hasExc[0] = true;
                        e.printStackTrace();
                    } finally {
                        pdfDocument.close();
                        f.delete();
                    }
                }

            };

            //提交任务的同时判断程序是否安全，如果发生了意外就断掉程序
            if (hasExc[0]) {
                result.put("resultCode", 3);
                result.put("msg", "文件转换发生异常请联系管理员");
                return result;
            }

            // 将任务交给线程池管理
            pdfToWordThreadPool.execute(runnable);

        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            result.put("resultCode", 4);
            result.put("msg", "服务器发生线程意外,请立刻联系管理员");
            return result;
        }

        //把结果文件的放入数组容器
        String[] jieguo_files_path = bu.toString().split(",");
        File[] jieguo_files_arr = new File[jieguo_files_path.length];
        for (int i = 0; i < jieguo_files_path.length; i++) {
            jieguo_files_arr[i] = new File(jieguo_files_path[i]);
        }

        //文件打包成zip
        File zipResult = new File(resultPath, UUID.randomUUID() + ".zip");
        FileUtil.zipFiles(jieguo_files_arr, zipResult, true);

        //文件处理完成，给前端返回压缩包的加密文件名
        result.put("resultCode", 5);
        result.put("msg", Base64Util.base64Encoder(zipResult.getPath()));
        return result;
    }


}