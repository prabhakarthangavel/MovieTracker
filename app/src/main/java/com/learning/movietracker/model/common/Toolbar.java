package com.learning.movietracker.model.common;

public class Toolbar {
    private String pageTitle;
    private boolean isBack;
    private Integer rightIcon;
    private Integer showRightIcon;

    public Toolbar(String pageTitle, boolean isBack) {
        this.pageTitle = pageTitle;
        this.isBack = isBack;
    }

    public Toolbar(String pageTitle, boolean isBack, Integer showRightIcon, Integer rightIcon) {
        this.pageTitle = pageTitle;
        this.isBack = isBack;
        this.showRightIcon = showRightIcon;
        this.rightIcon = rightIcon;
    }

    public Toolbar(String pageTitle, boolean isBack, Integer showRightIcon) {
        this.pageTitle = pageTitle;
        this.isBack = isBack;
        this.showRightIcon = showRightIcon;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public boolean isBack() {
        return isBack;
    }

    public void setBack(boolean back) {
        isBack = back;
    }

    public Integer getRightIcon() {
        return rightIcon;
    }

    public void setRightIcon(Integer rightIcon) {
        this.rightIcon = rightIcon;
    }

    public Integer isShowRightIcon() {
        return showRightIcon;
    }

    public void setShowRightIcon(Integer showRightIcon) {
        this.showRightIcon = showRightIcon;
    }
}
