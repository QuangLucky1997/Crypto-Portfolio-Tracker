package com.quangtrader.cryptoportfoliotracker.ui.chartanalysis

import android.net.Uri
import android.os.Build
import android.widget.Toast
import com.canhub.cropper.CropImageView
import com.quangtrader.cryptoportfoliotracker.common.utils.Constants
import com.quangtrader.cryptoportfoliotracker.common.utils.clicks
import com.quangtrader.cryptoportfoliotracker.databinding.ActivityCropImageBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CropImageActivity :
    BaseActivity<ActivityCropImageBinding>(ActivityCropImageBinding::inflate) {

    private var sourceUri: Uri? = null

    override fun onCreateView() {
        super.onCreateView()
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
            Toast.makeText(this, "Không tìm thấy ảnh!", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, "Crop thất bại: ${result.error}", Toast.LENGTH_SHORT).show()
            }
        }
        binding.cropImageView.croppedImageAsync()
    }

    private fun handleCroppedImage(uri: Uri?) {
        Toast.makeText(this, "Crop thất bại: $uri", Toast.LENGTH_SHORT).show()
//        val returnIntent = Intent().apply { putExtra("CROP_RESULT", uri) }
//        setResult(RESULT_OK, returnIntent)
//        finish()
    }
}