package com.thedream.cz.myndkproject.data.entity;

/**
 * Created by Administrator on 2017/12/15.
 */

public class FileUpLoadInfo {
    private String img;
    private String thumbnail;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "FileUpLoadInfo{" +
                "img='" + img + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
