package com.wy.worldmatter.utils;

import com.wy.worldmatter.bean.MasterTableInfo;
import com.wy.worldmatter.bean.MasterTableInfoPath;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 作者: wangyang <br/>
 * 创建时间: 2023/1/3 <br/>
 * 描述: <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;MasterTable
 */
public class MasterTable {
    public static void main(String[] args) throws IOException {
        MasterTableInfo m1 = new MasterTableInfo();
        m1.setXh(1);
        m1.setGnName("证件照功能");
        m1.setGnMeg("目前有两种分为有背景色证件照和无背景色个人照");
        List<MasterTableInfoPath> p1 = new ArrayList<>();
        p1.add(new MasterTableInfoPath("/idphoto/zjzhavebj","证件照"));
        p1.add(new MasterTableInfoPath("/idphoto/zjz","个人照"));
        m1.setPath(p1);

        MasterTableInfo m2 = new MasterTableInfo();
        m2.setXh(2);
        m2.setGnName("PDF转Word");
        m2.setGnMeg("目前提供两种技术转Word,各自的优缺点见详情页");
        List<MasterTableInfoPath> p2 = new ArrayList<>();
        p2.add(new MasterTableInfoPath("/pdfs/appdf","AP技术"));
        p2.add(new MasterTableInfoPath("/pdfs/srpdf","SR技术"));
        m2.setPath(p2);

        MasterTableInfo m3 = new MasterTableInfo();
        m3.setXh(3);
        m3.setGnName("图片文字识别");
        m3.setGnMeg("使用tess4j图片文字识别");
        List<MasterTableInfoPath> p3 = new ArrayList<>();
        p3.add(new MasterTableInfoPath("/imgword/toimgword","图片转文字"));
        m3.setPath(p3);

        ArrayList<MasterTableInfo> list = new ArrayList<>(3);
        list.add(m1);
        list.add(m2);
        list.add(m3);

        FileOutputStream out = new FileOutputStream("mastertable.txt");
        ObjectOutputStream oout = new ObjectOutputStream(out);
        oout.writeObject(list);
        oout.flush();
        oout.close();
    }

}
