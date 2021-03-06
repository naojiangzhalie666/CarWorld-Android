package com.liansheng.carworld.utils;

import java.util.ArrayList;
import java.util.List;

public class CityUtil {

    private static String[] citys = {
            "安徽省", "合肥市", "芜湖市", "蚌埠市", "淮南市", "马鞍山市", "淮北市", "铜陵市", "安庆市", "黄山市", "阜阳市", "宿州市", "滁州市", "六安市", "宣城市", "池州市", "亳州市",
            "北京市", "北京市",
            "重庆市", "重庆市",
            "福建省", "福州市", "厦门市", "莆田市", "三明市", "泉州市", "漳州市", "南平市", "龙岩市", "宁德市",
            "甘肃省", "兰州市", "嘉峪关市", "金昌市", "白银市", "天水市", "武威市", "张掖市", "平凉市", "酒泉市", "庆阳市", "定西市", "陇南市",
            "广东省", "广州市", "韶关市", "深圳市", "珠海市", "汕头市", "佛山市", "江门市", "湛江市", "茂名市", "肇庆市", "惠州市", "梅州市", "汕尾市", "河源市", "阳江市", "清远市", "东莞市", "中山市", "潮州市", "揭阳市", "云浮市",
            "贵州省", "贵阳市", "六盘水市", "遵义市", "安顺市", "毕节市", "铜仁市",
            "广西", "南宁市", "柳州市", "桂林市", "梧州市", "北海市", "防城港市", "钦州市", "贵港市", "玉林市", "百色市", "贺州市", "河池市", "来宾市", "崇左市",
            "河北省", "石家庄市", "唐山市", "秦皇岛市", "邯郸市", "邢台市", "保定市", "张家口市", "承德市", "沧州市", "廊坊市", "衡水市",
            "黑龙江省", "哈尔滨市", "齐齐哈尔市", "鸡西市", "鹤岗市", "双鸭山市", "大庆市", "伊春市", "佳木斯市", "七台河市", "牡丹江市", "黑河市", "绥化市",
            "河南省", "郑州市", "开封市", "洛阳市", "平顶山市", "安阳市", "鹤壁市", "新乡市", "焦作市", "濮阳市", "许昌市", "漯河市", "三门峡市", "南阳市", "商丘市", "信阳市", "周口市", "驻马店市",
            "湖北省", "武汉市", "黄石市", "十堰市", "宜昌市", "襄阳市", "鄂州市", "荆门市", "孝感市", "荆州市", "黄冈市", "咸宁市", "随州市",
            "湖南省", "长沙市", "株洲市", "湘潭市", "衡阳市", "邵阳市", "岳阳市", "常德市", "张家界市", "益阳市", "郴州市", "永州市", "怀化市", "娄底市",
            "海南省", "海口市", "三亚市", "三沙市", "儋州市",
            "吉林省", "长春市", "吉林市", "四平市", "辽源市", "通化市", "白山市", "松原市", "白城市",
            "江西省", "南昌市", "景德镇市", "萍乡市", "九江市", "抚州市", "鹰潭市", "赣州市", "吉安市", "宜春市", "新余市", "上饶市",
            "江苏省", "南京市", "无锡市", "徐州市", "常州市", "苏州市", "南通市", "连云港市", "淮安市", "盐城市", "扬州市", "镇江市", "泰州市", "宿迁市",
            "辽宁省", "沈阳市", "大连市", "鞍山市", "抚顺市", "本溪市", "丹东市", "锦州市", "营口市", "阜新市", "辽阳市", "盘锦市", "铁岭市", "朝阳市", "葫芦岛市",
            "内蒙古", "呼和浩特市", "包头市", "乌海市", "赤峰市", "通辽市", "鄂尔多斯市", "呼伦贝尔市", "巴彦淖尔市", "乌兰察布市",
            "宁夏", "银川市", "石嘴山市", "吴忠市", "固原市", "中卫市",
            "青海省", "西宁市", "海东市",
            "上海市", "上海市",
            "山西省", "太原市", "大同市", "阳泉市", "长治市", "晋城市", "朔州市", "晋中市", "运城市", "忻州市", "临汾市", "吕梁市",
            "陕西省", "西安市", "铜川市", "宝鸡市", "咸阳市", "渭南市", "延安市", "汉中市", "榆林市", "安康市", "商洛市",
            "山东省", "济南市", "青岛市", "淄博市", "枣庄市", "东营市", "烟台市", "潍坊市", "济宁市", "泰安市", "威海市", "日照市", "临沂市", "德州市", "聊城市", "滨州市", "菏泽市",
            "四川省", "成都市", "自贡市", "攀枝花市", "泸州市", "德阳市", "绵阳市", "广元市", "遂宁市", "内江市", "乐山市", "南充市", "眉山市", "宜宾市", "广安市", "达州市", "雅安市", "巴中市", "资阳市",
            "天津市", "天津市",
            "新疆", "乌鲁木齐市", "克拉玛依市", "吐鲁番市", "哈密市",
            "西藏", "拉萨市", "日喀则市", "昌都市", "林芝市", "山南市", "那曲市",
            "云南省", "昆明市", "曲靖市", "玉溪市", "保山市", "昭通市", "丽江市", "普洱市", "临沧市",
            "浙江省", "杭州市", "宁波市", "温州市", "嘉兴市", "湖州市", "绍兴市", "金华市", "衢州市", "舟山市", "台州市", "丽水市"
    };

    public static List<CustomGroupedItem> getGroupItems() {
        List<CustomGroupedItem> items = new ArrayList<>();
        for (int i = 0; i < citys.length; i++) {
            if (i <= 16) {
                setCity(0, i, items);
            } else if (i <= 18) {
                setCity(17, i, items);
            } else if (i <= 20) {
                setCity(19, i, items);
            } else if (i <= 30) {
                setCity(21, i, items);
            } else if (i <= 43) {
                setCity(31, i, items);
            } else if (i <= 65) {
                setCity(44, i, items);
            } else if (i <= 72) {
                setCity(66, i, items);
            } else if (i <= 87) {
                setCity(73, i, items);
            } else if (i <= 99) {
                setCity(88, i, items);
            } else if (i <= 112) {
                setCity(100, i, items);
            } else if (i <= 130) {
                setCity(113, i, items);
            } else if (i <= 143) {
                setCity(131, i, items);
            } else if (i <= 157) {
                setCity(144, i, items);
            } else if (i <= 162) {
                setCity(158, i, items);
            } else if (i <= 171) {
                setCity(163, i, items);
            } else if (i <= 183) {
                setCity(172, i, items);
            } else if (i <= 197) {
                setCity(184, i, items);
            } else if (i <= 212) {
                setCity(198, i, items);
            } else if (i <= 222) {
                setCity(213, i, items);
            } else if (i <= 228) {
                setCity(223, i, items);
            } else if (i <= 231) {
                setCity(229, i, items);
            } else if (i <= 233) {
                setCity(232, i, items);
            } else if (i <= 245) {
                setCity(234, i, items);
            } else if (i <= 256) {
                setCity(246, i, items);
            } else if (i <= 273) {
                setCity(257, i, items);
            } else if (i <= 292) {
                setCity(274, i, items);
            } else if (i <= 294) {
                setCity(293, i, items);
            } else if (i <= 299) {
                setCity(295, i, items);
            } else if (i <= 306) {
                setCity(300, i, items);
            } else if (i <= 315) {
                setCity(307, i, items);
            } else if (i <= 328) {
                setCity(316, i, items);
            }
        }
        return items;
    }

    public static void setCity(int index, int pos, List<CustomGroupedItem> items) {
        if (pos == index) {
            items.add(new CustomGroupedItem(true, citys[index]));
        } else {
            items.add(new CustomGroupedItem(new CustomGroupedItem.ItemInfo(citys[pos], citys[index], "")));
        }
    }
}
