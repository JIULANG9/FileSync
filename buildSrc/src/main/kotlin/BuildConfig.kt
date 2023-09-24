/**
 * @author jiulang
 * @description 编译配置信息
 */
object BuildConfig {
    const val compileSdk = 34
    const val buildToolsVersion ="33"
    const val minSdkVersion = 26
    const val targetSdkVersion = 34

    const val applicationId ="com.wordsfairy.filesync"
    const val testInstrumentationRunner="androidx.test.runner.AndroidJUnitRunner"
    var versionName = "1.0.0"
    var versionCode = 1
}
object SigningConfigs{
    /** 密钥别名 */
    const val key_alias = "wordsfairy"

    /** 别名密码 */
    const val key_password = "jl999999999"

    /** 密钥文件路径 */
    const val store_file = "../cert/wordsfairy.jks"

    /** 密钥密码 */
    const val store_password = "jl999999999"
}