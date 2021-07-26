package com.jlpay.appdelegate.util

class Logger {
    private static final String LOG_TAG = "Jlpay::delegate >>> "

    static void i(String info) {
        if (null != info) {
            println(LOG_TAG + info)
        }
    }

}
