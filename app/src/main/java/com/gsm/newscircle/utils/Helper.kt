package com.gsm.newscircle.utils

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsIntent.SHARE_STATE_ON
import androidx.core.net.toUri
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

object Helper {

    fun openNewsOnBrowser(context: Context, url: String) {
        CustomTabsIntent.Builder()
            .setShareState(SHARE_STATE_ON)
            .build()
            .launchUrl(context, url.toUri())
    }

//    fun getSupportedNewsCountries(): List<Country> {
//        val supportedCodes = setOf(
//            "ae", "ar", "at", "au", "be", "bg", "br", "ca", "ch", "cn", "co", "cu", "cz", "de",
//            "eg", "fr", "gb", "gr", "hk", "hu", "id", "ie", "il", "in", "it", "jp", "kr", "lt",
//            "lv", "ma", "mx", "my", "ng", "nl", "no", "nz", "ph", "pl", "pt", "ro", "rs", "ru",
//            "sa", "se", "sg", "si", "sk", "th", "tr", "tw", "ua", "us", "ve", "za"
//        )
//
//        return supportedCodes.map { code ->
//            val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
//                Locale.of("", code)
//            } else {
//                @Suppress("DEPRECATION")
//                Locale("", code)
//            }
//
//            val name = locale.displayCountry
//            // Correct emoji flag logic (Magic number 0x1F1E6)
//            val flag = code.uppercase().map { char ->
//                Character.codePointAt("$char", 0) - 'A'.code + 0x1F1E6
//            }.joinToString("") { String(Character.toChars(it)) }
//
//            Country(name, code, flag)
//        }.sortedBy { it.name }
//    }
//
//    fun getSupportedNewsLanguages(): List<Language> {
//        val supportedCodes = listOf(
//            "ar", "de", "en", "es", "fr", "he", "it",
//            "nl", "no", "pt", "ru", "sv", "ud", "zh"
//        )
//
//        return supportedCodes.map { code ->
//            val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
//                Locale.of(code)
//            } else {
//                @Suppress("DEPRECATION")
//                Locale(code)
//            }
//            // .displayLanguage will return "English", "Spanish", etc.
//            // based on the user's phone settings.
//            val name = locale.displayLanguage.replaceFirstChar { it.uppercase() }
//
//            Language(name, code)
//        }.sortedBy { it.name }
//    }

    /**
     * Maps network exceptions and HTTP status codes to user-friendly error messages.
     */
    fun handleError(e: Throwable): String {
        return when (e) {
            is HttpException -> {
                when (e.code()) {
                    400 -> "Bad Request: The request was unacceptable, often due to a missing parameter."
                    401 -> "Unauthorized: Your API key was missing or incorrect."
                    429 -> "Too Many Requests: You have been rate limited. Please try again later."
                    500 -> "Server Error: Something went wrong on our side."
                    else -> "Something went wrong: ${e.message()}"
                }
            }
            is SocketTimeoutException -> "The server is taking too long to respond. Please try again."
            is IOException -> "IOException: ${e.localizedMessage}"
            else -> e.localizedMessage ?: "An unexpected error occurred."
        }
    }
}