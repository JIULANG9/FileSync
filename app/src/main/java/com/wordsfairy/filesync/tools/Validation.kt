package com.wordsfairy.filesync.tools

/**
 * @Description:
 * @Author: JIULANG
 * @Data: 2023/7/2 17:13
 */

 fun validateEmail(email: String?): Boolean {
    if (email == null || !EMAIL_ADDRESS_REGEX.matches(email)) {
        return false
    }
    // more validations here
    return true
}
private val EMAIL_ADDRESS_REGEX =
    Regex("""[a-zA-Z0-9+._%\-']{1,256}@[a-zA-Z0-9][a-zA-Z0-9\-]{0,64}(\.[a-zA-Z0-9][a-zA-Z0-9\-]{0,25})+""")
