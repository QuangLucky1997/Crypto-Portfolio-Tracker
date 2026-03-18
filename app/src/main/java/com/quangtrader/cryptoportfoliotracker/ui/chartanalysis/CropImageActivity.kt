package com.quangtrader.cryptoportfoliotracker.ui.chartanalysis

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import com.canhub.cropper.CropImageView
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.common.utils.Constants
import com.quangtrader.cryptoportfoliotracker.common.utils.clicks
import com.quangtrader.cryptoportfoliotracker.databinding.ActivityCropImageBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

@AndroidEntryPoint
class CropImageActivity :
    BaseActivity<ActivityCropImageBinding>(ActivityCropImageBinding::inflate) {

    private var sourceUri: Uri? = null

    override fun onCreateView() {
        super.onCreateView()
        window.apply {
            navigationBarColor = resources.getColor(R.color.white, null)
            statusBarColor = resources.getColor(R.color.white, null)
        }
        sourceUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constants.EXTRA_DATA_CROP, Uri::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(Constants.EXTRA_DATA_CROP)
        }

        if (sourceUri != null) {
            binding.cropImageView.apply {
                guidelines = CropImageView.Guidelines.ON
                setAspectRatio(1, 1)
                setImageUriAsync(sourceUri)
            }
        } else {
            MotionToast.createToast(this,
                "Failed ☹️",
                "No image found!",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this,R.font.inter_bold))
            finish()
        }
        binding.imgConfirm.clicks {
            cropAndSave()
        }
    }

    private fun cropAndSave() {
        binding.cropImageView.setOnCropImageCompleteListener { _, result ->
            if (result.isSuccessful) {
                val croppedUri = result.uriContent
                handleCroppedImage(croppedUri)
            } else {
                MotionToast.createToast(this,
                    "Failed ☹️",
                    "Cropping failed!",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this,R.font.inter_bold))
            }
        }
        binding.cropImageView.croppedImageAsync()
    }

    private fun handleCroppedImage(uri: Uri?) {
        val intent = Intent(this, ResultAnalysisChartActivity::class.java).apply {
            putExtra(Constants.EXTRA_URI, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(intent)
        finish()
    }
}