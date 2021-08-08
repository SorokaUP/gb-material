package ru.sorokin.gb_material.viewmodel.earth

import ru.sorokin.gb_material.BuildConfig
import ru.sorokin.gb_material.R
import ru.sorokin.gb_material.model.earth.EarthResponseData
import ru.sorokin.gb_material.viewmodel.AppState
import ru.sorokin.gb_material.viewmodel.CommonViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EarthViewModel : CommonViewModel() {

    fun getData(dateString: String, lon: Float, lat: Float, dim: Float) {
        data.value = AppState.Loading
        val apiKey: String = BuildConfig.NASA_API_KEY
        retrofitImpl.getEarthRetrofitImpl().getEarthPhoto(lon, lat, dim, apiKey, dateString)
            .enqueue(object :
                Callback<EarthResponseData> {
                override fun onResponse(
                    call: Call<EarthResponseData>,
                    response: Response<EarthResponseData>
                ) {
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

                override fun onFailure(call: Call<EarthResponseData>, t: Throwable) {
                    data.value = AppState.Error(
                        Throwable(t.message ?: getString(R.string.error_request_msg))
                    )
                }
            })
    }
}