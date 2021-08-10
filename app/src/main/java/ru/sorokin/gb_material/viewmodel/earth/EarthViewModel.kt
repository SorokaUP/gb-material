package ru.sorokin.gb_material.viewmodel.earth

import ru.sorokin.gb_material.BuildConfig
import ru.sorokin.gb_material.R
import ru.sorokin.gb_material.model.earth.EarthResponseData
import ru.sorokin.gb_material.viewmodel.AppState
import ru.sorokin.gb_material.viewmodel.CommonViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.sorokin.gb_material.model.settings.SettingsData
import ru.sorokin.gb_material.util.getResponse

class EarthViewModel : CommonViewModel() {

    fun getData(dateString: String, lon: Float, lat: Float, dim: Float) {
        if (BuildConfig.NASA_API_KEY.isBlank()) {
            AppState.Error(Throwable(getString(R.string.nasa_api_key_blank)))
        } else {
            getFromServer(
                retrofitImpl
                    .getEarthRetrofitImpl()
                    .getEarthPhoto(lon, lat, dim, BuildConfig.NASA_API_KEY, dateString)
            )
        }
    }
}