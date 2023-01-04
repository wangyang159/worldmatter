package com.wy.worldmatter.controller;

import com.wy.worldmatter.bean.MasterTableInfo;
import com.wy.worldmatter.service.MasterHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

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
     * 描述: 这个程序的入口类,进入首页 ,并把配置文件中的首页功能列表读取到前台 <br/>
     * 作者: wangyang <br/>
     * 创建时间: 2022/11/4 <br/>
     * 参数:  <br/>
     * 返回值:  <br/>
     */
    @RequestMapping(value={"/",""})
    public String main(Model model,@RequestParam(defaultValue="1")Integer pageNum){
        //首页需要的数据装载
        masterHubService.loadMain(model,pageNum);
        return "master";
    }

}
