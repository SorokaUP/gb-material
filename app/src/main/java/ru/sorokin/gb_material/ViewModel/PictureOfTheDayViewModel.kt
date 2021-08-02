package ru.sorokin.gb_material.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.sorokin.gb_material.BuildConfig
import ru.sorokin.gb_material.Model.PODRetrofitImpl
import ru.sorokin.gb_material.Model.PODServerResponseData
import ru.sorokin.gb_material.Model.PictureOfTheDayData
import java.text.SimpleDateFormat
import java.util.*

const val DATE_FORMAT = "yyyy-MM-dd";

class PictureOfTheDayViewModel(
    private val liveDataForViewToObserve: MutableLiveData<PictureOfTheDayData> = MutableLiveData(),
    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()
) :
    ViewModel() {

    fun getDataToday(): LiveData<PictureOfTheDayData> {
        sendServerRequest(0)
        return liveDataForViewToObserve
    }

    fun getDataBack1(): LiveData<PictureOfTheDayData> {
        sendServerRequest(1)
        return liveDataForViewToObserve
    }

    fun getDataBack2(): LiveData<PictureOfTheDayData> {
        sendServerRequest(2)
        return liveDataForViewToObserve
    }

    private fun sendServerRequest(minusDays: Int) {
        val date = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
            .format(
                Calendar.getInstance().apply {
                    this.add(Calendar.DATE, -minusDays)
                }.time)

        liveDataForViewToObserve.value = PictureOfTheDayData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            PictureOfTheDayData.Error(Throwable("You need API key"))
        } else {
            retrofitImpl.getRetrofitImpl().getPictureOfTheDay(apiKey, date).enqueue(object :
                Callback<PODServerResponseData> {
                override fun onResponse(
                    call: Call<PODServerResponseData>,
                    response: Response<PODServerResponseData>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        liveDataForViewToObserve.value =
                            PictureOfTheDayData.Success(response.body()!!)
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            liveDataForViewToObserve.value =
                                PictureOfTheDayData.Error(Throwable("Unidentified error"))
                        } else {
                            liveDataForViewToObserve.value =
                                PictureOfTheDayData.Error(Throwable(message))
                        }
                    }
                }

                override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                    liveDataForViewToObserve.value = PictureOfTheDayData.Error(t)
                }
            })
        }
    }
}