package ru.sorokin.gb_material.view.mars

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
import ru.sorokin.gb_material.databinding.MarsFragmentBinding
import ru.sorokin.gb_material.model.mars.MarsPhotosResponseData
import ru.sorokin.gb_material.util.*
import ru.sorokin.gb_material.viewmodel.AppState
import ru.sorokin.gb_material.viewmodel.mars.MarsViewModel
import java.util.*

class MarsFragment : Fragment() {
    private var _binding: MarsFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this).get(MarsViewModel::class.java)
    }

    private var isImageExpanded = false

    private var daysBefore = START_DAYS_BEFORE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MarsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initImageExpansion()

        val observer = Observer<AppState> { renderData(it) }
        viewModel.setStringResources { id: Int -> getString(id) }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)

        getData()
    }

    private fun initImageExpansion() = with(binding) {
        marsImageView.setOnClickListener {
            isImageExpanded = isImageExpanded.not()
            marsImageView.imageTransform(isImageExpanded, marsRootView)
        }
    }

    private fun getData() {
        viewModel.getData(Date().subDays(daysBefore).format())
    }

    private fun renderData(data: AppState) = with(binding) {
        when (data) {
            is AppState.Success<*> -> {
                if (data.response !is MarsPhotosResponseData)
                    return

                val responseData = data.response
                if (responseData.photos.isEmpty()) {
                    if (daysBefore < MAX_DAYS_BEFORE) {
                        daysBefore++
                        getData()
                    } else {
                        daysBefore = START_DAYS_BEFORE
                        marsLoadingLayout.hide()
                        marsImageView.setImageResource(R.drawable.ic_no_photo_vector)
                    }
                } else {
                    val url = responseData.photos.first().url
                    val earthDate = responseData.photos.first().earth_date
                    marsDateTextView.text = earthDate

                    loadImageFromCallback(
                        url,
                        marsImageView,
                        marsLoadingLayout,
                        marsRootView
                    ) { getData() }
                }
            }
            is AppState.Loading -> {
                marsLoadingLayout.show()
            }
            is AppState.Error -> {
                callbackError(
                    data.error.message ?: getString(R.string.error_msg),
                    marsLoadingLayout,
                    marsRootView
                ) { getData() }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val MAX_DAYS_BEFORE = 30
        const val START_DAYS_BEFORE = 0
    }
}