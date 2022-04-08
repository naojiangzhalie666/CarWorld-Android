package com.liansheng.carworld.activity.logic;

public interface LoginResult {
    void login(String YDToken,String accessCode);
    void onError(String error);
}
