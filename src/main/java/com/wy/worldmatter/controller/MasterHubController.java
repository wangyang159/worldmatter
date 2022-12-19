package com.wy.worldmatter.controller;

import com.wy.worldmatter.service.MasterHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 作者: wangyang <br/>
 * 创建时间: 2022/11/4 <br/>
 * 描述: <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;MasterHubController
 * 核心枢纽控制器，负责程序的主要跳转
 */
@RequestMapping("/masterhub")
@Controller
public class MasterHubController {

    @Autowired
    private MasterHubService masterHubService;

    /**
     * 描述: 这个程序的入口类，进入首页 <br/>
     * 作者: wangyang <br/>
     * 创建时间: 2022/11/4 <br/>
     * 参数:  <br/>
     * 返回值:  <br/>
     */
    @RequestMapping(value={"/",""})
    public String main(Model model){
        masterHubService.loadMain(model);
        return "master";
    }


}
