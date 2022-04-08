package com.liansheng.carworld.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.liansheng.carworld.bean.CityBean;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cn.droidlover.xdroid.tools.log.Logger;
import cn.droidlover.xdroid.tools.utils.ActivityManage;
import cn.droidlover.xdroid.tools.utils.ContextHolder;
import cn.droidlover.xdroid.tools.utils.Util;


public class GetJsonDataUtil {

    private ArrayList<CityBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private boolean isLoaded = false;
    private String province;
    private String city;
    private TextView bank_addr;
    private SelectListener selectListener;

    //判断地址数据是否获取成功
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
//                    if (thread == null) {//如果已创建就不再重新创建子线程了
//                        Logger.e("地址数据开始解析");
//                        thread = new Thread(new Runnable() {
//                            @Override
//                            public void run() {
                                // 写子线程中的操作,解析省市区数据
                                initJsonData();
//                            }
//                        });
//                        thread.start();
//                    }
                    break;

                case MSG_LOAD_SUCCESS:
                    selectListener.success(options1Items,options2Items);
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
                    break;

            }
        }
    };

    public GetJsonDataUtil(SelectListener selectListener) {
        this.selectListener=selectListener;
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String CityData = getJson(ContextHolder.getContext(), "city.json");//获取assets目录下的json文件数据

        ArrayList<CityBean> jsonBean = parseData(CityData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCity_list().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCity_list().get(c);
                CityList.add(CityName);//添加城市

            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);
        }
        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }

    private ArrayList<CityBean> parseData(String result) {//Gson 解析
        ArrayList<CityBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                CityBean entity = gson.fromJson(data.optJSONObject(i).toString(), CityBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    private String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public interface SelectListener{
        void success(ArrayList<CityBean> cityBeans,ArrayList<ArrayList<String>> city);
    }
}