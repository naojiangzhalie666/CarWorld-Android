package com.liansheng.carworld.bean.other;

import java.util.List;

public class PageBean<T> {

    /**
     * pageIndex : 1
     * pageSize : 10
     * pageCount : 0
     * total : 0
     * items : []
     */

    private int pageIndex;
    private int pageSize;
    private int pageCount;
    private int total;
    private List<T> items;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

}
