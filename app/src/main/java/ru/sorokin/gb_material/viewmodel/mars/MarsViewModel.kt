package ru.sorokin.gb_material.viewmodel.mars

import ru.sorokin.gb_material.BuildConfig
import ru.sorokin.gb_material.R
import ru.sorokin.gb_material.model.mars.MarsPhotosResponseData
import ru.sorokin.gb_material.model.settings.SettingsData
import ru.sorokin.gb_material.viewmodel.AppState
import ru.sorokin.gb_material.viewmodel.CommonViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarsViewModel : CommonViewModel() {

    fun getMarsPhotosFromServer(dateString: String) {
        data.value = AppState.Loading
        val apiKey = BuildConfig.NASA_API_KEY
        val camera = SettingsData.marsRoverCamera
        retrofitImpl.getMarsRetrofitImpl().getMarsPhotos(camera, apiKey, dateString)
            .enqueue(object :
                Callback<MarsPhotosResponseData> {
                override fun onResponse(
                    call: Call<MarsPhotosResponseData>,
                    response: Response<MarsPhotosResponseData>
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

                override fun onFailure(call: Call<MarsPhotosResponseData>, t: Throwable) {
                    data.value = AppState.Error(
                        Throwable(t.message ?: getString(R.string.error_request_msg))
                    )
                }
            })
    }
}