package com.liansheng.carworld.bean;

public class CheckBean {
    String content;

    boolean check;

    public CheckBean(String content, boolean check) {
        this.content = content;
        this.check = check;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
