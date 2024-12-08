package com.learning.movietracker.model.common;

public class Toolbar {
    private String pageTitle;
    private Integer imageSrc;

    private boolean isBack;

    public Toolbar(String pageTitle, Integer imageSrc) {
        this.pageTitle = pageTitle;
        this.imageSrc = imageSrc;
    }

    public Toolbar(String pageTitle, boolean isBack) {
        this.pageTitle = pageTitle;
        this.isBack = isBack;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public Integer getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(Integer imageSrc) {
        this.imageSrc = imageSrc;
    }

    public boolean isBack() {
        return isBack;
    }

    public void setBack(boolean back) {
        isBack = back;
    }
}
