# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# ================================================================================================
# =========================================== 混淆模板 ===========================================
# ================================================================================================

# ---------------------------------------- 输入/输出 选项 ----------------------------------------
# 指定的jar将不被混淆
# -libraryjars libs/fastjson-1.2.4.jar
# 跳过(不混淆) jars中的 非public classes
-dontskipnonpubliclibraryclasses
# 不跳过(混淆) jars中的 非public classes   默认选项
# -dontskipnonpubliclibraryclassmembers

# ------------------------------------------- 优化选项 -------------------------------------------
# 不优化(当使用该选项时，下面的选项均无效)
-dontoptimize
# 默认启用优化,根据 optimization_filter 指定要优化的文件
# -optimizations optimization_filter
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
# 迭代优化的次数默认99次，一般迭代10次左右，代码已经不能再次优化了
-optimizationpasses 5

# ------------------------------------------- 压缩选项 -------------------------------------------
# 不压缩(全局性的,即便使用了-keep 开启shrink，也无效)
-dontshrink

# ------------------------------------------ 预校验选项 ------------------------------------------
# 不预校验
-dontpreverify

# ------------------------------------------- 通用选项 -------------------------------------------
# 打印详细
-verbose
# 不打印某些错误
# -dontnote android.support.v4.**
# 不打印警告信息
# -dontwarn android.support.v4.**
# 忽略警告，继续执行
-ignorewarnings

# ------------------------------------------- 混淆选项 -------------------------------------------
# 不混淆
# -dontobfuscate
# 不使用大小写混合类名
-dontusemixedcaseclassnames
# 指定重新打包,所有包重命名,这个选项会进一步模糊包名,将包里的类混淆成n个再重新打包到一个个的package中
-flattenpackagehierarchy ''
# 将包里的类混淆成n个再重新打包到一个统一的package中  会覆盖 flattenpackagehierarchy 选项
-repackageclasses ''
# 混淆时可能被移除下面这些东西，如果想保留，需要用该选项。对于一般注解处理如 -keepattributes *Annotation*
# attribute_filter : Exceptions, Signature, Deprecated, SourceFile, SourceDir, LineNumberTable,
# LocalVariableTable, LocalVariableTypeTable, Synthetic,
# EnclosingMethod, RuntimeVisibleAnnotations, RuntimeInvisibleAnnotations, RuntimeVisibleParameterAnnotations,
# RuntimeInvisibleParameterAnnotations, and AnnotationDefault.
# -keepattributes *Annotation*

# ---------------------------------------- 保持不变的选项 ----------------------------------------
# 保持class_specification规则；若有[,modifier,...]，则先启用它的规则
# -keep [,modifier,...] class_specification
# 保持类的成员：属性(可以是成员属性、类属性)、方法(可以是成员方法、类方法)
# -keepclassmembers [,modifier,...]class_specification
# 与-keep功能基本一致(经测试)
# -keepclasseswithmembers [,modifier,...] class_specification
# Short for -keep,allowshrinking class_specification
# -keepnames class_specification
# Short for -keepclassmembers,allowshrinking class_specification
# -keepclassmembernames class_specification
# Short for -keepclasseswithmembers,allowshrinking class_specification
# -keepclasseswithmembernames class_specification
# 打印匹配的-keep家族处理的 类和类成员列表，到标准输出。
# -printseeds [filename]

# ************************************************************************************************
# *******************************************  COMMON  *******************************************
# ************************************************************************************************
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
#-keep public class com.google.vending.licensing.ILicensingService
#-keep public class com.android.vending.licensing.ILicensingService

# 所有native的方法不混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 继承自View的构造方法不混淆
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(***);
    public *** get*();
}

# 枚举类不混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# AIDL 文件不能去混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# 保护 谷歌第三方 jar 包，界面特效
-keep class android.support.v4.**
-dontwarn android.support.v4.**
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*

# 保持源文件和行号的信息,用于混淆后定位错误位置
-keepattributes SourceFile,LineNumberTable
# 保持签名
-keepattributes Signature
# 保持任意包名.R类的类成员属性。即保护R文件中的属性名不变
-keepclassmembers class **.R$* {
    public static <fields>;
}

# 保护所有实体中的字段名称
-keepclassmembers class * implements java.io.Serializable {
    <fields>;
    <methods>;
}

# ************************************************************************************************
# *******************************************  CUSTOM  *******************************************
# ************************************************************************************************
#添加自己的混淆规则:
#1. 代码中使用了反射，如一些ORM框架的使用，需要保证类名 方法不变，不然混淆后，就反射不了
#2. 使用GSON、fastjson等JSON解析框架所生成的对象类，生成的bean实体对象，内部大多是通过反射来生成,不能混淆
#3. 引用了第三方开源框架或继承第三方SDK，如开源的okhttp网络访问框架，百度定位SDK等，在这些第三库的文档中 一般会给出"相应的"混淆规则，复制过来即可
#4. 有用到WEBView的JS调用接口，真没用过这块, 不是很熟, 网上那个看到的
#5. 继承了Serializable接口的类，在反序列画的时候，需要正确的类名等，在Android 中大多是实现 Parcelable 来序列化的

-keep class com.liansheng.carworld.bean.**{*;}

#高德 start
#3D 地图 V5.0.0之后：
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.**{*;}
-keep   class com.amap.api.trace.**{*;}

#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.loc.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

#搜索
-keep class com.amap.api.services.**{*;}

#高德 end

-keep class **_FragmentFinder { *; }
-keep class androidx.fragment.app.* { *; }

-keep class com.qmuiteam.qmui.arch.record.RecordIdClassMap { *; }
-keep class com.qmuiteam.qmui.arch.record.RecordIdClassMapImpl { *; }

-keep class com.qmuiteam.qmui.arch.scheme.SchemeMap {*;}
-keep class com.qmuiteam.qmui.arch.scheme.SchemeMapImpl {*;}

#PictureSelector 2.0
-keep class com.luck.picture.lib.** { *; }

#Ucrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

#Okio
-dontwarn org.codehaus.mojo.animal_sniffer.*
#pgyer
-libraryjars libs/pgyer_sdk_3.0.10.jar
-dontwarn com.pgyersdk.**
-keep class com.pgyersdk.** { *; }
-keep class com.pgyersdk.**$* { *; }


-keep class com.tencent.mm.opensdk.** {
    *;
}

-keep class com.tencent.wxop.** {
    *;
}

-keep class com.tencent.mm.sdk.** {
    *;
}

#网易易盾
-dontwarn com.cmic.sso.sdk.**
-dontwarn com.cmic.sso.wy.**
-keep public class com.cmic.sso.sdk.**{*;}
-keep class cn.com.chinatelecom.account.api.**{*;}
-keep class com.netease.nis.quicklogin.entity.**{*;}
-keep class com.netease.nis.quicklogin.listener.**{*;}
-keep class com.netease.nis.quicklogin.QuickLogin{
    public <methods>;
    public <fields>;
}
-keep class com.netease.nis.quicklogin.helper.UnifyUiConfig{*;}
-keep class com.netease.nis.quicklogin.helper.UnifyUiConfig$Builder{
     public <methods>;
     public <fields>;
 }
-keep class com.netease.nis.quicklogin.utils.LoginUiHelper$CustomViewListener{
     public <methods>;
     public <fields>;
}
-dontwarn com.sdk.**
-keep class com.sdk.** { *;}
-dontwarn com.netease.nis.basesdk.HttpUtil

#极光
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

#友盟
-keep class com.umeng.** {*;}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class com.liansheng.carworld.R$*{
public static final int *;
}

#百度人脸识别
-keep class com.baidu.**{*;}
-dontwarn com.baidu.**

-keep class com.baidu.idl.main.facesdk.**{*;}

#-keep class com.baidu.vis.unified.license.{*;}
#-keep class com.baidu.liantian.{*;}
#-keep class com.baidu.baidusec.**{*;}
#-keep class com.baidu.idl.main.facesdk.* {*;}

#OSS混淆
-keep class com.alibaba.sdk.android.oss.** { *; }
-dontwarn okio.**
-dontwarn org.apache.commons.codec.binary.**