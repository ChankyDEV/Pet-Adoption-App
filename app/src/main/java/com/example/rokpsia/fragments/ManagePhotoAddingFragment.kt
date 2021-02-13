package com.example.rokpsia.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.rokpsia.R
import com.example.rokpsia.databinding.AddPhotoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ManagePhotoAddingFragment: Fragment() {

    private val permission = Manifest.permission.CAMERA
    private val requestCode = 2
    private lateinit var binding:AddPhotoBinding
    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddPhotoBinding.inflate(inflater,container,false)


        if (allPermissionGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
        }

        outputDirectory = getOutputDirectory(requireContext())

        binding.button.setOnClickListener {
            takePhoto()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cameraExecutor = Executors.newSingleThreadExecutor()

    }

    private fun startCamera() {

        Log.d("1", "startCamera: ")

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.createSurfaceProvider())
            }

            imageCapture = ImageCapture.Builder()
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

        }, ContextCompat.getMainExecutor(requireContext()))
    }


    private fun takePhoto() {

        val imageCapture = imageCapture ?: return

        imageCapture.takePicture(cameraExecutor,object:ImageCapture.OnImageCapturedCallback(){

            override fun onCaptureSuccess(image: ImageProxy) {
                val bitmap = imageProxyToBitmap(image)

                lifecycleScope.launch {
                    withContext(Dispatchers.Main) {
                        findNavController().previousBackStackEntry?.savedStateHandle?.set("image", bitmap)
                        findNavController().popBackStack()
                    }
                }

                Log.d("1", "onCaptureSuccess: ")
                super.onCaptureSuccess(image)
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.d("1", "onCaptureNotSuccess: ")
            }
        })
    }


    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val planeProxy = image.planes[0]
        val buffer: ByteBuffer = planeProxy.buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }


    private fun getOutputDirectory(context: Context): File {
        val appContext = context.applicationContext
        val mediaDir = context.externalCacheDir.let {
            File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir.exists())
            mediaDir else appContext.filesDir
    }


    private fun allPermissionGranted() = ContextCompat.checkSelfPermission(
        requireContext(),
        permission
    ) == PackageManager.PERMISSION_GRANTED


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (this.requestCode == requestCode) {
            if (allPermissionGranted()) {
                startCamera()
            }
            else{
                Toast.makeText(requireContext(),
                    "Permissions not granted",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }


}