package com.liansheng.carworld.bean;

import java.util.List;

public class Directory {

    /**
     * pageIndex : 0
     * pageSize : 0
     * pageCount : 0
     * total : 0
     * items : [{"id":"3fa85f64-5717-4562-b3fc-2c963f66afa6","name":"string","photo":"string","province":"string","brand":"string","company":"string","mobile":"string","phone":"string","address":"string","voteCount":0,"collectionCount":0,"voted":true,"collected":true,"creation":"2021-02-08T02:53:10.815Z"}]
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
         * photo : string
         * province : string
         * brand : string
         * company : string
         * mobile : string
         * phone : string
         * address : string
         * voteCount : 0
         * collectionCount : 0
         * voted : true
         * collected : true
         * creation : 2021-02-08T02:53:10.815Z
         */

        private String id;
        private String name;
        private String photo;
        private String province;
        private String brand;
        private String company;
        private String mobile;
        private String phone;
        private String address;
        private int voteCount;
        private int collectionCount;
        private boolean voted;
        private boolean collected;
        private String creation;

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

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(int voteCount) {
            this.voteCount = voteCount;
        }

        public int getCollectionCount() {
            return collectionCount;
        }

        public void setCollectionCount(int collectionCount) {
            this.collectionCount = collectionCount;
        }

        public boolean isVoted() {
            return voted;
        }

        public void setVoted(boolean voted) {
            this.voted = voted;
        }

        public boolean isCollected() {
            return collected;
        }

        public void setCollected(boolean collected) {
            this.collected = collected;
        }

        public String getCreation() {
            return creation;
        }

        public void setCreation(String creation) {
            this.creation = creation;
        }
    }
}
