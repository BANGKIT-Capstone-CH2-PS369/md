package com.example.pindairasa

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pindairasa.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val menu = menu()
        val chat = chat()
        val camera = camera()
        val favorite = favorite()
        val profile = profile()

        setCurrentFragment(menu)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> setCurrentFragment(menu)
                R.id.chat -> setCurrentFragment(chat)
                R.id.camera -> setCurrentFragment(camera)
                R.id.favorite -> setCurrentFragment(favorite)
                R.id.profile -> setCurrentFragment(profile)

            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(binding.flFragment.id, fragment)
            commit()
        }
}
