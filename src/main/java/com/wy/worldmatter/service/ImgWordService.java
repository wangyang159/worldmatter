package com.wy.worldmatter.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * 作者: wangyang <br/>
 * 创建时间: 2022/11/15 <br/>
 * 描述: <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;ImgWordService
 * 图片转文字
 */
public interface ImgWordService {

    /**
     * 描述: 图片转文本,这个方法和证件照中的转换base64数据方法不同，文件直接转换后被使用，而不是保存<br/>
     * 作者: wangyang <br/>
     * 创建时间: 2022/11/15 <br/>
     * 参数: lang-语言，filebase64-文件的base64数据 <br/>
     * 返回值: 0-图片数据为空,1-base64数据解析异常,2-文字识别异常,3-识别完成并返回识别结果 <br/>
     */
    public Map toWord(int lang,String filebase64);

}
