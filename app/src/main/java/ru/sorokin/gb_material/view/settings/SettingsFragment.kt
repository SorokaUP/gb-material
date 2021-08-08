package ru.sorokin.gb_material.view.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import ru.sorokin.gb_material.R
import ru.sorokin.gb_material.model.settings.SettingsData
import ru.sorokin.gb_material.databinding.SettingsFragmentBinding

class SettingsFragment : Fragment() {

    private var _binding: SettingsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() = with(binding) {
        (radioGroup.getChildAt(SettingsData.CURRENT_THEME) as RadioButton).isChecked = true

        applyButton.setOnClickListener {
            val oldValue = SettingsData.CURRENT_THEME
            SettingsData.CURRENT_THEME = when (radioGroup.checkedRadioButtonId) {
                R.id.radio_button_teal -> SettingsData.THEME_PINK
                else -> SettingsData.THEME_INDIGO
            }
            activity?.supportFragmentManager?.popBackStack()
            if (SettingsData.CURRENT_THEME != oldValue) {
                writeSettings()
                activity?.recreate()
            }
        }
    }

    private fun writeSettings() {
        activity?.let {
            with(
                it.getSharedPreferences(SettingsData.PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
            ) {
                putInt(SettingsData.CURRENT_THEME_PREF_NAME, SettingsData.CURRENT_THEME)
                apply()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}
