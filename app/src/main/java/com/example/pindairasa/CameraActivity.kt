package com.example.pindairasa

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pindairasa.databinding.FragmentCameraBinding
import com.example.pindairasa.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class CameraActivity:Fragment(R.layout.fragment_camera) {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    lateinit var bitmap: Bitmap



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCameraBinding.inflate(inflater,container, false)
        val view = binding.root

        binding.gallery.setOnClickListener {
            var intent: Intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, 100)
        }

        var labels = requireContext().assets.open("labels.txt").bufferedReader().readLines()



        var imageProcessor = ImageProcessor.Builder()
            .add(NormalizeOp(0.0f,255.0f))
            .add(ResizeOp(300,300,ResizeOp.ResizeMethod.BILINEAR))
            .build()

        binding.camera.setOnClickListener{
            var tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(bitmap)
            tensorImage = imageProcessor.process(tensorImage)
            val model = Model.newInstance(requireContext())

            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 300, 300, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(tensorImage.buffer)

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            val confidences: FloatArray = outputFeature0.floatArray

            for (i in confidences.indices) {
                println("Confidence for class $i: ${confidences[i]}")
            }
            var maxPos = 0
            var maxConfidence = 0.0f

            for (i in confidences.indices) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }
            binding.bahan.setText(labels[maxPos])

            model.close()
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100){
            var uri = data?.data
            bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver,uri)
            binding.image1.setImageBitmap(bitmap)

            }
        }
    }
