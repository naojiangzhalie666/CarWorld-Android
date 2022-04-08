package com.liansheng.carworld.bean;

import java.util.List;

public class UserMessage {
    /**
     * id : 3fa85f64-5717-4562-b3fc-2c963f66afa6
     * title : string
     * body : string
     * read : true
     * type : user
     * values : [{"key":"string","value":"string"}]
     * creation : 2021-01-27T03:28:22.003Z
     */

    private String id;
    private String title;
    private String body;
    private boolean read;
    private String type;
    private String creation;
    private List<ValuesBean> values;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    public List<ValuesBean> getValues() {
        return values;
    }

    public void setValues(List<ValuesBean> values) {
        this.values = values;
    }

    public static class ValuesBean {
        /**
         * key : string
         * value : string
         */

        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
