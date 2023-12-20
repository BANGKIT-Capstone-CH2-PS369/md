package com.example.pindairasa

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.pindairasa.databinding.ActivityLoginBinding
import com.example.pindairasa.model.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.loginbtn.setOnClickListener{
            showLoading(true)
            val email = binding.username.text.toString()
            val password = binding.password.text.toString()

            viewModel.loginUser(email, password) { response ->
                showLoading(false)

                if (response.error == true) {
                    Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }

        binding.createacc.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(state: Boolean) { binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE }
}