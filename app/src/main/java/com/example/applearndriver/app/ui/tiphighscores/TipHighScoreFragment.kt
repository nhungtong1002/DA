package com.example.applearndriver.app.ui.tiphighscores

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.applearndriver.base.BaseFragment
import com.example.applearndriver.databinding.FragmentTipHighScoreBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.tiphighscores.TipsHighScoreAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TipHighScoreFragment :
    BaseFragment<FragmentTipHighScoreBinding>(FragmentTipHighScoreBinding::inflate) {

    override val viewModel by viewModels<TipsHighScoreViewModel>()

    private val adapter by lazy { TipsHighScoreAdapter() }

    override fun initData() {
        viewBinding.recyclerViewTipsHighScore.adapter = adapter
        viewBinding.recyclerViewTipsHighScore.isVisible = adapter.currentList.isEmpty().not()
        viewBinding.layoutVisibleWhenDataIsEmpty.root.isVisible = adapter.currentList.isEmpty()
    }

    override fun handleEvent() {
        // Nothing
    }

    override fun bindData() {
        viewModel.listTipsHighScore.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            viewBinding.recyclerViewTipsHighScore.isVisible = adapter.currentList.isEmpty().not()
            viewBinding.layoutVisibleWhenDataIsEmpty.root.isVisible = adapter.currentList.isEmpty()
        }
    }
}
