package com.liansheng.carworld.activity.circle;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.baidu.liantian.ac.U;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.JsonObject;
import com.liansheng.carworld.R;
import com.liansheng.carworld.activity.BaseActivity;
import com.liansheng.carworld.bean.CityBean;
import com.liansheng.carworld.bean.event.RefreshEvent;
import com.liansheng.carworld.net.JsonCallback;
import com.liansheng.carworld.net.NetApi;
import com.liansheng.carworld.net.UrlKit;
import com.liansheng.carworld.utils.GetJsonDataUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.tools.utils.Util;

public class BuyAct extends BaseActivity {
    @BindView(R.id.input_content)
    EditText inputContent;
    @BindView(R.id.input_content_size)
    TextView inputContentSize;
    @BindView(R.id.tv_city)
    TextView tv_city;
    private OptionsPickerView pvOptions;

    @Override
    public void initData(Bundle savedInstanceState) {
        new GetJsonDataUtil(new GetJsonDataUtil.SelectListener() {
            @Override
            public void success(ArrayList<CityBean> cityBeans, ArrayList<ArrayList<String>> city) {
                pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        String city = cityBeans.get(options1).getCity_list().get(options2);
                        tv_city.setText(city);
                    }
                })
                        .setDividerColor(ContextCompat.getColor(context, R.color.line_grey))
                        .setTextColorCenter(ContextCompat.getColor(context, R.color.main_normal_color))
                        .setContentTextSize(16)
                        .build();
                pvOptions.setPicker(cityBeans, city);//二级选择器（市区）
                pvOptions.setSelectOptions(10, 5);
            }
        });
        inputContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(editable)){
                    inputContentSize.setText(editable.length()+"/100");
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_buy;
    }

    @OnClick({R.id.btn_go,R.id.tv_city})
    public void onViewClicker(View view){
        switch (view.getId()){
            case R.id.tv_city:
                if (pvOptions != null) {
                    pvOptions.show();
                }
                break;
            case R.id.btn_go:
                String content = inputContent.getText().toString();
                String city = tv_city.getText().toString();
                if(TextUtils.isEmpty(content)){
                    Util.toast("请输入求购信息");
                    return;
                }
                if(TextUtils.isEmpty(city)){
                    Util.toast("请选择城市");
                    return;
                }
                Map<String,Object> map=new HashMap<>();
                map.put("type","Seek");
                map.put("text",content);
                map.put("city",city);
                JSONObject json = new JSONObject(map);
                NetApi.postJson(UrlKit.USER_CAR_MOMENT, json.toString(), new JsonCallback() {
                    @Override
                    public void onSuccess(String data, int id) {
                        Util.toast("发布成功");
                        EventBus.getDefault().post(new RefreshEvent());
                        finish();
                    }
                });
                break;
        }
    }
}
