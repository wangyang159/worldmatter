package com.wy.worldmatter.service;

import org.springframework.ui.Model;

import java.util.Map;

/**
 * 作者: wangyang <br/>
 * 创建时间: 2022/11/6 <br/>
 * 描述: <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;IDPhotoService
 * 证件照的Service
 */
public interface IDPhotoService {

    /**
     * 描述: 控制层跳转有背景证件照页面时，获取配置参数的方法，一开始配置可以把控大小，但是2.0版本的时候发现没有让用户控制的必要 <br/>
     *      所以就取消了配置文件中的参数配置，代码层面只保留了方法，大小的参数写死了 <br/>
     * 作者: wangyang <br/>
     * 创建时间: 2022/11/6 <br/>
     * 参数:  <br/>
     * 返回值:  <br/>
     */
    public void zjzHaveBjParm(Model model);

    /**
     * 描述: 控制层跳转无背景证件照页面时，获取配置参数的方法，一开始配置可以把控大小，但是2.0版本的时候发现没有让用户控制的必要 <br/>
     *     所以就取消了配置文件中的参数配置，代码层面只保留了方法，大小的参数写死了 <br/>
     * 作者: wangyang <br/>
     * 创建时间: 2022/11/6 <br/>
     * 参数:  <br/>
     * 返回值:  <br/>
     */
    public void zjzParm(Model model);

    /**
     * 描述: 处理前端的证件照base64的加密数据 <br/>
     * 作者: wangyang <br/>
     * 创建时间: 2022/11/6 <br/>
     * 参数: base-加密数据 <br/>
     * 返回值: Map-包含保存结果,其中resultCode的值有如下区间：<br/>
     * 0-base无数据,1-数据接收成功并返回加密后的全路径文件名,2-数据接收发生意外并返回异常信息 <br/>
     */
    public Map getFileFromBase64(String base);

}
