package com.scesi.appmobile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scesi.appmobile.model.QuoteModel
import com.scesi.appmobile.model.QuoteProvider

class QuoteViewModel: ViewModel() {
    val quoteModel = MutableLiveData<QuoteModel>()
    fun randomQuote() {
        val currentQuote = QuoteProvider.random()
        quoteModel.postValue(currentQuote)
    }
}