# TaoToDo 项目介绍
## APP目录
app目录主要用于展示APP的UI界面以及界面资源文件
* java目录
    * com.km.tao
        * adapter 适配器
        * ui      界面
        * utils   工具类
        * view    自定义控件
* res目录
    * anim     动画
    * drawable 图片资源
    * layout   界面布局
    * values   字符串、颜色、样式
    * AndroidManifest.xml 项目配置
* build.gradle 依赖配置
```
    buildFeatures {
        viewBinding = true
    }
    implementation(project(":pickerview"))
    implementation(project(":kernel"))
```  
## db目录
db是使用room数据库进行数据存储module
* java目录
    * com.km.db
        * bean 数据实体
        * dao 数据访问对象
        * DataBaseManage 数据库管理类
        * CustomDisposable 数据线程安全
* build.gradle 依赖配置
```
    def room_version = "2.6.1"
    api "androidx.room:room-runtime:$room_version"
    annotationProcessor "androidx.room:room-compiler:$room_version"
    // optional - RxJava3 support for Room
    api "androidx.room:room-rxjava3:$room_version"
    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation "androidx.room:room-guava:$room_version"
    // optional - Test helpers
    testImplementation "androidx.room:room-testing:$room_version"
    // optional - Paging 3 Integration
    implementation "androidx.room:room-paging:$room_version"
```

## kernel目录
kernel是进行数据操作与UI交互的中间层，主要作用是对db进行增删改查，同时将获取的数据传递给UI展示的module
* java目录
    * com.km.kernel
        * base 基类
        * repository 提供数据操作方法
        * viewmodel 进行数据处理，并传递给UI
        * viewmodelfactory 提供viewmodel工厂
        * kernelApplication.java APP启动核心配置
        * AndroidManifest.xml 项目配置
* build.gradle 依赖配置
```
    api project(':db')
    api 'androidx.lifecycle:lifecycle-livedata-ktx:2.4.1'
    api 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1'
```
## pickerview目录
pickerview是一个第三方自定义控件module，主要用于时间选择器
[项目地址](https://github.com/Bigkoo/Android-PickerView.git)

## demo目录
demo是一个测试module

## pocketsphinx目录
pocketsphinx是一个语音识别module，pocketsphinx是一个开源的语音识别引擎，可以用于语音识别，语音合成（待研究）等功能
参考地址：[pocketsphinx 1 ](https://github.com/crystalyf/PocketSphinx)
        [pocketsphinx 2 ](https://github.com/cmusphinx/pocketsphinx-android-demo)
        [pocketsphinx 3 ](https://github.com/cmusphinx/pocketsphinx-android)
        [pocketsphinx 4 ](https://github.com/eson-yunfei/PocketSphinxDemo)



# 总结
项目采用的是组件化开发，将APP的主要模块进行拆分，分别放在不同的module中，通过kernel进行组合。kernel中进行数据操作，采用的是MVVM架构模式，通过viewmodel处理数据，并将数据传递给UI展示。数据同步UI使用的是LiveData,UI绑定使用的是viewBinding