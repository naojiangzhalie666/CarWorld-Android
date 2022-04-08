package com.liansheng.carworld.bean;

import com.google.gson.Gson;

public class Sts {

    /**
     * error_no : 0
     * msg :
     * data : {"AccessKeyId":"STS.NJATUbDGMEMvQFQw82aecPSgu","AccessKeySecret":"4fNkEkNHm1sHwJ7SaBLyfKsRKegCXpH2t5icSnd67xB6","SecurityToken":"CAISjgJ1q6Ft5B2yfSjIr4n0H+/WqZhs8o+dU2DgkzhnbepPv5bMlzz2IHhFeHZgCO0evvw2lWtY7/Yalq0pFc8fHZ974GDJqsY5yxioRqackf7XhOV2tf/IMGyXDAGBq622Su7lTdTbV+6wYlTf7EFayqf7cjPQMD7INoaS29wdLbZxZASjaidcD9p7PxZrrNRgVUHcLvGwKBXn8A2yaUNjoVh7kngtq/b9kI++kkOF0QKnl7BI+NWof8H4MJVWUc0hA4vv7otfbbHc1SNc0R9O+ZptgbZMkTW95YnMXwYBskjeY7KNo4Y/dV8hfNszH69Vsf77juZkve/ekYv6zRtXNP1SST7YQI2wWlM9+hsX6rwagAFfcf6EDCxPXrE4s5cXubOGKPnkyZSCo6gJ9h1gnbVuVLjpDRi9VcLJI3MYkqBx7L2cIwP1SoVnE96o5QIBIdCpVMs3+fhJ0F4oAU7Em+mTMHQfg3N3elT4wGwp0zQv2RNr5kGB3C2S0FLBUMFFR0iTAs4q7keUsJ6QJVZdVIYfGw==","Expiration":"2018-08-08T07:38:55Z"}
     */

    private int error_no;
    private String msg;
    private DataBean data;

    public static Sts objectFromData(String str) {

        return new Gson().fromJson(str, Sts.class);
    }

    public int getError_no() {
        return error_no;
    }

    public void setError_no(int error_no) {
        this.error_no = error_no;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * AccessKeyId : STS.NJATUbDGMEMvQFQw82aecPSgu
         * AccessKeySecret : 4fNkEkNHm1sHwJ7SaBLyfKsRKegCXpH2t5icSnd67xB6
         * SecurityToken : CAISjgJ1q6Ft5B2yfSjIr4n0H+/WqZhs8o+dU2DgkzhnbepPv5bMlzz2IHhFeHZgCO0evvw2lWtY7/Yalq0pFc8fHZ974GDJqsY5yxioRqackf7XhOV2tf/IMGyXDAGBq622Su7lTdTbV+6wYlTf7EFayqf7cjPQMD7INoaS29wdLbZxZASjaidcD9p7PxZrrNRgVUHcLvGwKBXn8A2yaUNjoVh7kngtq/b9kI++kkOF0QKnl7BI+NWof8H4MJVWUc0hA4vv7otfbbHc1SNc0R9O+ZptgbZMkTW95YnMXwYBskjeY7KNo4Y/dV8hfNszH69Vsf77juZkve/ekYv6zRtXNP1SST7YQI2wWlM9+hsX6rwagAFfcf6EDCxPXrE4s5cXubOGKPnkyZSCo6gJ9h1gnbVuVLjpDRi9VcLJI3MYkqBx7L2cIwP1SoVnE96o5QIBIdCpVMs3+fhJ0F4oAU7Em+mTMHQfg3N3elT4wGwp0zQv2RNr5kGB3C2S0FLBUMFFR0iTAs4q7keUsJ6QJVZdVIYfGw==
         * Expiration : 2018-08-08T07:38:55Z
         */

        private String AccessKeyId;
        private String AccessKeySecret;
        private String SecurityToken;
        private String Expiration;

        public static DataBean objectFromData(String str) {

            return new Gson().fromJson(str, DataBean.class);
        }

        public String getAccessKeyId() {
            return AccessKeyId;
        }

        public void setAccessKeyId(String AccessKeyId) {
            this.AccessKeyId = AccessKeyId;
        }

        public String getAccessKeySecret() {
            return AccessKeySecret;
        }

        public void setAccessKeySecret(String AccessKeySecret) {
            this.AccessKeySecret = AccessKeySecret;
        }

        public String getSecurityToken() {
            return SecurityToken;
        }

        public void setSecurityToken(String SecurityToken) {
            this.SecurityToken = SecurityToken;
        }

        public String getExpiration() {
            return Expiration;
        }

        public void setExpiration(String Expiration) {
            this.Expiration = Expiration;
        }
    }
}
