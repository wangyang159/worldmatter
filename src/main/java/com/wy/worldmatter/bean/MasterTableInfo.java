package com.wy.worldmatter.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 作者: wangyang <br/>
 * 创建时间: 2023/1/3 <br/>
 * 描述: <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;MasterTableInfo 功能列表项
 */
public class MasterTableInfo implements Serializable {
    private Integer xh;//序号
    private String gnName;//功能名称
    private String gnMeg;//功能简介
    private List<MasterTableInfoPath> path;//功能连接

    public Integer getXh() {
        return xh;
    }

    public void setXh(Integer xh) {
        this.xh = xh;
    }

    public String getGnName() {
        return gnName;
    }

    public void setGnName(String gnName) {
        this.gnName = gnName;
    }

    public String getGnMeg() {
        return gnMeg;
    }

    public void setGnMeg(String gnMeg) {
        this.gnMeg = gnMeg;
    }

    public List<MasterTableInfoPath> getPath() {
        return path;
    }

    public void setPath(List<MasterTableInfoPath> path) {
        this.path = path;
    }

    public MasterTableInfo() {
    }

    public MasterTableInfo(Integer xh, String gnName, String gnMeg, List<MasterTableInfoPath> path) {
        this.xh = xh;
        this.gnName = gnName;
        this.gnMeg = gnMeg;
        this.path = path;
    }

    @Override
    public String toString() {
        return "MasterTableInfo{" +
                "xh=" + xh +
                ", gnName='" + gnName + '\'' +
                ", gnMeg='" + gnMeg + '\'' +
                ", path=" + path +
                '}';
    }
}
