package authenticator.app.otp.authentication.fa.common.extentions

import android.animation.ValueAnimator
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.SystemClock
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.utils.scaleclick.PushDownAnim

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

