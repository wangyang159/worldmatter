package com.wy.worldmatter.service.impl;

import com.wy.worldmatter.service.MasterHubService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * 作者: wangyang <br/>
 * 创建时间: 2022/11/7 <br/>
 * 描述: <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;MasterHubServiceImpl
 * 程序核心入口类代码
 */
@Service
public class MasterHubServiceImpl implements MasterHubService {

    /**
     * 首页背景是否开启
     */
    @Value("${main.mic.yesorno}")
    private Boolean mainMic;

    @Override
    public void loadMain(Model model) {
        model.addAttribute("mainMic",mainMic);
    }
}
