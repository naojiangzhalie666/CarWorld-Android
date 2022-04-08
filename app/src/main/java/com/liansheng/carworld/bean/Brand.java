package com.liansheng.carworld.bean;

import com.liansheng.carworld.utils.PinYinStringHelper;

import java.util.List;

public class Brand {
    /**
     * pageIndex : 0
     * pageSize : 0
     * pageCount : 0
     * total : 0
     * items : [{"id":"3fa85f64-5717-4562-b3fc-2c963f66afa6","name":"string","letter":"string","hot":true,"icon":"string","updation":"2020-12-30T09:25:08.501Z","creation":"2020-12-30T09:25:08.501Z"}]
     */

    private int pageIndex;
    private int pageSize;
    private int pageCount;
    private int total;
    private List<ItemsBean> items;

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

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * id : 3fa85f64-5717-4562-b3fc-2c963f66afa6
         * name : string
         * letter : string
         * hot : true
         * icon : string
         * updation : 2020-12-30T09:25:08.501Z
         * creation : 2020-12-30T09:25:08.501Z
         */

        private String id;
        private String name;
        private String letter;
        private boolean hot;
        private String icon;
        private String updation;
        private String creation;
        private String word;
        private String logo;
        private String total;

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getWord() {
//            return  PinYinStringHelper.getAlpha(name);
            return letter;
        }

        public void setWord(String word) {
//            this.word = PinYinStringHelper.getAlpha(name);
            this.word = letter;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLetter() {
            return letter;
        }

        public void setLetter(String letter) {
            this.letter = letter;
        }

        public boolean isHot() {
            return hot;
        }

        public void setHot(boolean hot) {
            this.hot = hot;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getUpdation() {
            return updation;
        }

        public void setUpdation(String updation) {
            this.updation = updation;
        }

        public String getCreation() {
            return creation;
        }

        public void setCreation(String creation) {
            this.creation = creation;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }
    }
}
