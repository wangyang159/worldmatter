package com.wy.worldmatter.controller;

import com.wy.utils.Base64Util;
import com.wy.worldmatter.service.PDFSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 作者: wangyang <br/>
 * 创建时间: 2022/11/8 <br/>
 * 描述: <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;PDFSController
 */
@Controller
@RequestMapping("/pdfs")
public class PDFSController {

    @Autowired
    private PDFSService pdfsService;

    @Value("${pdf.to.word.result.protect}")
    private boolean protect;

    /**
     * 描述: 跳转到ap技术转Word <br/>
     * 作者: wangyang <br/>
     * 创建时间: 2022/11/5 <br/>
     * 参数:  <br/>
     * 返回值:  <br/>
     */
    @RequestMapping("/appdf")
    public String toap() {
        return "pdfapp/appdf";
    }

    /**
     * 描述: 跳转到sr技术转Word <br/>
     * 作者: wangyang <br/>
     * 创建时间: 2022/11/5 <br/>
     * 参数:  <br/>
     * 返回值:  <br/>
     */
    @RequestMapping("/srpdf")
    public String tosr() {
        return "pdfapp/srpdf";
    }

    /**
     * 描述: 使用aspose-pdf技术，进行多pdf转word,文件最多10个，这是程序核心中写死的 <br/>
     * 作者: wangyang <br/>
     * 创建时间: 2022/11/5 <br/>
     * 参数:  <br/>
     * 返回值: 0-传入的文件集合为空,1-文件超过了10个,2-文件保存到服务器过程中出现意外,3-文件转换时发生错误,4-服务器线程池发生异常,5-任务完成并返回加密后的结果压缩文件路径<br/>
     */
    @RequestMapping("/appdftodocx")
    @ResponseBody
    public Map apPdfToDocx(MultipartFile[] files) {
        return pdfsService.apPdfToDocx(files);
    }

    /**
     * 描述: 使用spire技术，进行多pdf转word,文件最多10个，这是程序核心中写死的<br/>
     * 作者: wangyang <br/>
     * 创建时间: 2022/11/5 <br/>
     * 参数: 文件 <br/>
     * 返回值:0-传入的文件集合为空,1-文件超过了10个,2-文件保存到服务器过程中出现意外,3-文件转换时发生错误,4-服务器线程池发生异常,5-任务完成并返回加密后的结果压缩文件路径 <br/>
     */
    @RequestMapping("/srpdftodocx")
    @ResponseBody
    public Map srPdfToDocx(MultipartFile[] files) {
        return pdfsService.srPdfToDocx(files);
    }

    /**
     * 描述: 结果文件的下载,采用不同于证件照的下载方法，使用一次性下载 <br/>
     * 作者: wangyang <br/>
     * 创建时间: 2022/11/5 <br/>
     * 参数:  <br/>
     * 返回值:  <br/>
     */
    @RequestMapping("/download")
    public void download(String path, HttpServletResponse response) {
        try {
            //解密文件名
            String dePath = Base64Util.base64Decoder(path.replace(" ", "+"));
            // path是指想要下载的文件的路径
            File file = new File(dePath);
            // 获取文件名
            String filename = file.getName();

            // 将文件写入输入流，加载到内存里面，暂存到名为buffer的字节数组里面
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            fileInputStream.close();

            //是否删除
            if(!protect){
                file.delete();
            }

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
}
