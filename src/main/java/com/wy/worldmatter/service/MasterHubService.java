package com.wy.worldmatter.service;

import org.springframework.ui.Model;

import java.io.IOException;

/**
 * 作者: wangyang <br/>
 * 创建时间: 2022/11/7 <br/>
 * 描述: <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;MasterHubService
 */
public interface MasterHubService {

    /**
     * 首页功能列表数据集、页码集合数据初始化
     * @param fileName
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void setMasterTableInfos(String fileName) throws IOException, ClassNotFoundException ;

    /**
     * 描述: 程序入口-首页加载配置 <br/>
     * 作者: wangyang <br/>
     * 创建时间: 2022/11/7 <br/>
     * 参数: model <br/>
     * 返回值:  <br/>
     */
    public void loadMain(Model model,Integer pageNum);
}
