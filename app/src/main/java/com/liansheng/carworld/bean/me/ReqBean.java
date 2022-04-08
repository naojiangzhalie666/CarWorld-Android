package com.liansheng.carworld.bean.me;

import java.util.List;

public class ReqBean<T> {

    private List<T> items;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
