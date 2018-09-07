package com.example.sig.lianjiang.bean;

/**
 * Created by sig on 2018/9/4.
 */

public class PublishImage {
    private int pmId;
    private int publicId;
    private String path;

    public int getPmId() {
        return pmId;
    }

    public void setPmId(int pmId) {
        this.pmId = pmId;
    }

    public int getPublicId() {
        return publicId;
    }

    public void setPublicId(int publicId) {
        this.publicId = publicId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "PublishImage{" +
                "pmId=" + pmId +
                ", publicId=" + publicId +
                ", path='" + path + '\'' +
                '}';
    }
}
