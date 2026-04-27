package com.gsm.newscircle.ui.country

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gsm.newscircle.R
import com.gsm.newscircle.databinding.CountryListBottomSheetBinding
import com.gsm.newscircle.ui.base.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CountryListBottomSheet(private val selectedCountryCode: String) :
    BottomSheetDialogFragment() {

    private var _binding: CountryListBottomSheetBinding? = null
    val binding: CountryListBottomSheetBinding
        get() = _binding!!

    @Inject
    lateinit var countryListAdapter: CountryListAdapter

    private val countryListViewModel: CountryListViewModel by activityViewModels()

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CountryListBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupObserver()
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog as? BottomSheetDialog
        val bottomSheet =
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            behavior.skipCollapsed = true
        }
    }

    private fun setupUI() {
        binding.apply {
            // Close bottom sheet
            ivCancel.setOnClickListener {
                dismiss()
            }

            rvCountries.setHasFixedSize(true)
            rvCountries.adapter = countryListAdapter

            countryListAdapter.itemClickListener = { selectedCountry ->
                countryListViewModel.setCountry(selectedCountry)
                dismiss()
            }
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                countryListViewModel.countryUiState.collect {
                    when (it) {
                        // Loading has empty block as countries are fetched locally
                        UiState.Loading -> {}

                        is UiState.Success -> {
                            countryListAdapter.updateCountryData(it.data, selectedCountryCode)
                        }

                        is UiState.Error -> {
                            Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
