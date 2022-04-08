package com.liansheng.carworld.bean;

import java.util.List;

public class ListDeleteBean {

    private List<Item>  items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class Item {

        private String id;
        private boolean sold;
        private boolean off;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isSold() {
            return sold;
        }

        public void setSold(boolean sold) {
            this.sold = sold;
        }

        public boolean isOff() {
            return off;
        }

        public void setOff(boolean off) {
            this.off = off;
        }
    }
}
