package com.liansheng.carworld.net;

import android.text.TextUtils;

import com.liansheng.carworld.kit.Constant;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.OtherRequestBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroid.hold.info.SharedInfo;
import cn.droidlover.xdroid.tools.log.Logger;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class NetApi {

    public static void postJson(String url, String json, JsonCallback callback) {
        Logger.e("uri=" + UrlKit.getUrl(url) + "&request=" + json);
        OkHttpUtils
                .postString()
                .url(UrlKit.getUrl(url))
//                .addHeader("Connection", "close")
                .addHeader("Authorization", SharedInfo.getInstance().getValue(Constant.KEY_TOKEN, "").toString())
                .content(json)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(callback);
    }

    public static void put(String url, String json, JsonCallback callback) {
        Logger.e("uri=" + UrlKit.getUrl(url) + "&request=" + json);
        OkHttpUtils
                .put()
                .url(UrlKit.getUrl(url))
                .addHeader("Connection", "close")
                .addHeader("Authorization", SharedInfo.getInstance().getValue(Constant.KEY_TOKEN, "").toString())
                .requestBody(RequestBody.create(MediaType.parse("application/json"), json))
                .build()
                .execute(callback);
    }

    public static void post(String url, HashMap<String, Object> hashMap, JsonCallback callback) {
        PostFormBuilder builder = OkHttpUtils.post().url(UrlKit.getUrl(url))
                .addHeader("Content-type", "application/x-www-form-urlencoded");
        if (!TextUtils.isEmpty(SharedInfo.getInstance().getValue(Constant.KEY_TOKEN, "").toString())) {
            builder.addHeader("Authorization", SharedInfo.getInstance().getValue(Constant.KEY_TOKEN, "").toString());
        }
        if (hashMap != null) {
            for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
                builder.addParams(entry.getKey(), entry.getValue().toString());
            }
            Logger.e("uri=" + UrlKit.getUrl(url) + "&request=" + hashMap.toString());
        } else {
            Logger.e("uri=" + UrlKit.getUrl(url));
        }
        builder.build().execute(callback);
    }

    public static void get(String url, HashMap<String, Object> hashMap, JsonCallback callback) {
        GetBuilder builder = OkHttpUtils.get().url(UrlKit.getUrl(url))
                .addHeader("Content-type", "application/x-www-form-urlencoded");
        if (!TextUtils.isEmpty(SharedInfo.getInstance().getValue(Constant.KEY_TOKEN, "").toString())) {
            builder.addHeader("Authorization", SharedInfo.getInstance().getValue(Constant.KEY_TOKEN, "").toString());
        }
        if (hashMap != null) {
            for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
                builder.addParams(entry.getKey(), entry.getValue().toString());
            }
            Logger.e("uri=" + UrlKit.getUrl(url) + "&request=" + hashMap.toString());
        } else {
            Logger.e("uri=" + UrlKit.getUrl(url));
        }
        builder.build().execute(callback);
    }

    public static void put(String url, HashMap<String, Object> hashMap, JsonCallback callback) {
        OtherRequestBuilder requestBuilder = OkHttpUtils.put().url(UrlKit.getUrl(url)).addHeader("Content-type", "application/x-www-form-urlencoded");
        FormBody.Builder body = new FormBody.Builder();
        if (hashMap != null) {
            for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
                body.add(entry.getKey(), entry.getValue().toString());
            }
            Logger.e("uri=" + UrlKit.getUrl(url) + "&request=" + hashMap.toString());
        } else {
            Logger.e("uri=" + UrlKit.getUrl(url));
        }
        if (!TextUtils.isEmpty(SharedInfo.getInstance().getValue(Constant.KEY_TOKEN, "").toString())) {
            requestBuilder.addHeader("Authorization", SharedInfo.getInstance().getValue(Constant.KEY_TOKEN, "").toString());
        }
        requestBuilder.requestBody(body.build()).build().execute(callback);
    }

    public static void delete(String url,HashMap<String,Object> hashMap,JsonCallback callback){
        OtherRequestBuilder requestBuilder = OkHttpUtils.delete().url(UrlKit.getUrl(url)).addHeader("Content-type", "application/x-www-form-urlencoded");
        FormBody.Builder body = new FormBody.Builder();
        if (hashMap != null) {
            for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
                body.add(entry.getKey(), entry.getValue().toString());
            }
            Logger.e("uri=" + UrlKit.getUrl(url) + "&request=" + hashMap.toString());
        } else {
            Logger.e("uri=" + UrlKit.getUrl(url));
        }
        if (!TextUtils.isEmpty(SharedInfo.getInstance().getValue(Constant.KEY_TOKEN, "").toString())) {
            requestBuilder.addHeader("Authorization", SharedInfo.getInstance().getValue(Constant.KEY_TOKEN, "").toString());
        }
        requestBuilder.requestBody(body.build()).build().execute(callback);
    }

    public static void delete(String url, JsonCallback callback,String json) {
        Logger.e("uri=" + UrlKit.getUrl(url) + "&request=" + json);
        OkHttpUtils
                .delete()
                .url(UrlKit.getUrl(url))
                .addHeader("Connection", "close")
                .addHeader("Authorization", SharedInfo.getInstance().getValue(Constant.KEY_TOKEN, "").toString())
                .requestBody(RequestBody.create(MediaType.parse("application/json"), json))
                .build()
                .execute(callback);
    }

    public static void postResources(List<LocalMedia> selectList, JsonCallback callback) {
        PostFormBuilder post = OkHttpUtils.post();
        String url = UrlKit.getUrl(UrlKit.RESOURCES);
//        Map<String, File> map = new HashMap<>();
        post.url(url).addHeader("Content-type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", SharedInfo.getInstance().getValue(Constant.KEY_TOKEN, "").toString());
        for (int i = 0; i < selectList.size(); i++) {
            File file = new File(selectList.get(i).getCompressPath());
            post.addFile("files", file.getName(), file);
        }
        post.build().execute(callback);
    }

    public static void postResources(String token, String img, JsonCallback callback) {
        String url = UrlKit.getUrl(UrlKit.RESOURCES);
        File file = new File(img);
        OkHttpUtils.post().url(url).addHeader("Content-type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", token)
                .addFile("files", file.getName(), file)
                .build()
                .execute(callback);
    }

    public static void postDriver(String token, String orderId, JsonCallback callback) {
        String url = UrlKit.getUrl(UrlKit.TRANSPORT_ORDER_DRIVER);
        OkHttpUtils.post().url(url).addHeader("Content-type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", token)
                .addParams("orderId", orderId)
                .build()
                .execute(callback);
    }

    public static void postAssess(String token, String orderId, String type, JsonCallback callback) {
        String url = UrlKit.getUrl(UrlKit.ASSESS);
        OkHttpUtils.post().url(url).addHeader("Content-type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", token)
                .addParams("orderId", orderId)
                .addParams("type", type)
                .build()
                .execute(callback);
    }

    public static void postCashOut(String token, String amount, String account, JsonCallback callback) {
        String url = UrlKit.getUrl(UrlKit.CASHOUT);
        OkHttpUtils.post().url(url).addHeader("Content-type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", token)
                .addParams("amount", amount)
                .addParams("account", account)
                .build()
                .execute(callback);
    }

    public static void postIncreasePrice(String token, String orderId, String price, JsonCallback callback) {
        String url = UrlKit.getUrl(UrlKit.INCREASEPRICE);
        OkHttpUtils.post().url(url).addHeader("Content-type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", token)
                .addParams("orderId", orderId)
                .addParams("price", price)
                .build()
                .execute(callback);
    }

    public static void postCollection(String token, String directoryId, JsonCallback callback) {
        String url = UrlKit.getUrl(UrlKit.COLLECTION);
        OkHttpUtils.post().url(url).addHeader("Content-type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", token)
                .addParams("directoryId", directoryId)
                .build()
                .execute(callback);
    }

    public static void postVote(String token, String directoryId, JsonCallback callback) {
        String url = UrlKit.getUrl(UrlKit.VOTE);
        OkHttpUtils.post().url(url).addHeader("Content-type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", token)
                .addParams("directoryId", directoryId)
                .build()
                .execute(callback);
    }

    public static void postSuggest(String token, String directoryId, String description, JsonCallback callback) {
        String url = UrlKit.getUrl(UrlKit.SUGGEST);
        OkHttpUtils.post().url(url).addHeader("Content-type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", token)
                .addParams("directoryId", directoryId)
                .addParams("description", description)
                .build()
                .execute(callback);
    }

    public static void putForgetPsw(String mobile, String code, String password, JsonCallback callback) {
        String url = UrlKit.getUrl(UrlKit.PASSWORD);
        FormBody body = new FormBody.Builder()
                .add("mobile", mobile)
                .add("code", code)
                .add("password", password)
                .build();
        OkHttpUtils.put().url(url).addHeader("Content-type", "application/x-www-form-urlencoded")
                .requestBody(body)
                .build()
                .execute(callback);
    }

    public static void putCancelOrder(String token, String transportOrderId, JsonCallback callback) {
        String url = UrlKit.getUrl(UrlKit.ORDER_CANCEL);
        FormBody body = new FormBody.Builder()
                .add("transportOrderId", transportOrderId)
                .build();
        OkHttpUtils.put().url(url).addHeader("Content-type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", token)
                .requestBody(body)
                .build()
                .execute(callback);
    }

    public static void putNextStatus(String token, String orderId, JsonCallback callback) {
        String url = UrlKit.getUrl(UrlKit.NEXTSTATUS);
        FormBody body = new FormBody.Builder()
                .add("orderId", orderId)
                .build();
        OkHttpUtils.put().url(url).addHeader("Content-type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", token)
                .requestBody(body)
                .build()
                .execute(callback);
    }


    public static void putHeadPhoto(String token, String headphoto, JsonCallback callback) {
        String url = UrlKit.getUrl(UrlKit.HEADPHOTO);
        FormBody body = new FormBody.Builder()
                .add("headphoto", headphoto)
                .build();
        OkHttpUtils.put().url(url).addHeader("Content-type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", token)
                .requestBody(body)
                .build()
                .execute(callback);
    }

    public static void putCompany(String token, String name, String lon, String lat, String province, String city, String county, String street, JsonCallback callback) {
        String url = UrlKit.getUrl(UrlKit.COMPANY);
        FormBody body = new FormBody.Builder()
//                .add("company", company)
                .add("longitude", lon)
                .add("latitude", lat)
                .add("province", province)
                .add("city", city)
                .add("county", county)
                .add("name", name)
                .add("street", street)
                .build();
        OkHttpUtils.put().url(url).addHeader("Content-type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", token)
                .requestBody(body)
                .build()
                .execute(callback);
    }

    public static void putVip(String token, int months, JsonCallback callback) {
        String url = UrlKit.getUrl(UrlKit.VIP);
        FormBody body = new FormBody.Builder()
                .add("months", months + "")
                .build();
        OkHttpUtils.put().url(url).addHeader("Content-type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", token)
                .requestBody(body)
                .build()
                .execute(callback);
    }

    public static void getSms(String mobile, JsonCallback callback) {
        String url = UrlKit.getUrl(UrlKit.SMS);
        OkHttpUtils.get().url(url).addHeader("Content-type", "application/x-www-form-urlencoded")
                .addParams("mobile", mobile)
                .build()
                .execute(callback);
    }

    public static void getRecnent(String token, JsonCallback callback) {
        String url = UrlKit.getUrl(UrlKit.RECENT);
        OkHttpUtils.get().url(url).addHeader("Content-type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", token)
                .addParams("count", "5")
                .addParams("type", "destination")
                .build()
                .execute(callback);
    }

    public static void getAliPay(String token, String orderId, JsonCallback callback) {
        String url = UrlKit.getUrl(UrlKit.ALIPAY);
        OkHttpUtils.get().url(url).addHeader("Content-type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", token)
                .addParams("orderId", orderId)
                .build()
                .execute(callback);
    }

    public static void getSerachList(String token, String pageIndex, String pageSize, String status, String city, JsonCallback callback) {
        String url = UrlKit.getUrl(UrlKit.TRANSPORT_ORDER);
        if (status.equals("origin")) {
            OkHttpUtils.get().url(url).addHeader("Content-type", "application/x-www-form-urlencoded")
                    .addHeader("Authorization", token)
                    .addParams("pageIndex", pageIndex)
                    .addParams("pageSize", pageSize)
                    .addParams("privately", "true")
                    .addParams("origin.city", city)
                    .build()
                    .execute(callback);
        } else {
            OkHttpUtils.get().url(url).addHeader("Content-type", "application/x-www-form-urlencoded")
                    .addHeader("Authorization", token)
                    .addParams("pageIndex", pageIndex)
                    .addParams("pageSize", pageSize)
                    .addParams("privately", "true")
                    .addParams("destination.city", city)
                    .build()
                    .execute(callback);
        }
    }

    public static void putPostion(String token, String latitude, String longitude, JsonCallback callback) {
        String url = UrlKit.getUrl(UrlKit.POSTION);
        FormBody body = new FormBody.Builder()
                .add("latitude", latitude)
                .add("longitude", longitude)
                .build();
        OkHttpUtils.put().url(url).addHeader("Content-type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", token)
                .requestBody(body)
                .build()
                .execute(callback);
    }

    public static void delCollection(String id, JsonCallback callback) {
        String url = UrlKit.getUrl(UrlKit.CAR_RESOURCE_COLLECTION);
        FormBody body = new FormBody.Builder()
                .add("id", id)
                .build();
        OkHttpUtils.delete().url(url).addHeader("Content-type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", SharedInfo.getInstance().getValue(Constant.KEY_TOKEN,"").toString())
                .requestBody(body)
                .build()
                .execute(callback);
    }

    public static void delCollection(String token, String directoryId, JsonCallback callback) {
        String url = UrlKit.getUrl(UrlKit.COLLECTION);
        FormBody body = new FormBody.Builder()
                .add("directoryId", directoryId)
                .build();
        OkHttpUtils.delete().url(url).addHeader("Content-type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", token)
                .requestBody(body)
                .build()
                .execute(callback);
    }

    public static void delVote(String token, String directoryId, JsonCallback callback) {
        String url = UrlKit.getUrl(UrlKit.VOTE);
        FormBody body = new FormBody.Builder()
                .add("directoryId", directoryId)
                .build();
        OkHttpUtils.delete().url(url).addHeader("Content-type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", token)
                .requestBody(body)
                .build()
                .execute(callback);
    }

}
