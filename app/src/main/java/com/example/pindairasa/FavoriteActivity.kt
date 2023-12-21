package com.example.pindairasa

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pindairasa.databinding.FragmentFavoriteBinding

class FavoriteActivity:Fragment(R.layout.fragment_favorite) {

    private var _binding:FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater,container, false)
        val view = binding.root
        setupAction()
        return view
    }

    private fun setupAction(){
        binding.topAppBar.setOnMenuItemClickListener{menuItem ->
            when (menuItem.itemId) {
                R.id.menu -> {
                    val intent = Intent(requireContext(), SettingActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}