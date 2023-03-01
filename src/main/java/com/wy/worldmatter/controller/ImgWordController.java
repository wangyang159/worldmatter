package com.wy.worldmatter.controller;

import com.wy.worldmatter.service.ImgWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * 作者: wangyang <br/>
 * 创建时间: 2022/11/15 <br/>
 * 描述: <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;ImgWordController
 * 图片转文字
 */
@Controller
@RequestMapping("/imgword")
public class ImgWordController {
    
    @Autowired
    private ImgWordService imgWordService;

    /**
     * 描述: 图片转文字功能跳转页面的控制器 <br/>
     * 作者: wangyang <br/>
     * 创建时间: 2022/11/16 <br/>
     * 参数:  <br/>
     * 返回值:  <br/>
     */
    @RequestMapping("/toimgword")
    public String toImgWord(Model model, int token){
        if(token == 1){
            return "imgword/imgword";
        }
        model.addAttribute("mes","请不要随意修改内部参数");
        return "common/err";
    }

    /**
     * 描述: 文件识别文字 <br/>
     * 作者: wangyang <br/>
     * 创建时间: 2022/11/16 <br/>
     * 参数: lang-前台选择的语言，filebase64-图片的base64数据 <br/>
     * 返回值: 0-图片数据为空,1-base64数据解析异常,2-文字识别异常,3-识别完成并返回识别结果 <br/>
     */
    @RequestMapping("/toword")
    @ResponseBody
    public Map toWord(int lang,String filebase64){
        return imgWordService.toWord(lang, filebase64);
    }
    
}
