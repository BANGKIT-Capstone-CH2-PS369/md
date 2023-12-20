package com.example.pindairasa

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pindairasa.databinding.ActivityRegisterBinding
import com.example.pindairasa.model.RegisterViewModel


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userviewmodel:RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    private fun setupAction(){
        binding.signupbtn.setOnClickListener{
            showLoading(true)
            val name = binding.username.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            userviewmodel.registerUser(name, email, password) { response ->

                showLoading(false)
                if (response.error == true){
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                }else{
                    AlertDialog.Builder(this).apply {
                        setTitle("Yeah!")
                        setMessage(response.message)
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

    private fun obtainViewModel(activity: AppCompatActivity): RegisterViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[RegisterViewModel::class.java]
    }


}