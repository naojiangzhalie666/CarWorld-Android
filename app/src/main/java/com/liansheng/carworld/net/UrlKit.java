package com.liansheng.carworld.net;


import android.os.Environment;
import android.text.TextUtils;

import cn.droidlover.xdroid.tools.utils.ActivityManage;

public class UrlKit {

    public static final boolean IS_DEBUG = false;

        public static final String API_BASE_URL = IS_DEBUG ? "http://61.153.14.147:5000" : "https://www.wzlsqc.com";
//    public static final String API_BASE_URL = IS_DEBUG ? "http://61.153.14.147:5000" : "http://www.wzxsqc.cn";

//    public static final String API_BASE_URL = "http://www.wzlsqc.com/";
//        public static final String API_BASE_URL = "http://highness:5000/";

//  public static final String PAY_BASE_URL = "http://192.168.1.103:8081/";

    public static final String APP_ID = "wx6427f3020a955519";
    public static final String WxAPPSECRET = "21fc04a8fcecf66cc50e97f018ba95b2";

    public static final String XIAOMI_ID = "2882303761517843952";
    public static final String XIAOMI_KEY = "5901784335952";

    public static final String UMENG_KEY = "60d03a9a8a102159db71f5a4";

    public static final String NETESA_ID = "7b8d84d405ba449cbce679bbd1df9673";

    public static final String H5_SECRET_REGULAR = API_BASE_URL + "/secret_regular.html";
    public static final String H5_REGISTER_REGULAR = API_BASE_URL + "/user_service_regular.html";
    public static final String H5_TRANSPORT_SERVICE = API_BASE_URL + "/transport_service.html";
    public static final String H5_SEARCH_SERVICE_REGULAR = API_BASE_URL + "/search_service_regular.html";
    public static final String H5_DEPOSIT_PROTECTION_REGULAR = API_BASE_URL + "/deposit_protection_regular.html";
    public static final String H5_INSURANCE_CAR_SMALL_1 = API_BASE_URL + "/insurance_car_small_1.html";
    public static final String H5_INSURANCE_CAR_SMALL_2 = API_BASE_URL + "/insurance_car_small_2.html";
    public static final String H5_INSURANCE_CAR_BIG = API_BASE_URL + "/insurance_car_big.html";
    public static final String H5_PARTNER = API_BASE_URL + "/partner_regular.html";
    public static final String H5_CAR_DETAILS = API_BASE_URL+"/serie-mode.html?fct=%s&serie=%s";

    public static final String LOGIN_STATE = "LOGIN_STATE";
    public static final String LAST_LOGIN_STATE = "LAST_LOGIN_STATE";
    public static final String COOKIE = "cookie";
    public static final String COOKIE_LOGIN = "cookie_login";
    public static final String LOGIN_RESULT = "login_result";
    public static final String FIRST_START = "first_start";
    public static final String FIRST_RESULT = "first_result";
    public static final String FIRST_ML = "first_ml";

    public static final String OSS_URL = "https://chewujie.oss-cn-beijing.aliyuncs.com/wwwroot";
    public static final String OSS_URL2 = "https://chewujie.oss-cn-beijing.aliyuncs.com";

    public static String getImgUrl(String action) {
        if (TextUtils.isEmpty(action)) {
            return "";
        }
        if (action.startsWith("http")) {
            return action;
        }
        if (action.contains("wwwroot")) {
            if (action.startsWith("/")) {
                return new StringBuilder(OSS_URL2).append(action).toString();
            } else {
                return new StringBuilder(OSS_URL2).append("/").append(action).toString();
            }
        } else {
            if (action.startsWith("/")) {
                return new StringBuilder(OSS_URL).append(action).toString();
            } else {
                return new StringBuilder(OSS_URL).append("/").append(action).toString();
            }
        }
    }

    public static String getUrl(String action) {
        if (TextUtils.isEmpty(action)) {
            return "";
        }
        if (action.startsWith("/")) {
            return new StringBuilder(API_BASE_URL).append(action).toString();
        } else {
            return new StringBuilder(API_BASE_URL).append("/").append(action).toString();
        }
    }

    //    public static String getPayUrl(String action) {
//        return new StringBuilder(PAY_BASE_URL).append(action).toString();
//    }
    public static final String OSS_CONFIG = "api/app/resources/oss-credentials";

    public static final String LOGIN = "api/app/auth";
    public static final String USER_INFORM = "api/app/user";
    public static final String TRANSPORT_ORDER = "api/app/transport_order";
    public static final String GET_DISTANCE = "api/app/price";
    public static final String ORDER_DETIALS = "api/app/transport_order/";
    public static final String CHANGE_PSW = "api/app/user/password";
    public static final String HEADPHOTO = "api/app/user/headphoto";
    public static final String RESOURCES = "api/app/resources";
    public static final String NICKNAME = "api/app/user/nickname";
    public static final String WECHAT = "api/app/user/wechat";
    public static final String CITY = "api/app/user/city";
    public static final String COMPANY = "api/app/user/address";
    public static final String SMS = "api/app/validation/sms";
    public static final String PASSWORD = "api/app/auth/password";
    public static final String REG = "api/app/auth/user";
    //??????
    public static final String TRANSPORT_ORDER_DRIVER = "api/app/transport_order/driver";
    public static final String RECENT = "api/app/transport_order/recent_address";
    public static final String ALIPAY = "api/app/transport_order/pay";
    public static final String ASSESS = "api/app/transport_order/assess";
    public static final String ORDER_CANCEL = "api/app/transport_order/cancel";
    //????????????
    public static final String BRAND = "api/app/directory/brand";
    //??????????????????????????????
    public static final String COLLECTED_BRAND = "api/app/user/directoryCollectedBrand";
    public static final String DIRECTORY = "api/app/directory";
    public static final String AREA_PROVINCE = "api/app/directory/area/province";
    public static final String AREA_CITY = "api/app/directory/area/city";
    public static final String POSTION = "api/app/user/position";
    public static final String CASHOUT = "api/app/user/cashout";
    public static final String MESSAGE = "api/app/user/notification";
    public static final String MESSAGECOUNT = "api/app/user/unread_notification_counts";
    public static final String INCREASEPRICE = "api/app/transport_order/increase_price";

    public static final String NEXTSTATUS = "api/app/transport_order/next_status";
    //????????????
    public static final String COLLECTION = "api/app/directory/collection";
    public static final String VOTE = "api/app/directory/vote";
    public static final String FORM = "api/app/directory/form";
    public static final String SUGGEST = "api/app/directory/suggestion";
    public static final String VIP = "api/app/user/vip";
    public static final String INVITE = "api/app/user/income";
    public static final String USER_INVITE_LIST = "api/app/user/relation";
    //??????????????????
    public static final String USER_COMPANY = "api/app/user/company";
    public static final String USER_COMPANYS = "api/app/user/companys";
    //??????
    public static final String USER_LICENSE = "api/app/user/license";
    public static final String USER_LICENSES = "api/app/user/licenses";
    public static final String USER_CAR = "api/app/user/car";
    //??????
    public static final String RECHARGE_VIP_PRICE = "api/app/vip_price";
    public static final String RECHARGE_VIP_ORDER = "api/app/user/vipOrder";
    public static final String RECHARGE_VIP_ORDER_PAY = "api/app/user/vipOrderPay";

    //????????????????????????
    public static final String USER_INVITATION = "api/app/user/invitation";
    //??????????????????
    public static final String USER_BASIC_BY_MOBILE = "api/app/user/basicbyMobile/";

    //??????/????????????
    public static final String USER_MEMBER = "api/app/user/member";
    public static final String USER_MEMBER_DELETE = "api/app/user/member/";
    //??????????????????
    public static final String USER_MEMBER_STATUS = "api/app/user/company/member/passed";

    //+++++++++++++++++++++++++ ????????? +++++++++++++++++++++++++//
    //?????????-????????????
    public static final String CAR_RESOURCE = "api/app/car_resource";
    public static final String CAR_RESOURCE_COUNT = "api/app/car_resource/count";
    //??????
    public static final String CAR_RESOURCE_OFF = "api/app/car_resource/off";
    //??????
    public static final String CAR_RESOURCE_ON = "api/app/car_resource/on";
    //??????
    public static final String CAR_RESOURCE_SOLD = "api/app/car_resource/sold/";
    //????????????????????????
    public static final String CAR_RESOURCE_UPDATE_DATE = "api/app/car_resource/updateDate/";
    //????????????
    public static final String CAR_RESOURCE_DETAILS = "api/app/car_resource/";
    //?????????-????????????
    public static final String CAR_RESOURCE_RECOMMEND = "api/app/car_resource/recommend";
    public static final String CAR_RESOURCE_BRAND = "api/app/car_resource/brand";
    public static final String CAR_RESOURCE_FCT = "api/app/car_resource/fct";
    public static final String CAR_RESOURCE_SERIE = "api/app/car_resource/serie";
    public static final String CAR_RESOURCE_SUMMARY = "api/app/car_resource/summary";
    //??????????????????
    public static final String CAR_RESOURCE_BRAND_ORDER = "api/app/car_resource_order";
    public static final String CAR_RESOURCE_BRAND_ORDER_DETAILS = "api/app/car_resource_order/";

    public static final String CAR_RESOURCE_ORDER_OFF = "api/app/car_resource_order/off/";
    //?????????
    public static final String USER_COUPON = "api/app/user/coupon";
    //???????????????
    public static final String USER_COUPON_READ = "api/app/user/coupon/read/";

    //??????????????????????????????
    public static final String CAR_RESOURCE_SHARED = "api/app/car_resource/shared";
    //????????????
    public static final String CAR_RESOURCE_MOBILE = "api/app/car_resource/mobile";
    //??????
    public static final String CAR_RESOURCE_CORRECT = "api/app/car_resource/correct";
    //????????????
    public static final String CAR_RESOURCE_COLLECTION = "api/app/car_resource/collection";

    //vin??????
    public static final String PRICE_CAR_CONDITION = "api/app/price/car_condition/%s/%s";
    //??????????????????
    public static final String APP_INSURANCE_ORDER = "api/app/insurance_order";
    public static final String APP_INSURANCE_ORDER_REPORT = "api/app/insurance_order/report/";
    public static final String APP_INSURANCE_ORDER_PAY = "api/app/insurance_order/pay";

    //??????????????????
    public static final String APP_MTNCE_ORDER = "api/app/mtnce_order";
    public static final String APP_MTNCE_ORDER_REPORT = "api/app/mtnce_order/report/";
    public static final String APP_MTNCE_ORDER_PAY = "api/app/mtnce_order/pay";

    //+++++++++++++++++++++++++ ????????? +++++++++++++++++++++++++//


    //+++++++++++++++++++++++++ ????????? +++++++++++++++++++++++++//

    public static final String USER_CAR_MOMENT = "/api/app/user/car_moment";
    public static final String USER_CAR_MOMENT_COMMENT = "/api/app/user/car_moment_comment";


    //+++++++++++++++++++++++++ ????????? +++++++++++++++++++++++++//

    //????????????
    public static final String APP_RESOURCE_COMPANY = "api/app/resources/company";
    //??????????????????
    public static final String APP_RESOURCE_ENTERPRISE = "api/app/resources/enterprise";

    public static final String APP_RESOURCE_CONFIG = "api/app/resources/app_resources/config";
    //??????banner
    public static final String APP_RESOURCES_INDEX_BANNER = "api/app/resources/app_resources/indexBanner";
    //banner
    public static final String APP_RESOURCES = "api/app/resources/app_resources";
    //??????????????????
    public static final String APP_RESOURCES_INSURANCE = "api/app/resources/app_resources/insurance";
    //??????????????????
    public static final String USER_POINT = "api/app/user/point";
    //??????????????????
    public static final String USER_BALANCE = "api/app/user/balance";
    public static final String USER_COMPANY_ADMIN = "api/app/user/company/%s/admin";
    //????????????
    public static final String USER_COMPANY_MEMBER = "api/app/user/company/%s/member";
    public static final String USER_COMPANY_AUTH = "api/app/user/company/%s/auth";
    public static final String USER_ORDER_DIRVER = "api/app/user/order-dirver";
    public static final String USER_APPLYING_COMPANY = "api/app/user/applying-company";

    //??????
    //??????????????????
    public static final String CAR_LOAN_PARTNER="api/app/car_loan_partner";
    public static final String CAR_LOAN_ORDER="api/app/car_loan_order";

    //????????????
    public static final String APP_VERSION = "api/app/version";
    //????????????
    public static final String APP_PLATFORM_GUARANTEE = "api/app/platform_guarantee";
    public static final String APP_PLATFORM_GUARANTEE_ID = "api/app/platform_guarantee/";
    public static final String APP_PLATFORM_GUARANTEE_CONFIRM = "api/app/platform_guarantee/%s/confirm";
    public static final String APP_PLATFORM_GUARANTEE_CANCEL = "api/app/platform_guarantee/%s/cancel";
    public static final String APP_PLATFORM_GUARANTEE_PAY = "api/app/platform_guarantee/pay";


    // ????????? start
    public static final String CHILD_ACCOUNT = "api/app/user/child-account";
    public static final String CHILD_ACCOUNT_INFO = "api/app/user-child-account/info";
    public static final String CHILD_ACCOUNT_TRANSPORTORDER = "api/app/user-child-account/transportOrder";
    public static final String CHILD_ACCOUNT_TRANSPORT_ORDER = "api/app/user-child-account/transport_order";
    public static final String CHILD_ACCOUNT_PASSWORD = "api/app/user-child-account/password";
    public static final String CHILD_ACCOUNT_TRANSPORT_ORDER_ID = "api/app/user-child-account/transport_order/";
    public static final String CHILD_ACCOUNT_NEXT_STATUS = "api/app/user-child-account/next_status";
    public static final String CHILD_ACCOUNT_POSITION = "api/app/user-child-account/position";
    public static final String CHILD_ACCOUNT_TRANSPORT_ORDER_PRICE = "api/app/user-child-account/transportOrder_price";
    // ????????? end

    /////////////// H5 ??????
    public static final String H5_SHARE_4S = API_BASE_URL + "/Directory/info/%s/%s";

    public static final String H5_SHARE_CAR = API_BASE_URL + "/CarResource/info/%s/%s";

    public static final String H5_SHARE_CAR_2 = API_BASE_URL + "/CarResource/info/%s/%s?price=%s";

    public static final String H5_SHARE_COMPANY = API_BASE_URL+ "/company/?userCompanyId=%s&sharerId=%s";


    public static final String FILE_PATH = Environment.getExternalStorageDirectory() + "/carWorld/";

}
