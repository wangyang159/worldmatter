package com.wy.worldmatter.bean;

import java.io.Serializable;

/**
 * 作者: wangyang <br/>
 * 创建时间: 2023/1/4 <br/>
 * 描述: <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;MasterTableInfoPath
 */
public class MasterTableInfoPath implements Serializable {
    private String hrefPath;
    private String hrefName;

    public MasterTableInfoPath() {
    }

    public MasterTableInfoPath(String hrefPath, String hrefName) {
        this.hrefPath = hrefPath;
        this.hrefName = hrefName;
    }

    public String getHrefPath() {
        return hrefPath;
    }

    public void setHrefPath(String hrefPath) {
        this.hrefPath = hrefPath;
    }

    public String getHrefName() {
        return hrefName;
    }

    public void setHrefName(String hrefName) {
        this.hrefName = hrefName;
    }

    @Override
    public String toString() {
        return "MasterTableInfoPath{" +
                "hrefPath='" + hrefPath + '\'' +
                ", hrefName='" + hrefName + '\'' +
                '}';
    }
}
