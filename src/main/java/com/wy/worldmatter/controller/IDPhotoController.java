package com.wy.worldmatter.controller;

import com.wy.utils.Base64Util;
import com.wy.worldmatter.service.IDPhotoService;
import com.wy.worldmatter.utils.UploadAndDownloadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

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
      * 描述: 该方法为证件照功能的跳转控制方法 <br/>
      * 作者: wangyang <br/>
      * 创建时间: 2023/3/1 <br/>
      * 参数: token-跳转页面的键 1跳转无背景 2跳转有背景色 <br/>
      * 返回值: 跳转固定页面 <br/>
      */
    @RequestMapping("/zjz")
    public String zjztz(Model model,int token){
        if(token == 1){
            idPhotoService.zjzParm(model);
            return "idphotoapp/zjz";
        } else if (token == 2) {
            idPhotoService.zjzHaveBjParm(model);
            return "idphotoapp/zjzhavebj";
        }
        model.addAttribute("mes","请不要随意修改内部参数");
        return "common/err";
    }

    /**
     * 描述: 接收证件照的base64编码解析成文件并保存到结果路径下 <br/>
     * 作者: wangyang <br/>
     * 创建时间: 2022/11/4 <br/>
     * 参数: base-证件照的base64编码 <br/>
     * 返回值: Map-结果集 <br/>
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
        //使用工具类解密文件路径,并且防止传递数据时+号变成空格
        String dePath = Base64Util.base64Decoder(path.replace(" ","+"));
        UploadAndDownloadUtil.whileDownload(dePath,response);
    }

}
