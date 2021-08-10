package ru.sorokin.gb_material.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.sorokin.gb_material.R
import ru.sorokin.gb_material.model.RetrofitImpl

open class CommonViewModel : ViewModel() {
    protected val data: MutableLiveData<AppState> = MutableLiveData()
    protected val retrofitImpl = RetrofitImpl()
    private var stringResources: ((Int) -> String)? = null

    fun getLiveData() = data

    protected fun <T> getFromServer(api: Call<T>) {
        data.value = AppState.Loading
        api.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful && response.body() != null) {
                    data.value = AppState.Success(response.body()!!)
                } else {
                    val message = response.message()
                    if (message.isNullOrEmpty()) {
                        data.value =
                            AppState.Error(Throwable(getString(R.string.error_server_msg)))
                    } else {
                        data.value = AppState.Error(Throwable(message))
                    }
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                data.value = AppState.Error(
                    Throwable(t.message ?: getString(R.string.error_request_msg))
                )
            }
        })
    }

    fun setStringResources(stringResources: (Int) -> String) {
        this.stringResources = stringResources
    }

    protected fun getString(id: Int) = stringResources?.invoke(id)
}