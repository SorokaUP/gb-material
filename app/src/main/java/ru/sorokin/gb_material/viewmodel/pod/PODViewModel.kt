package ru.sorokin.gb_material.viewmodel.pod

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.sorokin.gb_material.BuildConfig
import ru.sorokin.gb_material.model.pod.PODServerResponseData
import ru.sorokin.gb_material.viewmodel.AppState
import ru.sorokin.gb_material.viewmodel.CommonViewModel
import ru.sorokin.gb_material.util.*

class PODViewModel: CommonViewModel() {

    fun getData(dateString: String) {
        data.value = AppState.Loading
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            AppState.Error(Throwable("Требуется API ключ для NASA"))
        } else {
            retrofitImpl.getPODRetrofitImpl().getPictureOfTheDay(apiKey, dateString).enqueue(object :
                Callback<PODServerResponseData> {
                override fun onResponse(
                    call: Call<PODServerResponseData>,
                    response: Response<PODServerResponseData>
                ) {
                    data.value = response.getResponse()
                }

                override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                    data.value = AppState.Error(t)
                }
            })
        }
    }
}