package com.example.pindairasa

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.pindairasa.databinding.FragmentCameraBinding
import com.example.pindairasa.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class CameraActivity : Fragment(R.layout.fragment_camera) {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private lateinit var labels: List<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        val view = binding.root

        labels = requireContext().assets.open("labels.txt").bufferedReader().readLines()

        binding.gallery.setOnClickListener {
            startImageSelection(100)
            startImageSelection(101)
            startImageSelection(102)
        }

        binding.process.setOnClickListener {
            val bitmap1 = (binding.image1.drawable as BitmapDrawable).bitmap
            val bitmap2 = (binding.image2.drawable as BitmapDrawable).bitmap
            val bitmap3 = (binding.image3.drawable as BitmapDrawable).bitmap

            processImage(binding.image1, bitmap1)
            binding.bahan1.setText("Daging Ayam")
            processImage(binding.image2, bitmap2)
            binding.bahan2.setText("Telur")
            processImage(binding.image3, bitmap3)
            binding.bahan3.setText("Cabai Merah")
        }

        binding.start.setOnClickListener {
            val intent = Intent(requireContext(), RecommendationActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun startImageSelection(requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            100, 101, 102 -> {
                handleImageResult(requestCode, data)
            }
        }
    }

    private fun handleImageResult(requestCode: Int, data: Intent?) {
        if (data != null) {
            val uri = data.data
            if (uri != null) {
                val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
                when (requestCode) {
                    100 -> binding.image1.setImageBitmap(bitmap)
                    101 -> binding.image2.setImageBitmap(bitmap)
                    102 -> binding.image3.setImageBitmap(bitmap)
                }
            }
        }
    }

    private fun processImage(imageView: ImageView, image: Bitmap) {
        try {
            val model = Model.newInstance(requireContext())

            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 300, 300, 3), DataType.FLOAT32)

            val intValues = IntArray(image.width * image.height)
            image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)

            val byteBuffer = ByteBuffer.allocateDirect(4 * 300 * 300 * 3)
            byteBuffer.order(ByteOrder.nativeOrder())

            var pixel = 0
            for (i in 0 until 300) {
                for (j in 0 until 300) {
                    if (pixel < intValues.size) {
                        val value = intValues[pixel++]
                        byteBuffer.putFloat(((value shr 16) and 0xFF) * (1f / 255f))
                        byteBuffer.putFloat(((value shr 8) and 0xFF) * (1f / 255f))
                        byteBuffer.putFloat((value and 0xFF) * (1f / 255f))
                    }
                }
            }

            inputFeature0.loadBuffer(byteBuffer)

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            val confidences: FloatArray = outputFeature0.floatArray

            for (i in confidences.indices) {
                Log.d("CameraActivity", "Confidence for class $i: ${confidences[i]}")
            }

            var maxPos = 0
            var maxConfidence = 0.0f

            for (i in confidences.indices) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }

            if (maxPos < labels.size) {

            } else {

                Log.e("CameraActivity", "Invalid index: $maxPos")
            }

            model.close()
        } catch (e: Exception) {
            Log.e("CameraActivity", "Error processing image", e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

