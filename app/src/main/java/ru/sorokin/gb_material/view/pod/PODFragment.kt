package ru.sorokin.gb_material.view.pod

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.pod_fragment.*
import ru.sorokin.gb_material.R
import ru.sorokin.gb_material.databinding.BottomSheetLayoutBinding
import ru.sorokin.gb_material.databinding.PodFragmentBinding
import ru.sorokin.gb_material.model.pod.PODServerResponseData
import ru.sorokin.gb_material.model.settings.SettingsData
import ru.sorokin.gb_material.util.*
import ru.sorokin.gb_material.viewmodel.AppState
import ru.sorokin.gb_material.viewmodel.pod.PODViewModel
import java.util.*

class PODFragment : Fragment() {

    private var _binding: PodFragmentBinding? = null
    private val binding get() = _binding!!

    private var _bottomSheetBinding: BottomSheetLayoutBinding? = null
    private val bottomSheetBinding get() = _bottomSheetBinding!!
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val viewModel: PODViewModel by lazy {
        ViewModelProvider(this).get(PODViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PodFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBottomSheetBehavior(view)
        initChips()

        val observer = Observer<AppState> { renderData(it) }
        viewModel.setStringResources { id: Int -> getString(id) }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
    }

    private fun getData() {
        viewModel.getData(Date().subDays(SettingsData.dayOfPhoto).format())
    }

    private fun initBottomSheetBehavior(view: View) {
        val bottomSheet: ConstraintLayout = view.findViewById(R.id.bottom_sheet_container)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        _bottomSheetBinding = BottomSheetLayoutBinding.bind(bottomSheet)
    }

    private fun initChips() = with(binding) {
        chipGroup.check(R.id.photo_today_chip)

        chipGroup.setOnCheckedChangeListener { chipGroup, position ->
            chipGroup.findViewById<Chip>(position)?.let {
                it.isChecked = true
                var newDayOfPhoto = SettingsData.dayOfPhoto
                when (it.id) {
                    R.id.photo_today_chip -> newDayOfPhoto = SettingsData.TODAY_PHOTO
                    R.id.photo_yesterday_chip -> newDayOfPhoto = SettingsData.YESTERDAY_PHOTO
                    R.id.photo_before_yesterday_chip -> newDayOfPhoto =
                        SettingsData.DAY_BEFORE_YESTERDAY_PHOTO
                }
                if (newDayOfPhoto != SettingsData.dayOfPhoto) {
                    SettingsData.dayOfPhoto = newDayOfPhoto
                    getData()
                }
            }
        }
    }

    private fun renderData(data: AppState) = with(binding) {
        when (data) {
            is AppState.Success<*> -> {
                if (data.response !is PODServerResponseData)
                    return false

                val serverResponseData = data.response
                val url = serverResponseData.url

                if (data.response.mediaType == "image") {
                    loadImageFromCallback(
                        url,
                        podImageView,
                        podLoadingLayout,
                        podRootView
                    ) { getData() }

                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                } else {
                    podImageView.setImageResource(R.drawable.ic_no_photo_vector)
                    podLoadingLayout.hide()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                }

                bottomSheetBinding.apply {
                    bottomSheetDescriptionHeader.text = data.response.title
                    bottomSheetDescription.text = data.response.explanation
                }
            }
            is AppState.Loading -> {
                podLoadingLayout.show()
            }
            is AppState.Error -> {
                callbackError(
                    data.error.message ?: getString(R.string.error_msg),
                    podLoadingLayout,
                    root
                ) { getData() }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
