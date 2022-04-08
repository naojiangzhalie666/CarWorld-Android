package com.liansheng.carworld.bean.event;

public class UIEvent {
    public static final int UPDATE_LOGIN = 1001;
    public static final int UPDATE_HOME = 1002;
    public static final int UPDATE_COMPANY = 1003;

    private int type;

    public UIEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
