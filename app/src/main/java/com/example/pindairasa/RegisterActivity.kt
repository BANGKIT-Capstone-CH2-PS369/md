package com.example.pindairasa

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.pindairasa.databinding.ActivityRegisterBinding
import com.example.pindairasa.model.RegisterViewModel
class RegisterActivity : AppCompatActivity() {

    private val userviewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()

    }

    private fun setupAction(){
        binding.signupbtn.setOnClickListener{
            showLoading(true)
            val name = binding.username.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val repassword = binding.repassword.text.toString()

            userviewModel.registerUser(name, email, password, repassword) { response ->

                showLoading(false)
                if (response.error == true){
                    Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
                }else{
                    AlertDialog.Builder(this).apply {
                        setTitle("Yeah!")
                        setMessage("Berhasil")
                        setPositiveButton("Lanjut") { _, _ ->
                            finish()
                        }
                        create()
                        show()
                    }
                }
            }
        }
    }


    private fun showLoading(state: Boolean) { binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE }



}