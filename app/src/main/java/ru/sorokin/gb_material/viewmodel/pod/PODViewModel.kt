package ru.sorokin.gb_material.viewmodel.pod

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.sorokin.gb_material.BuildConfig
import ru.sorokin.gb_material.R
import ru.sorokin.gb_material.model.pod.PODServerResponseData
import ru.sorokin.gb_material.viewmodel.AppState
import ru.sorokin.gb_material.viewmodel.CommonViewModel
import ru.sorokin.gb_material.util.*

class PODViewModel: CommonViewModel() {

    fun getData(dateString: String) {
        if (BuildConfig.NASA_API_KEY.isBlank()) {
            AppState.Error(Throwable(getString(R.string.nasa_api_key_blank)))
        } else {
            getFromServer(
                retrofitImpl
                    .getPODRetrofitImpl()
                    .getPictureOfTheDay(BuildConfig.NASA_API_KEY, dateString)
            )
        }
    }
}