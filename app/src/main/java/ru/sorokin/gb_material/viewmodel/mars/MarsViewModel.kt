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
import ru.sorokin.gb_material.util.getResponse

class MarsViewModel : CommonViewModel() {

    fun getData(dateString: String) {
        if (BuildConfig.NASA_API_KEY.isBlank()) {
            AppState.Error(Throwable(getString(R.string.nasa_api_key_blank)))
        } else {
            getFromServer(
                retrofitImpl
                    .getMarsRetrofitImpl()
                    .getMarsPhotos(SettingsData.marsRoverCamera, BuildConfig.NASA_API_KEY, dateString)
            )
        }
    }
}