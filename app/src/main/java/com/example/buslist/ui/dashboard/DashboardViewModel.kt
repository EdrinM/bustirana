package com.example.buslist.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        // Change the value to an empty string or any desired text
        value = ""
    }
    val text: LiveData<String> = _text
}
