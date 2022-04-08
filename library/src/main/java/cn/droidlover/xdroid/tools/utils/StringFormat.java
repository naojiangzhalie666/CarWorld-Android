package cn.droidlover.xdroid.tools.utils;

import java.sql.Array;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.text.TextUtils.isEmpty;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/8/1 10:47
 * <p>
 * Description: 字符串格式化
 */
public class StringFormat {
    /**
     * 保留小数点后两位
     */
    public static String twoDecimalFormat(Object args) {
        if (null == args) {
            return "0.00";
        }
        DecimalFormat df = new DecimalFormat("#,###,##0.00");
        return df.format(ConverterUtil.getDouble(args.toString()));
    }

    //格式化金钱
    public static String montyFormat(Object args)
    {
        if (null == args) {
            return "0.00";
        }
        DecimalFormat df = new DecimalFormat("######0.00");
        return subZeroAndDot(df.format(ConverterUtil.getDouble(args.toString())));
    }



    /**
     * 对字符加星号处理：除前面几位和后面几位外，其他的字符以星号代替
     *
     * @param content
     *            传入的字符串
     * @param frontNum
     *            保留前面字符的位数
     * @param endNum
     *            保留后面字符的位数
     * @return 带星号的字符串
     */
    public static String replaceString2Star(String content, int frontNum, int endNum) {
        if (content == null || content.trim().isEmpty())
            return content;

        int len = content.length();

        if (frontNum >= len || frontNum < 0 || endNum >= len || endNum < 0)
            return content;

        if (frontNum + endNum >= len)
            return content;
        int beginIndex = frontNum;
        int endIndex = len - endNum;
        char[] cardChar = content.toCharArray();

        for (int j = beginIndex; j < endIndex; j++) {
            cardChar[j] = '*';
        }
        return new String(cardChar);
    }

    /**
     * 银行卡格式化(每四位一个空格)
     *
     * @param args 银行卡号
     */
    public static String bankcardFormat(String args) {
        if (isEmpty(args)) {
            return args;
        }
        return args.replaceAll("([\\d]{4})(?=\\d)", "$1 ");
    }

    /**
     * 获得隐私保护银行卡号
     */
    public static String bankcardHideFormat(String args) {
        if (isEmpty(args)) {
            return args;
        }
        return args.replaceAll("^(.{4})(.*)(.{4})$", "$1 **** **** $3");
    }

    /**
     * 手机号格式化
     *
     * @param args 手机号
     */
    public static String phoneFormat(String args) {
        if (isEmpty(args)) {
            return args;
        }
        return args.replaceAll("(\\d{3})(\\d{4})(\\d{4})", "$1 $2 $3");
    }

    /**
     * 获得隐私保护手机号
     */
    public static String phoneHideFormat(String args) {
        if (isEmpty(args)) {
            return args;
        }
        return args.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 获得隐私保护手机号
     */
    public static String IDHide(String args) {
        if (isEmpty(args)) {
            return args;
        }
        return args.replaceAll("(\\d{10})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 获得隐私保护邮箱号
     */
    public static String emailHideFormat(String args) {
        if (isEmpty(args)) {
            return args;
        }
        String email = args.split("@")[0];
        return email.substring(0, 1) + "***" + email.substring(email.length() - 1) + args.substring(args.indexOf("@"));
    }

    /**
     * 判断是否有特殊字符
     *
     * @param content
     * @return
     */
    public static boolean hasEmoji(String... content) {
        Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]");
        Matcher matcher;
        for (int i = 0; i < content.length; i++) {
            matcher = pattern.matcher(content[i]);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }


    /**
     * 身份证号格式化
     *
     * @param IDCard 身份证号,330424 900202 121
     */
    public static String IDCardFormat(String IDCard) {
        if (isEmpty(IDCard)) {
            return IDCard;
        }
        int length = IDCard.length();
        if (length == 15) {
            return IDCard.replaceAll("(\\d{6})(\\d{6})(\\d{3})", "$1 $2 $3");
        } else if (length == 18) {
            return IDCard.replaceAll("(\\d{6})(\\d{4})(\\d{4})(\\S{4})", "$1 $2 $3 $4");
        } else {
            return IDCard;
        }
    }

    /**
     * 获得隐私保护身份证号
     */
    public static String IDCardHideFormat(String IDCard) {
        if (isEmpty(IDCard)) {
            return IDCard;
        }
        int length = IDCard.length();
        String result;
        if (length == 15) {
            result = IDCard.replaceAll("(\\d{4})\\d{7}(\\d{4})", "$1 *** **** $2");
        } else if (length == 18) {
            result = IDCard.replaceAll("(\\d{4})\\d{10}(\\S{4})", "$1 *** *** **** $2");
        } else {
            result = "";
        }
        return result;
    }

    /**
     * 去掉多余的.与0
     */
    public static String subZeroAndDot(Object args) {
        if (null == args) {
            return "";
        }
        String str = args.toString();
        if (str.indexOf(".") > 0) {
            // 去掉多余的0
            str = str.replaceAll("0+?$", "");
            // 如最后一位是.则去掉
            str = str.replaceAll("[.]+?$", "");
        }
        return str;
    }

    /**
     * 数值格式化 - 12,345.00
     */
    public static String doubleFormat(Object args) {
        if (args != null && !isEmpty(args.toString())) {
            String number = args.toString();
            try {
                DecimalFormat df = new DecimalFormat();
                if (ConverterUtil.getDouble(number) < 1) {
                    df.applyPattern("0.00");
                } else {
                    df.applyPattern("##,###,###,###.00");
                }
                return df.format(ConverterUtil.getDouble(number));
            } catch (Exception e) {
                e.printStackTrace();
                return number;
            }
        } else {
            return "0.00";
        }
    }

    /**
     * 数值格式化 - 12,345.00
     */
    public static String doubleFormat2(Object args) {
        if (args != null && !isEmpty(args.toString())) {
            String number = args.toString();
            try {
                DecimalFormat df = new DecimalFormat();
                if (ConverterUtil.getDouble(number) < 1) {
                    df.applyPattern("0.00");
                } else {
                    df.applyPattern("##,###,###,###.00");
                }
                return df.format(ConverterUtil.getDouble(number));
            } catch (Exception e) {
                e.printStackTrace();
                return number;
            }
        } else {
            return "0";
        }
    }

    /**
     * 金额两位小数格式化 - (type = 1)
     */
    public static String doubleMoney(Object formatArgs) {
        return doubleMoney(formatArgs, 1);
    }

    /**
     * 金额两位小数格式化
     *
     * @param formatArgs 待格式化数据
     * @param type       0 - 不足一万，则单位为“元”，否则单位为“万元”
     *                   1 - 单位均为“元”
     */
    public static String doubleMoney(Object formatArgs, int type) {
        if (formatArgs != null && !isEmpty(formatArgs.toString())) {
            String arg = formatArgs.toString();
            switch (type) {
                case 0:
                    try {
                        double money = Double.valueOf(arg);
                        if (money > 10000) {
                            return doubleFormat(money / 10000) + "万元";
                        } else {
                            return doubleFormat(money) + "元";
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return arg;
                    }

                case 1:
                default:
                    return doubleFormat(formatArgs) + "元";
            }
        } else {
            return "0.00元";
        }
    }

    /**
     * 不足1万，则常规格式化，否则，以“万”为单位格式化
     *
     * @param args 待格式化数据
     */
    public static String doubleFormatForW(Object args) {
        if (null != args && !isEmpty(args.toString())) {
            String number = args.toString();
            try {
                double digit = ConverterUtil.getDouble(number);
                if (digit >= 10000) {
                    return doubleFormat(digit / 10000) + "万";
                } else {
                    return doubleFormat(digit);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return number;
            }
        } else {
            return "0.00";
        }
    }

    /**
     * 不足1万，则常规格式化，否则，以“万”为单位格式化
     *
     * @param args 待格式化数据
     */
    public static String doubleFormatForW2(Object args) {
        if (null != args && !isEmpty(args.toString())) {
            String number = args.toString();
            try {
                double digit = ConverterUtil.getDouble(number);
//                if (digit >= 10000) {
                    return doubleFormat2(digit / 10000) + "万";
//                } else {
//                    return subZeroAndDot(digit);
//                }
            } catch (Exception e) {
                e.printStackTrace();
                return number;
            }
        } else {
            return "0";
        }
    }

    /**
     * 数值格式化 - 12,345
     */
    public static String intFormat(Object args) {
        if (args != null && !isEmpty(args.toString())) {
            String number = args.toString();
            try {
                DecimalFormat df = new DecimalFormat();
                if (ConverterUtil.getDouble(number) < 1) {
                    df.applyPattern("0");
                } else {
                    df.applyPattern("##,###,###,###");
                }
                return df.format(ConverterUtil.getDouble(number));
            } catch (Exception e) {
                e.printStackTrace();
                return number;
            }
        } else {
            return "0";
        }
    }

    /**
     * 不足1万，则常规格式化，否则，以“万”为单位格式化
     *
     * @param args 待格式化数据
     */
    public static String intFormatForW(Object args) {
        if (null != args && !isEmpty(args.toString())) {
            String number = args.toString();
            try {
                double digit = ConverterUtil.getDouble(number);
                if (digit >= 10000) {
                    return intFormat(digit / 10000) + "万";
                } else {
                    return intFormat(digit);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return number;
            }
        } else {
            return "0";
        }
    }


    /**
     * 拼接字符串
     */
    public static String join(Object[] tokens, CharSequence delimiter) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }

    /**
     * 去除字符串中的空格、回车、换行符、制表符
     */
    public static String replaceBlank(String str) {
        if (!isEmpty(str)) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            str = m.replaceAll("");
        }
        return str;
    }

    /**
     * 删除所有的标点符号
     */
    public static String trimPunctuation(String str) {
        return str.replaceAll("[\\pP\\p{Punct}]", "");
    }

    /**
     * 格式化一个float
     *
     * @param format 要格式化成的格式 such as #.00, #.#
     */
    public static String formatFloat(float f, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(f);
    }

    /**
     * 将list 用传入的分隔符组装为String
     */
    public static String listToStringSlipStr(List list, String slipStr) {
        StringBuilder builder = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                builder.append(list.get(i)).append(slipStr);
            }
        }
        if (builder.toString().length() > 0) {
            return builder.toString().substring(0, builder.toString().lastIndexOf(slipStr));
        } else {
            return "";
        }
    }

    /**
     * 全角括号转为半角
     */
    public static String replaceBracketStr(String str) {
        if (!isEmpty(str)) {
            str = str.replaceAll("（", "(");
            str = str.replaceAll("）", ")");
        }
        return str;
    }

    /**
     * 全角字符变半角字符
     */
    public static String full2Half(String str) {
        if (isEmpty(str)) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= 65281 && c < 65373) {
                builder.append((char) (c - 65248));
            } else {
                builder.append(str.charAt(i));
            }
        }
        return builder.toString();
    }

    /**
     * 解析字符串返回map键值对(例：a=1&b=2 => a=1,b=2)
     *
     * @param query   源参数字符串
     * @param split1  键值对之间的分隔符（例：&）
     * @param split2  key与value之间的分隔符（例：=）
     * @param dupLink 重复参数名的参数值之间的连接符，连接后的字符串作为该参数的参数值，可为null
     *                null：不允许重复参数名出现，则靠后的参数值会覆盖掉靠前的参数值。
     * @return map
     */
    public static Map<String, String> parseQuery(String query, char split1, char split2, String dupLink) {
        if (!isEmpty(query) && query.indexOf(split2) > 0) {
            Map<String, String> result = new HashMap<>();

            String name = null;
            String value = null;
            String tempValue;
            for (int i = 0; i < query.length(); i++) {
                char c = query.charAt(i);
                if (c == split2) {
                    value = "";
                } else if (c == split1) {
                    if (!isEmpty(name) && value != null) {
                        if (dupLink != null) {
                            tempValue = result.get(name);
                            if (tempValue != null) {
                                value += dupLink + tempValue;
                            }
                        }
                        result.put(name, value);
                    }
                    name = null;
                    value = null;
                } else if (value != null) {
                    value += c;
                } else {
                    name = (name != null) ? (name + c) : "" + c;
                }
            }

            if (!isEmpty(name) && value != null) {
                if (dupLink != null) {
                    tempValue = result.get(name);
                    if (tempValue != null) {
                        value += dupLink + tempValue;
                    }
                }
                result.put(name, value);
            }
            return result;
        }
        return null;
    }

    public static String stripHtml(String content) {
        // <p>段落替换为换行
        content = content.replaceAll("<p .*?>", "\r\n");
        // <br><br/>替换为换行
        content = content.replaceAll("<br\\s*/?>", "\r\n");
        // 去掉其它的<>之间的东西
        content = content.replaceAll("\\<.*?>", "");
        // 还原HTML
        // content = HTMLDecoder.decode(content);
        return content;
    }

    /**
     * 截取字符串中的数值部分
     */
    public static String substringNumber(String params) {
        Matcher matcher = Pattern.compile("[0-9,.%]+").matcher(params);
        if (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            return params.substring(start, end);
        } else {
            return "0";
        }
    }
}