package ru.sorokin.gb_material.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.sorokin.gb_material.model.RetrofitImpl

open class CommonViewModel : ViewModel() {
    protected val data: MutableLiveData<AppState> = MutableLiveData()
    protected val retrofitImpl = RetrofitImpl()
    private var stringResources: ((Int) -> String)? = null

    fun getLiveData() = data

    fun setStringResources(stringResources: (Int) -> String) {
        this.stringResources = stringResources
    }

    protected fun getString(id: Int) = stringResources?.invoke(id)
}