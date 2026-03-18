package com.quangtrader.cryptoportfoliotracker.common.utils

import android.animation.ValueAnimator
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.SystemClock
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.common.utils.scaleclick.PushDownAnim
import com.quangtrader.cryptoportfoliotracker.helper.isNetworkAvailable
import com.quangtrader.cryptoportfoliotracker.inject.App.Companion.app
import com.quangtrader.cryptoportfoliotracker.ui.home.HomeActivity
import com.quangtrader.cryptoportfoliotracker.ui.market.coin.detailCoin.ChartTokenActivity
import com.quangtrader.cryptoportfoliotracker.ui.tutorials.TutorialActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

fun View.clickWithAnimationDebounce(
    debounceTime: Long = 250L,
    scale: Float = 0.95f,
    action: () -> Unit
) {
    PushDownAnim.setPushDownAnimTo(this)
        .setScale(PushDownAnim.MODE_SCALE, scale)
        .clickWithAnimationDebounce(object : View.OnClickListener {
            private var lastClickTime: Long = 0
            override fun onClick(v: View) {
                if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
                else action()
                lastClickTime = SystemClock.elapsedRealtime()
            }
        })
}

fun ImageView.setTint(color: Int? = null) {
    if (color == null) {
        imageTintList = null
        return
    }
    imageTintList = ColorStateList.valueOf(color)
}

fun EditText.showKeyboard() {
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun Context.copyClipboard(text: String) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as android.text.ClipboardManager
        clipboard.text = text

    } else {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(resources.getString(R.string.copy_clip_board), text)
        clipboard.setPrimaryClip(clip)
    }
}

fun Context.pasterFromClipboard(completed: (String) -> Unit, empty: () -> Unit) {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    if (clipboardManager.hasPrimaryClip()) {
        val clipData = clipboardManager.primaryClip

        if (clipData != null && clipData.itemCount > 0) {
            val item: ClipData.Item = clipData.getItemAt(0)

            val textFromClipboard = item.text?.toString()
            if (textFromClipboard != null) {
                completed(textFromClipboard)
            } else {
                empty()
            }
        } else {
            empty()
        }
    } else {
        empty()
    }
}

fun View.clicks(
    debounce: Long = 250,
    withAnim: Boolean = true,
    scale: Float = 0.96F,
    clicks: (View) -> Unit
) {
    if (withAnim) {
        var lastClickTime: Long = 0
        PushDownAnim.setPushDownAnimTo(this)
            .setScale(PushDownAnim.MODE_SCALE, scale)
            .setOnClickListener {
                if (SystemClock.elapsedRealtime() - lastClickTime < debounce) return@setOnClickListener
                else clicks(this)
                lastClickTime = SystemClock.elapsedRealtime()
            }
    } else {
        var lastClickTime: Long = 0
        setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounce) return@setOnClickListener
            else clicks(this)
            lastClickTime = SystemClock.elapsedRealtime()
        }
    }
}

fun View.animateHorizontalShake(
    offset: Float,
    repeatCount: Int = 3,
    dampingRatio: Float? = null,
    duration: Long = 1000L,
    interpolator: Interpolator = AccelerateDecelerateInterpolator()
) {
    val defaultDampingRatio = dampingRatio ?: (1f / (repeatCount + 1))
    val animValues = mutableListOf<Float>()
    repeat(repeatCount) { index ->
        animValues.add(0f)
        animValues.add(-offset * (1 - defaultDampingRatio * index))
        animValues.add(0f)
        animValues.add(offset * (1 - defaultDampingRatio * index))
    }
    animValues.add(0f)
    val anim: ValueAnimator = ValueAnimator.ofFloat(*animValues.toFloatArray())
    anim.addUpdateListener {
        this.translationX = it.animatedValue as Float
    }
    anim.interpolator = interpolator
    anim.duration = duration
    anim.start()
}

fun Double?.format(digits: Int = 2): String {
    if (this == null) return "0.00"
    return "%.${digits}f".format(this)
}


fun View.startActivity(context: Context, activity: Class<*>) {
    val intent = Intent(context, activity)
    context.startActivity(intent)
}


fun getTradingViewChartHtml(symbol: String, interval: String): String {
    return """
        <html>
        <head>
            <style>
                body, html { margin:0; padding:0; width:100%; height:100%; overflow:hidden; background:#000; }
            </style>
        </head>
        <body>
            <div id="tradingview_chart"></div>
            <script type="text/javascript" src="https://s3.tradingview.com/tv.js"></script>
            <script type="text/javascript">
                new TradingView.widget({
                    "width": "100%",
                    "height": "100%",
                    "symbol": "$symbol",
                    "interval": "$interval",       // <- đổi theo input
                    "timezone": "Etc/UTC",
                    "theme": "dark",
                    "style": "1",
                    "locale": "vi_VN",
                    "hide_top_toolbar": true,
                    "hide_side_toolbar": true,
                    "withdateranges": false,
                    "allow_symbol_change": false,
                    "hide_legend": true,
                    "details": false,
                    "hotlist": false,
                    "calendar": false,
                    "studies": [],
                    "container_id": "tradingview_chart",
                });
            </script>
        </body>
        </html>
    """.trimIndent()
}

fun formatNumberWithCommas(number: Long): String {
    return String.format("%,d", number)
}

fun Context.getColorCompat(colorRes: Int): Int {
    //return black as a default color, in case an invalid color ID was passed in
    return tryOrNull { ContextCompat.getColor(this, colorRes) } ?: Color.BLACK
}

fun <T> tryOrNull(logOnError: Boolean = true, body: () -> T?): T? {
    return try {
        body()
    } catch (e: Exception) {
        if (logOnError) {
            Timber.e("Error: $e")
        }
        null
    }
}

fun Activity.transparent(hideNavigationBar: Boolean = true) {
    window.decorView.systemUiVisibility = when {
        hideNavigationBar -> View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        else -> View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = Color.TRANSPARENT
    window.navigationBarColor = Color.TRANSPARENT
}

fun AppCompatActivity.startMain(doAfterTask: () -> Unit = {}) {
    val intent = Intent(this, HomeActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
    doAfterTask()
}

fun AppCompatActivity.startTutorialActivity(doAfterTask: () -> Unit = {}) {
    startActivity(
        Intent(this, TutorialActivity::class.java)
    )
    doAfterTask()
}




fun Fragment.startChartToken(
    isFull: Boolean = true,
    name: String,
    logo: String,
    symbol: String,
    percentChange24h: Double,
    price: Double,
    marketCap: Double,
    doAfterTask: () -> Unit = {}
) {
    val task = {
        val intent = Intent(requireActivity(), ChartTokenActivity::class.java)
        intent.putExtra(Constants.EXTRA_NAME_COIN, name)
        intent.putExtra(Constants.EXTRA_LOGO_COIN, logo)
        intent.putExtra(Constants.EXTRA_SYMBOL_COIN, symbol)
        intent.putExtra(Constants.EXTRA_PERCENT_24_H, percentChange24h)
        intent.putExtra(Constants.EXTRA_PRICE_COIN, price)
        intent.putExtra(Constants.EXTRA_MARKET_CAP, marketCap)
        startActivity(intent)
        doAfterTask()
    }
    when {
        isFull -> showFullIfHad { task() }
        else -> task()
    }
}

private fun AppCompatActivity.showFullIfHad(task: () -> Unit) {
    when {
        !app.prefs.isUpgraded.get() && isNetworkAvailable() && app.isFullScreenChanges && app.fullScreenChanges.isTimeAdAvailable() -> {
            when {
                else -> loadAndShowFullAdmob { task() }
            }
        }
        else -> task()
    }
}


private fun Fragment.showFullIfHad(task: () -> Unit) {
    when {
        !app.prefs.isUpgraded.get() && isNetworkAvailable(requireActivity()) && app.isFullScreenChanges && app.fullScreenChanges.isTimeAdAvailable() -> {
            when {
                else -> loadAndShowFullAdmob { task() }
            }
        }
        else -> task()
    }
}
private fun Fragment.loadAndShowFullAdmob(task: () -> Unit) {
    val activity = activity as? AppCompatActivity ?: return
    val fm = parentFragmentManager

    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
        tryOrNull {
            if (!app.dialogLoadingAds.isAdded) {
                app.dialogLoadingAds.show(
                    fm,
                    "LoadingAdsDialog"
                )
            }
        }

        delay(app.loadingFullDisplayInterval)

        app.fullScreenChanges.loadAd(activity) { isSuccess ->
            when {
                isSuccess -> {
                    app.fullScreenChanges.loadOrShowIfAvailable(
                        activity,
                        isLoadAgain = false
                    ) {
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                            delay(app.delayFullDisplayInterval)
                            tryOrNull { app.dialogLoadingAds.dismiss() }
                            tryOrNull { task() }
                        }
                    }
                }

                else -> {
                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                        delay(app.delayFullDisplayInterval)
                        tryOrNull { app.dialogLoadingAds.dismiss() }
                        tryOrNull { task() }
                    }
                }
            }
        }
    }
}

private fun AppCompatActivity.loadAndShowFullAdmob(task: () -> Unit) {
    lifecycleScope.launch(Dispatchers.Main) {
        tryOrNull { app.dialogLoadingAds.show(this@loadAndShowFullAdmob.supportFragmentManager, "LoadingAdsDialog") }
        delay(app.loadingFullDisplayInterval)
        app.fullScreenChanges.loadAd(this@loadAndShowFullAdmob) { isSuccess ->
            when {
                isSuccess -> {
                    app.fullScreenChanges.loadOrShowIfAvailable(
                        this@loadAndShowFullAdmob,
                        isLoadAgain = false
                    ) {
                        lifecycleScope.launch(Dispatchers.Main) {
                            delay(app.delayFullDisplayInterval)
                            tryOrNull { app.dialogLoadingAds.dismiss() }
                            tryOrNull { task() }
                        }
                    }
                }

                else -> {
                    lifecycleScope.launch(Dispatchers.Main) {
                        delay(app.delayFullDisplayInterval)
                        tryOrNull { app.dialogLoadingAds.dismiss() }
                        tryOrNull { task() }
                    }
                }
            }
        }
    }
}

fun disableKeyboard(view: View) {
    val window = (view.context as? Activity)?.window ?: return
    val controller = WindowInsetsControllerCompat(window, view)
    controller.hide(WindowInsetsCompat.Type.ime())
}


fun Activity.showKeyboardModern(view: View) {
    view.requestFocus()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.insetsController?.show(WindowInsets.Type.ime())
    } else {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}