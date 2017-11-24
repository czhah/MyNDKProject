package com.thedream.cz.myndkproject.bean;

import javax.inject.Inject;

/**
 * Created by chenzhuang on 2017/10/24.
 * Describe:
 */
public class SideInfo {
    private int id;
    private String name;


    public SideInfo() {
    }

    @Inject
    public SideInfo(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SideInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
