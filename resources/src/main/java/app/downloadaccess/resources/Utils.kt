package app.downloadaccess.resources

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics
import com.google.common.base.Preconditions.checkArgument
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.*

object Utils {
    @JvmField
    var PREFS_NAME = "access-app-prefs"
    @JvmField
    var PROFILE_REQ_CODE = 1
    @JvmStatic
    fun getJwtToken(context: Context): String {
        var token1: String? = null
        var token2: String? = null
        try {
            BufferedReader(
                InputStreamReader(
                    context.assets.open("jwt_key.txt"),
                    StandardCharsets.UTF_8
                )
            ).use { reader ->

                // do reading, usually loop until end of file reading
                token1 = reader.readLine()
                token2 = reader.readLine()
            }
        } catch (e: IOException) {
            //log the exception
        }
        //log the exception
        var jsontoken = ""
        if (token1 != null) {
            val jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, token1!!.toByteArray())
                .setPayload("demo")
                .compact()
            jsontoken = "Bearer $jwt"
        }
        if (token2 != null) {
            val jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, token2!!.toByteArray())
                .setPayload("demo")
                .compact()
            jsontoken = "$jsontoken $jwt"
        }
        println(jsontoken)
        return jsontoken
    }

    @JvmStatic
    fun loadLocale(activity: Activity) {
        val prefs = activity.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        var languageKey = prefs.getString("lang", "no")
        if (languageKey == "no") {
            languageKey = "en"
            prefs.edit().putString("lang", languageKey).apply()
        }
        val locale = Locale(languageKey)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        activity.baseContext.resources.updateConfiguration(
            config,
            activity.baseContext.resources.displayMetrics
        )
    }

    @JvmStatic
    fun getDayOfMonthSuffix(n: Int): String? {
        checkArgument(n in 1..31, "illegal day of month: $n")
        return if (n in 11..13) {
            "th"
        } else when (n % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }

    @JvmStatic
    fun showLoadingIndicator(context: Context): ProgressDialog {
        val indicator = ProgressDialog(context)
        indicator.setCancelable(false)
        indicator.setCanceledOnTouchOutside(false)
        indicator.show()

        return indicator
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    @JvmStatic
    fun dpToPx(context: Context, dp: Float): Int {
        return (dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    @JvmStatic
    fun pxToDp(context: Context, px: Float): Int {
        return (px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }

}