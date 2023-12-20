package com.example.pindairasa.di

import android.content.Context
import com.example.pindairasa.API.ApiConfig
import com.example.pindairasa.pref.UserPreference
import com.example.pindairasa.pref.dataStore
import com.example.pindairasa.repository.repo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): repo {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return repo.getInstance(apiService, pref)
    }
}