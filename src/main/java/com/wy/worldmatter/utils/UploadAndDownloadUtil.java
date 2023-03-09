package com.wy.worldmatter.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * 作者: wangyang <br/>
 * 创建时间: 2023/3/1 <br/>
 * 描述: <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;DownloadUtil
 * 处理下载任务的工具类
 */
public class UploadAndDownloadUtil {

    /**
      * 描述: 使用流将文件陆续传递给前端 <br/>
      * 作者: wangyang <br/>
      * 创建时间: 2023/3/1 <br/>
      * 参数: path-前端传递过来的路径,需要提前解密 <br/>
      * 返回值:  <br/>
      */
    public static void whileDownload(String path, HttpServletResponse response) throws IOException {
        // 文件读到流中
        InputStream inputStream = new FileInputStream(path);

        //设置前端响应头
        response.reset();
        response.setContentType("application/octet-stream");
        String filename = new File(path).getName();
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
        ServletOutputStream outputStream = response.getOutputStream();
        byte[] b = new byte[1024];
        int len;
        //从输入流中读取一定数量的字节，并将其存储在缓冲区字节数组中，读到末尾返回-1
        while ((len = inputStream.read(b)) > 0) {
            outputStream.write(b, 0, len);
        }
        inputStream.close();
    }


    /**
      * 描述: 一次性将文件全部返回给前端 <br/>
      * 作者: wangyang <br/>
      * 创建时间: 2023/3/1 <br/>
      * 参数: path-前端传递的路径,需要提前解密 <br/>
      * 返回值:  <br/>
      */
    public static void allDownload(String path, HttpServletResponse response) {
        try {
            // path是指想要下载的文件的路径
            File file = new File(path);
            // 获取文件名
            String filename = file.getName();

            // 将文件写入输入流，加载到内存里面，暂存到名为buffer的字节数组里面
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            fileInputStream.close();

            // 预处理：清空response
            response.reset();

            // 设置response的响应Header
            //响应内容的字符集
            response.setCharacterEncoding("UTF-8");
            //浏览器响应返回内容的方式
            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            //attachment表示以附件方式下载 inline表示在线打开 "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + file.length());
            //获取响应的输出流把内容写景区
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            outputStream.write(buffer);
            outputStream.flush();

            outputStream.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 描述: 保存文件的方法 <br/>
     * 作者: wangyang <br/>
     * 创建时间: 2022/11/8 <br/>
     * 参数: file-需要保存的文件,resultPath-保存时的基准路径 <br/>
     * 返回值: 返回保存后的原始文件名称  <br/>
     */
    public static String saveFile(MultipartFile file,String resultPath) throws IOException {
        //获取文件名称
        String originalFilename = file.getOriginalFilename();
        //结果文件名称
        String fileName = "原文件_"+ UUID.randomUUID() + "_" + originalFilename;
        //创建新文件对象
        File destFile = new File(resultPath, fileName);

        //确保目标的父文件目录存在
        if (!destFile.getParentFile().exists()) {
            destFile.mkdirs();
        }
        //执行拷贝过程
        file.transferTo(destFile);
        //返回全路径文件名
        return destFile.getPath();
    }


}
