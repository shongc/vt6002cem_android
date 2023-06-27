package com.hong.vt6002cem_227019175_classwork2.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Today's Feng Shui Tips"
    }
    val text: LiveData<String> = _text
}