package com.wy.worldmatter.controller;

import com.wy.utils.Base64Util;
import com.wy.worldmatter.service.IDPhotoService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 作者: wangyang <br/>
 * 创建时间: 2022/11/1 <br/>
 * 描述: <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;
 * 证件照控制器
 */
@RequestMapping("/idphoto")
@Controller
public class IDPhotoController {

    @Autowired
    private IDPhotoService idPhotoService;

    /**
     * 描述: 跳转包含底色的证件照 <br/>
     * 作者: wangyang <br/>
     * 创建时间: 2022/11/4 <br/>
     * 参数: model <br/>
     * 返回值: 跳转前台zjzhavebj.jsp页面 <br/>
     */
    @RequestMapping("/zjzhavebj")
    public String zjzHaveBj(Model model){
        idPhotoService.zjzHaveBjParm(model);
        return "idphotoapp/zjzhavebj";
    }

    /**
     * 描述: 跳转证件照不含底色页面，做个人照使用 <br/>
     * 作者: wangyang <br/>
     * 创建时间: 2022/11/5 <br/>
     * 参数: model <br/>
     * 返回值: 跳转前台zjz.jsp页面 <br/>
     */
    @RequestMapping("/zjz")
    public String zjz(Model model){
        idPhotoService.zjzParm(model);
        return "idphotoapp/zjz";
    }

    /**
     * 描述: 接收证件照的base64编码解析成文件并保存到结果路径下 <br/>
     * 作者: wangyang <br/>
     * 创建时间: 2022/11/4 <br/>
     * 参数: base-证件照的base64编码 <br/>
     * 返回值: Map-包含保存结果 <br/>
     */
    @RequestMapping("/zjzFromBase64")
    @ResponseBody
    public Map getFileFromBase64(String base) {
        return idPhotoService.getFileFromBase64(base);
    }

    /**
     * 描述: 证件照相关功能的下载控制器，使用循环写到前端的响应流中，而不是一次性写入 <br/>
     * 作者: wangyang <br/>
     * 创建时间: 2022/11/6 <br/>
     * 参数: path-下载文件路径 <br/>
     * 返回值:  <br/>
     */
    @RequestMapping("/download")
    public void download(String path, HttpServletResponse response) throws IOException {
        //使用工具类解密文件路径
        String dePath = Base64Util.base64Decoder(path.replace(" ","+"));
        // 文件读到流中
        InputStream inputStream = new FileInputStream(dePath);

        //设置前端响应头
        response.reset();
        response.setContentType("application/octet-stream");
        String filename = new File(dePath).getName();
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

}
