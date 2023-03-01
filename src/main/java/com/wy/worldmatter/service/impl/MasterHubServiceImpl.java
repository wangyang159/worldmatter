package com.wy.worldmatter.service.impl;

import com.wy.worldmatter.bean.MasterTableInfo;
import com.wy.worldmatter.controller.MasterHubController;
import com.wy.worldmatter.service.MasterHubService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 首页功能列表、列表的页码集合、分页大小
     * 分页大小定死3，之所以代码里面写死，是因为如果使用set方法注入在启动时大概率发生注入不及时导致的空异常
     */
    private static List<MasterTableInfo> masterTableInfos;
    private static List<Integer> pageNums ;
    private static Integer pageSize = 3;

    /**
     * 首页功能列表加载方法，使用springboot赋值单参set方法的能力完成
     * @param fileName
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Value("${main.master.table.file}")
    public void setMasterTableInfos(String fileName) throws IOException, ClassNotFoundException {
        //获取配置文件  初始化话首页功能列表的数据 user.dir可以获取到程序被调用时的路径
        String s = System.getProperty("user.dir") + File.separator + "lib" + File.separator + fileName;
        System.out.println("首页功能表单数据文件："+s);
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(s));
        MasterHubServiceImpl.masterTableInfos = (List<MasterTableInfo>) objectInputStream.readObject();
        System.out.println("已加载到的首页功能列表数据串："+masterTableInfos);
        //页码集合 用摸运算，算出要分几页
        int p = MasterHubServiceImpl.masterTableInfos.size() % MasterHubServiceImpl.pageSize == 0 ? MasterHubServiceImpl.masterTableInfos.size() / MasterHubServiceImpl.pageSize : MasterHubServiceImpl.masterTableInfos.size() / MasterHubServiceImpl.pageSize + 1;
        MasterHubServiceImpl.pageNums = new ArrayList<>(p);
        for (int i = 1 ; i <= p ; i++ ){
            MasterHubServiceImpl.pageNums.add(i);
        }
    }

    @Override
    public void loadMain(Model model,Integer pageNum) {
        //是否启用背景音乐
        model.addAttribute("mainMic",mainMic);
        //当前页
        model.addAttribute("pageNum",pageNum);
        //总页数
        int i = MasterHubServiceImpl.masterTableInfos.size() % MasterHubServiceImpl.pageSize;
        int total = i==0 ? MasterHubServiceImpl.masterTableInfos.size()/MasterHubServiceImpl.pageSize : MasterHubServiceImpl.masterTableInfos.size()/MasterHubServiceImpl.pageSize + 1;
        model.addAttribute("total",total);
        //具体数据的集合
        //正常数据库的分页是（当前页,每页大小）  但由于这里从数据集中直接拿就需要换算为(开始下标,终止下标) 当然当前页和开始下标的换算方式是一样的
        if(pageNum * MasterHubServiceImpl.pageSize > MasterHubServiceImpl.masterTableInfos.size()){
            //这个if判断是因为最后一页数据不满一页，防止下标超出
            model.addAttribute("data", MasterHubServiceImpl.masterTableInfos.subList( (pageNum - 1) * 3, MasterHubServiceImpl.masterTableInfos.size()) );
        }else {
            model.addAttribute("data", MasterHubServiceImpl.masterTableInfos.subList((pageNum - 1) * 3, pageNum * 3) );
        }
        //上一页、下一页、页码的集合
        model.addAttribute("prePage",pageNum==1 ? 1 : pageNum-1 );
        model.addAttribute("nextPage",pageNum==total ? total : pageNum+1);
        model.addAttribute("pageNums",pageNums);
    }
}
