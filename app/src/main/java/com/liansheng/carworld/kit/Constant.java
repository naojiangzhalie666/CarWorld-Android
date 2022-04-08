package com.liansheng.carworld.kit;

public interface Constant {

    String KEY_TOKEN = "token";

    String KEY_BEAN = "bean";

    String KEY_TYPE = "type";

    String KEY_CITY = "current_city";

    String KEY_TITLE = "title";

    String KEY_URL = "url";

    String KEY_ID = "id";

    String KEY_POSITION = "position";

    String KEY_START = "start";
    String KEY_END = "end";

    String SERVICE_PHONE = "4008678280";

    int DEFULT_RESULT = 20;//默认返回条数

    int JPUSH_MESSAGE = 1234;

    String REGULAR_STATE = "regular_state";

    String CHILD_TYPE = "child";

    //身份证、营业执照、货物保险、商业保险、强制保险、驾驶执照
    // IdentityCard, BusinessLicense, CargoInsurance, CommercialInsurance, CompulsoryInsurance, DrivingLicense
    //身份证
    String AUTH_IDENTITY_CARD = "IdentityCard";
    //强制保险
    String AUTH_COMPULSORY_INSURANCE = "CompulsoryInsurance";
    //驾驶证执照
    String AUTH_DRIVERS_LICENSE = "DriversLicense";
    //行驶证执照
    String AUTH_DRIVING_LICENSE = "DrivingLicense";
    //营业执照
    String AUTH_BUSINESS_LICENSE = "BusinessLicense";
    //货物保险
    String AUTH_CARGO_INSURANCE = "CargoInsurance";
    //商业保险
    String AUTH_COMMERCIAL_INSURANCE = "CommercialInsurance";

    //证件审核状态
    String ID_STATUS_VERIFYING = "verifying";//审核中
    String ID_STATUS_PASSED = "passed";//已认证
    String ID_STATUS_REJECT = "reject";//驳回

    //店铺状态  1 汽贸 2 4s
    String STORE_TYPE_1 = "AutoShop";
    String STORE_TYPE_2 = "AutomobileSalesServicshop";
    String STORE_TYPE_3 = "ForwardingCompany";

    //用户类型 4S Driver AutoShop
    String USER_TYPE_1 = "AutoShop";
    String USER_TYPE_2 = "Driver";
    String USER_TYPE_3 = "4S";
    String USER_TYPE_4 = "ForwardingCompany";

}
