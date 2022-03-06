package com.wizeline.bookchallenge.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import java.io.File

object DeviceUtil {

    @SuppressLint("HardwareIds")
    fun isEmulator(context: Context):Boolean{

       val  androidId = Settings.Secure.getString(context.contentResolver, "android_id")
        return Build.PRODUCT.contains("sdk") || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu") || androidId == null
    }

    fun isRooted(context: Context):Boolean{

        val isEmulator = isEmulator(context)
        val buildTags = Build.TAGS

        return if (!isEmulator && buildTags != null && buildTags.contains("test-keys")) {
            true
        }else{
            var file = File("/system/app/Superuser.apk")

            if (file.exists()) {
                true
            }else{
                file =  File("/system/xbin/su");
                !isEmulator && file.exists();
            }
        }
    }
}