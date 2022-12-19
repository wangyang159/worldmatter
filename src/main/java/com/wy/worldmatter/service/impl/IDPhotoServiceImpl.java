package com.wy.worldmatter.service.impl;

import com.wy.utils.Base64Util;
import com.wy.worldmatter.service.IDPhotoService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者: wangyang <br/>
 * 创建时间: 2022/11/6 <br/>
 * 描述: <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;IDPhotoServiceImpl
 * 证件照的服务层代码
 */
@Service
public class IDPhotoServiceImpl implements IDPhotoService {
    /**
     * 证件照保存路径
     * 文件格式
     */
    @Value("${zjz.result.path}")
    private String imgFilePath;
    @Value("${zjz.result.endwith}")
    private String endWith ;

    @Override
    public void zjzHaveBjParm(Model model) {
        //背景的证件照最多支持x259 y413
        model.addAttribute("xs_x","295");
        model.addAttribute("xs_y","413");
    }

    @Override
    public void zjzParm(Model model) {
        //没有背景的证件照最多支持x400 y450
        model.addAttribute("xs_x","400");
        model.addAttribute("xs_y","450");
    }

    @Override
    public Map getFileFromBase64(String base) {
        //中间对象
        String base64 = null;
        File file = null;
        Map<String, Object> resultMap = new HashMap<String, Object>();

        // 图像数据为空返回异常结果
        if (base == null || "".equals(base)) {
            resultMap.put("resultCode", 0);
            resultMap.put("msg", "图片数据未接收到");
        } else {
            //前台传递回来的数据格式需要处理一下，把没用的数据头删掉，以及换行符号删掉
            base64 = base.substring(base.indexOf(",")+1);
            base64 = base64.toString().replace("\r\n", "");

            //预处理结果文件路径
            if(!imgFilePath.endsWith("\\")){
                file = new File(imgFilePath+"\\"+
                        System.currentTimeMillis()+
                        (endWith.startsWith("\\.")?"_result"+endWith:"_"+endWith) );
            }else{
                file = new File(imgFilePath+
                        System.currentTimeMillis()+
                        (endWith.startsWith("\\.")?"_result"+endWith:"_"+endWith) );
            }

            if(!file.getParentFile().exists()){
                file.mkdir();
            }

            //把结果文件解析并写入结果文件
            BufferedOutputStream bos = null;
            FileOutputStream fos = null;
            try {
                byte[] bytes = Base64.decodeBase64(base64);
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos);
                bos.write(bytes);
                bos.flush();
            } catch (Exception e) {
                resultMap.put("resultCode", 2);
                resultMap.put("msg", e.getMessage());
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
            resultMap.put("resultCode", 1);
            resultMap.put("msg", Base64Util.base64Encoder(file.getPath()));
        }
        return resultMap;
    }
}
