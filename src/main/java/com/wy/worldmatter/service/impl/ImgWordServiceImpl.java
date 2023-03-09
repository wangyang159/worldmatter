package com.wy.worldmatter.service.impl;

import com.wy.worldmatter.service.ImgWordService;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者: wangyang <br/>
 * 创建时间: 2022/11/15 <br/>
 * 描述: <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;ImgWordServiceImpl
 * 图片转文字
 */
@Service
public class ImgWordServiceImpl implements ImgWordService {

    /**
     * 中间文件的缓存地址
     */
    @Value("${img.to.word.tmp.path}")
    private String imgWordTmpPath;

    @Override
    public Map toWord(int lang,String filebase64) {
        //1、转换文件的base64
        //装换用到的中间对象
        String base64 = null;
        File file = null;
        Map<String, Object> resultMap = new HashMap<String, Object>();

        // 图像数据为空返回异常结果
        if (filebase64 == null || "".equals(filebase64)) {
            resultMap.put("resultCode", 0);
            resultMap.put("msg", "图片数据未接收到");
        } else {
            //根据base中的后缀生成临时文件
            //样例：data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAB......后面的省略
            String hz = filebase64.substring(filebase64.indexOf("/")+1, filebase64.indexOf(";"));
            file = new File(imgWordTmpPath, System.currentTimeMillis() + "." + hz);
            //前台传递回来的数据格式需要处理一下，把没用的数据头删掉，以及前端强制加进去的换行符号删掉
            //样例：data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAB......后面的省略
            base64 = filebase64.substring(filebase64.indexOf(",")+1).replace("\r\n", "");

            //把base64数据解析并写入结果文件
            BufferedOutputStream bos = null;
            FileOutputStream fos = null;
            try {
                byte[] bytes = Base64.decodeBase64(base64);
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos);
                bos.write(bytes);
                bos.flush();
            } catch (Exception e) {
                e.printStackTrace();
                resultMap.put("resultCode", 1);
                resultMap.put("msg", "解析base64异常");
                return resultMap;
            } finally {
                //关流
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        //因为tesseract内部try了空指针，无法捕获，所以这里需要个补丁代码防止文件转换时的空异常
        if( file==null || !file.exists()){
            resultMap.put("resultCode", 0);
            resultMap.put("msg", "图片数据未接收到");
            return resultMap;
        }

        //把转换好的文件用tess4j识别文字
        Tesseract tesseract = new Tesseract();
        //加载语言库
        tesseract.setDatapath(System.getProperty("user.dir")+ File.separator + "tessdata");
        //抑制自动识别分辨率的日志输出
        tesseract.setTessVariable("debug_file", "/dev/null");

        //确定语言，注意这里设置的是tesseract的语言包用前缀代表，而不是国际语言文化代码
        switch (lang){
            case 1 :
                tesseract.setLanguage("chi_sim");
                break;
            case 2 :
                tesseract.setLanguage("eng");
                break;
        }

        String s ;
        try {
            if(lang==1){
                //如果是中文识别那么需要把换行符等特殊字符删掉
                s = tesseract.doOCR(file).replace(" ","").replaceAll("\n|\r","");
            }else{
                s = tesseract.doOCR(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultCode", 2);
            resultMap.put("msg", "识别文字异常");
            return resultMap;
        } finally {
            //转换结束后删除保存的图片
            file.delete();
        }

        resultMap.put("resultCode", 3);
        resultMap.put("msg", s);
        return resultMap;
    }

}
