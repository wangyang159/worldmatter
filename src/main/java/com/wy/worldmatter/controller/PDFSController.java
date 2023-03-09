package com.wy.worldmatter.controller;

import com.wy.utils.Base64Util;
import com.wy.worldmatter.service.PDFSService;
import com.wy.worldmatter.utils.UploadAndDownloadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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

    /**
      * 描述: 证件照功能页面跳转控制器 <br/>
      * 作者: wangyang <br/>
      * 创建时间: 2023/3/1 <br/>
      * 参数: token-跳转页面的键 1跳转AP技术 2跳转SR技术 <br/>
      * 返回值:  <br/>
      */
    @RequestMapping("/pdftoword")
    public String pdfstz(Model model,int token){
        if(token == 1){
            return "pdfapp/appdf";
        } else if (token == 2) {
            return "pdfapp/srpdf";
        }
        model.addAttribute("mes","请不要随意修改内部参数");
        return "common/err";
    }

    /**
     * 描述: 使用aspose-pdf技术，进行多pdf转word <br/>
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
     * 描述: 使用spire技术，进行多pdf转word<br/>
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
        //解密文件路径
        String dePath = Base64Util.base64Decoder(path.replace(" ", "+"));
        UploadAndDownloadUtil.allDownload(dePath,response);
    }
}
