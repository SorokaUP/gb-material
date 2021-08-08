package ru.sorokin.gb_material.view.earth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import ru.sorokin.gb_material.R
import ru.sorokin.gb_material.databinding.EarthFragmentBinding
import ru.sorokin.gb_material.model.earth.EarthResponseData
import ru.sorokin.gb_material.model.settings.SettingsData
import ru.sorokin.gb_material.util.*
import ru.sorokin.gb_material.viewmodel.AppState
import ru.sorokin.gb_material.viewmodel.earth.EarthViewModel
import java.util.*

class EarthFragment : Fragment() {
    private var _binding: EarthFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this).get(EarthViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EarthFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        val observer = Observer<AppState> { renderData(it) }
        viewModel.setStringResources { id: Int -> getString(id) }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)

        getData()
    }

    private fun initViews() = with(binding) {
        earthLatEditText.text = "${SettingsData.earthLat}".toEditable()
        earthLonEditText.text = "${SettingsData.earthLon}".toEditable()
        earthDimEditText.text = "${SettingsData.earthDim}".toEditable()
        earthApplyChip.setOnClickListener {
            val lat = earthLatEditText.text.toString().toFloat()
            val lon = earthLonEditText.text.toString().toFloat()
            val dim = earthDimEditText.text.toString().toFloat()
            if ((SettingsData.earthLat != lat)
                || (SettingsData.earthLon != lon)
                || (SettingsData.earthDim != dim)
            ) {
                SettingsData.earthLat = lat
                SettingsData.earthLon = lon
                SettingsData.earthDim = dim
                writeSettings()
                getData()
            }
        }
    }

    private fun writeSettings() {
        activity?.let {
            with(
                it.getSharedPreferences(SettingsData.PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
            ) {
                putFloat(SettingsData.CURRENT_EARTH_LAT, SettingsData.earthLat)
                putFloat(SettingsData.CURRENT_EARTH_LON, SettingsData.earthLon)
                putFloat(SettingsData.CURRENT_EARTH_DIM, SettingsData.earthDim)
                apply()
            }
        }
    }

    private fun getData() {
        viewModel.getEarthPhotoFromServer(
            Date().format(),
            SettingsData.earthLon,
            SettingsData.earthLat,
            SettingsData.earthDim
        )
    }

    private fun renderData(data: AppState) = with(binding) {
        when (data) {
            is AppState.Success<*> -> {
                if (data.response !is EarthResponseData)
                    return

                val url = data.response.url

                Picasso
                    .get()
                    .load(url)
                    .placeholder(R.drawable.ic_no_photo_vector)
                    .into(earthImageView, object : Callback {
                        override fun onSuccess() {
                            earthLoadingLayout.hide()
                        }

                        override fun onError(e: Exception?) {
                            earthRootView.showSnackBar(e?.message
                                ?: getString(R.string.error_server_msg),
                                getString(R.string.reload_msg),
                                { getData() })
                        }
                    })
            }
            is AppState.Loading -> {
                earthLoadingLayout.show()
            }
            is AppState.Error -> {
                earthLoadingLayout.hide()
                earthRootView.showSnackBar(data.error.message ?: getString(R.string.error_msg),
                    getString(R.string.reload_msg),
                    { getData() })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}