package com.liansheng.carworld.bean.company;

import java.util.List;

public class InsuranceBean {

    private List<SchemasBean> schemas;

    public List<SchemasBean> getSchemas() {
        return schemas;
    }

    public void setSchemas(List<SchemasBean> schemas) {
        this.schemas = schemas;
    }

    public static class SchemasBean {
        private String title;
        private List<ItemsBean> items;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean {
            private String title;
            private String subTitle;
            private String url;
            private String weixin;
            private String sale;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSubTitle() {
                return subTitle;
            }

            public void setSubTitle(String subTitle) {
                this.subTitle = subTitle;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getWeixin() {
                return weixin;
            }

            public void setWeixin(String weixin) {
                this.weixin = weixin;
            }

            public String getSale() {
                return sale;
            }

            public void setSale(String sale) {
                this.sale = sale;
            }
        }
    }
}
