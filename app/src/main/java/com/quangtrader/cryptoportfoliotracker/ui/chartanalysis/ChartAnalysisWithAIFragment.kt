package com.quangtrader.cryptoportfoliotracker.ui.chartanalysis

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.common.utils.CameraManager
import com.quangtrader.cryptoportfoliotracker.common.utils.Constants
import com.quangtrader.cryptoportfoliotracker.common.utils.clicks
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentChartAnalysisAiBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.text.SimpleDateFormat
import java.util.Locale
@AndroidEntryPoint
class ChartAnalysisWithAIFragment : BaseFragment<FragmentChartAnalysisAiBinding>() {
    private val viewModel: ChartAnalysisViewModel by viewModels()
    private lateinit var cameraManager: CameraManager
    private var imageCapture: ImageCapture? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) startCamera() else MotionToast.createToast(requireActivity(),
            "Failed ☹️",
            "Camera permission request failed!",
            MotionToastStyle.ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(requireActivity(), R.font.inter_bold))
    }


    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentChartAnalysisAiBinding
        get() = FragmentChartAnalysisAiBinding::inflate



    override fun onViewCreated() {
        cameraManager = CameraManager(requireContext())

        if (hasCameraPermission()) startCamera()
        else requestPermissionLauncher.launch(Manifest.permission.CAMERA)

        setupListeners()
        observeViewModel()
    }

    private fun startCamera() {
        cameraManager.startCamera(viewLifecycleOwner, binding.preview) {
            imageCapture = it
        }
    }

    private fun setupListeners() {
        binding.btnTakePhoto.clicks {
            takePhoto()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.captureState.collect { state ->
                when (state) {
                    is ChartAnalysisViewModel.CaptureState.Success -> {
                        val intent = Intent(requireContext(), CropImageActivity::class.java).also {
                            it.putExtra(Constants.EXTRA_DATA_CROP, state.uri)
                            it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        startActivity(intent)
                    }

                    is ChartAnalysisViewModel.CaptureState.Error -> {
                        Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> {

                    }
                }
            }
        }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val outputOptions = createOutputOptions(requireContext().contentResolver)

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    viewModel.onImageSaved(output.savedUri)
                }

                override fun onError(exc: ImageCaptureException) {
                    viewModel.onCaptureError(exc)
                }
            }
        )
    }

    private fun createOutputOptions(resolver: ContentResolver): ImageCapture.OutputFileOptions {
        val name = SimpleDateFormat(Constants.FORMAT_DATE, Locale.US).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-App")
        }
        return ImageCapture.OutputFileOptions.Builder(
            resolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
        ).build()
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        requireContext(), Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED


}