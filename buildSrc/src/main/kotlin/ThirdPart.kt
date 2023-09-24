/**
 * @author phz
 * @desciption 第三方依赖包
 */
object ThirdPart {
    /**网络请求**/
    object Retrofit {//网路请求库retrofit
    private const val RETROFIT_VERSION = "2.9.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$RETROFIT_VERSION"

        //gson转换器
        const val convertGson = "com.squareup.retrofit2:converter-gson:$RETROFIT_VERSION"
        //scalars转换器
        const val convertScalars = "com.squareup.retrofit2:converter-scalars:$RETROFIT_VERSION"

        const val serializationConverter = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"

    }

    object OkHttp {//okhttp
    private const val version = "4.10.0"
        const val okhttp = "com.squareup.okhttp3:okhttp:$version"
        const val urlConnection = "com.squareup.okhttp3:okhttp-urlconnection:$version"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    object Alibaba {
        const val fastjson = "com.alibaba:fastjson:1.2.83"
    }



    /**图片**/
    //图片加载框架
    object Glide {
        private const val version = "4.11.0"
        const val glide = "com.github.bumptech.glide:glide:$version"
        const val compiler = "com.github.bumptech.glide:compiler:$version"
    }
    object Coil{
        //线圈（Kotlin 协程支持的 Android 图像加载库）
        const val kt="io.coil-kt:coil:2.4.0"
        const val compose="io.coil-kt:coil-compose:2.4.0"
    }


    //lottie用来加载json动画
    // 你可以使用转换工具将mp4转换成json【https://isotropic.co/video-to-lottie/】
    // 去这里下载素材【https://lottiefiles.com/】
    const val lottie ="com.airbnb.android:lottie:4.1.0"

    /*******************************窗口、控件和相关工具***********************************/

    /*******************************依赖注入***********************************/
    object DI {
        object Koin {
            private const val koin_version = "2.1.5"

            const val core = "org.koin:koin-core:$koin_version}"
            const val android = "org.koin:koin-android:$koin_version"
            const val androidxViewModel = "org.koin:koin-androidx-viewmodel:$koin_version"
            const val androidScope = "org.koin:koin-android-scope:$$koin_version"
        }

        object Dagger{
            //tip:可搭配Hilt使用
            private const val version = "2.37"
            const val dagger ="com.google.dagger:dagger:$version"
            //use annotationProcessor ,not implementation
            const val compiler ="com.google.dagger:dagger-compiler::$version"
        }
    }
    /*******************************依赖注入***********************************/

    //常用运行时权限请求管理库
    const val rxPermission="com.github.tbruyelle:rxpermissions:0.12"

    object PermissionDispatcher{
        private const val version= "4.8.0"
        const val permissionsDispatcher="com.github.permissions-dispatcher:permissionsdispatcher:$version"
        //use kapt，not api or implementation
        const val processor="com.github.permissions-dispatcher:permissionsdispatcher-processor:$version"
    }

    const val xxPermission="com.github.getActivity:XXPermissions:12.5"

    //常用的工具类
    const val utilCodex = "com.blankj:utilcodex:1.30.0"

    //微信开源项目，替代SP
    const val mmkv = "com.tencent:mmkv:1.0.22"

    //rxjava2配合RxAndroid
    const val rxjava2 = "io.reactivex.rxjava2:rxjava:2.2.21"
    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:2.1.1"
    //rxjava3配合RxAndroid
    /*const val rxjava3 = "io.reactivex.rxjava3:rxjava:3.0.13"
    const val rxAndroid3 = "io.reactivex.rxjava3:rxandroid:3.0.0"*/

    //腾讯bug上报收集
    const val bugly ="com.tencent.bugly:crashreport_upgrade:latest.release"

    //内存泄露检测库
    //use debugImplementation
    const val leakcanary="com.squareup.leakcanary:leakcanary-android:2.7"

    //BLE蓝牙操作工具
    const val rxAndroidBle ="com.polidea.rxandroidble2:rxandroidble:1.12.1"
    //Replaying share
    const val rxjavaReplayingShare ="com.jakewharton.rx2:replaying-share:2.2.0"

    //ARouter
    const val aRouter ="com.alibaba:arouter-api:1.5.2"
    const val aRouterCompiler ="com.alibaba:arouter-compiler:1.5.2"

    //Android-Iconics
    const val iconCore="com.mikepenz:iconics-core:5.3.1"
}