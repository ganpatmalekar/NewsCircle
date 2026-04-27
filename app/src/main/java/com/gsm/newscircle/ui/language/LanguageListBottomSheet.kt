package com.gsm.newscircle.ui.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gsm.newscircle.NewsApplication
import com.gsm.newscircle.R
import com.gsm.newscircle.databinding.LanguageListBottomSheetBinding
import com.gsm.newscircle.di.component.DaggerActivityComponent
import com.gsm.newscircle.di.module.ActivityModule
import com.gsm.newscircle.ui.base.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

class LanguageListBottomSheet(private val selectedLanguageCode: String) :
    BottomSheetDialogFragment() {

    private var _binding: LanguageListBottomSheetBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var languageListViewModel: LanguageListViewModel

    @Inject
    lateinit var languageListAdapter: LanguageListAdapter

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LanguageListBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObserver()
    }

    private fun setupUI() {
        binding.apply {
            ivCancel.setOnClickListener { dismiss() }

            languageListAdapter.itemClickListener = { languageSelected ->
                languageListViewModel.setLanguage(languageSelected)
                dismiss()
            }

            rvSelectLanguage.apply {
                setHasFixedSize(true)
                adapter = languageListAdapter
            }
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                languageListViewModel.languageUiState.collect {
                    when (it) {
                        UiState.Loading -> {}

                        is UiState.Success -> {
                            languageListAdapter.updateData(it.data, selectedLanguageCode)
                        }

                        is UiState.Error -> {
                            Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
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

    private fun injectDependencies() {
        DaggerActivityComponent.builder()
            .applicationComponent((requireActivity().application as NewsApplication).daggerComponent)
            .activityModule(ActivityModule(requireActivity() as AppCompatActivity))
            .build()
            .inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
