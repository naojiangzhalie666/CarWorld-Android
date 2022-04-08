package com.liansheng.carworld.oss;

import android.text.TextUtils;

import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.google.gson.Gson;
import com.liansheng.carworld.bean.other.OssBean;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;

import okhttp3.Call;

public class STSGetter extends OSSFederationCredentialProvider {

    String ak;
    String sk;
    String token;
    String expiration;

    public STSGetter(String ak, String sk, String token, String expiration) {
        this.ak = ak;
        this.sk = sk;
        this.token = token;
        this.expiration = expiration;
    }

    public STSGetter() {
    }

    public OSSFederationToken getFederationToken() {
        getParams();
        return new OSSFederationToken(ak, sk, token, expiration);
    }


    private void getParams() {
        NetApi.get(UrlKit.OSS_CONFIG, null, new JsonCallback() {

            @Override
            public void onSuccess(String response, int id) {
                OssBean ossBean = new Gson().fromJson(response, OssBean.class);
                if (!TextUtils.isEmpty(ossBean.getAccessKeyId())) {
                    ak = ossBean.getAccessKeyId();
                    sk = ossBean.getAccessKeySecret();
                    token = ossBean.getSecurityToken();
                    expiration = ossBean.getExpiration();
                }
            }
        });
    }
}