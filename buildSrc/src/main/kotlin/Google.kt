
/**
 * @author phz
 */
object Google {
    //material包
    const val material = "com.google.android.material:material:1.5.0-alpha01"
    //gson解析
    const val gson = "com.google.code.gson:gson:2.8.7"
    //扫码
    const val barcode_scanning="com.google.mlkit:barcode-scanning:17.0.0"


    object Zxing {
        private const val version = "3.5.2"
        const val core = "com.google.zxing:core:$version"
        const val journeyapps = "com.journeyapps:zxing-android-embedded:4.3.0"
    }
}