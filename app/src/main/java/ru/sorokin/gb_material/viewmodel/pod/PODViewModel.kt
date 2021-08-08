package ru.sorokin.gb_material.viewmodel.pod

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.sorokin.gb_material.BuildConfig
import ru.sorokin.gb_material.model.RetrofitImpl
import ru.sorokin.gb_material.model.pod.PODServerResponseData
import ru.sorokin.gb_material.viewmodel.AppState
import ru.sorokin.gb_material.viewmodel.CommonViewModel
import java.text.SimpleDateFormat
import java.util.*

class PODViewModel: CommonViewModel() {

    fun getData(dateString: String) {
        data.value = AppState.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            AppState.Error(Throwable("Требуется API ключ для NASA"))
        } else {
            retrofitImpl.getRetrofitImpl().getPictureOfTheDay(apiKey, dateString).enqueue(object :
                Callback<PODServerResponseData> {
                override fun onResponse(
                    call: Call<PODServerResponseData>,
                    response: Response<PODServerResponseData>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        data.value =
                            AppState.Success(response.body()!!)
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            data.value =
                                AppState.Error(Throwable("Неизвестная ошибка"))
                        } else {
                            data.value =
                                AppState.Error(Throwable(message))
                        }
                    }
                }

                override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                    data.value = AppState.Error(t)
                }
            })
        }
    }
}