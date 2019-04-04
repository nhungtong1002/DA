package com.example.applearndriver.app.ui.exam

import android.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.applearndriver.R
import com.example.applearndriver.app.ui.main.MainActivity
import com.example.applearndriver.app.ui.exam.infodialog.ExamInfoDialog
import com.example.applearndriver.base.BaseFragment
import com.example.applearndriver.constant.AppConstant
import com.example.applearndriver.data.model.ExamState
import com.example.applearndriver.data.model.LicenseType
import com.example.applearndriver.data.model.findCreateExamRuleByLicenseTypeString
import com.example.applearndriver.databinding.FragmentExamBinding
import com.example.applearndriver.utils.extensions.showToast
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.exam.ExamAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExamFragment : BaseFragment<FragmentExamBinding>(FragmentExamBinding::inflate) {

    override val viewModel by activityViewModels<ExamViewModel>()

    private val examAdapter by lazy { ExamAdapter() }

    private var isAddExam = false

    private val smoothScroller by lazy {
        object : LinearSmoothScroller(activity) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
    }

    private val currentLicenseType
        get() = arguments?.getString(AppConstant.KEY_BUNDLE_CURRENT_LICENSE_TYPE) ?: LicenseType.A1.type

    override fun initData() {
        (activity as? MainActivity)?.apply {
            updateTitleToolbar("Đề hạng $currentLicenseType")
            addCallbackExamInfoButton {
                ExamInfoDialog.showExamInfoDialog(this, findCreateExamRuleByLicenseTypeString(this@ExamFragment.currentLicenseType))
            }
        }
        viewBinding.recyclerViewExam.apply {
            setRecycledViewPool(RecyclerView.RecycledViewPool())
            adapter = examAdapter

            // Xử lý ẩn nút thêm khi cuộn xuống
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val isScrollUp = dy > 0

                    viewBinding.buttonAddExam.apply {
                        if(isScrollUp) {
                            hide()
                        } else {
                            show()
                        }
                    }
                }
            })
        }
        viewModel.getExamByLicenseType(currentLicenseType)
    }

    override fun handleEvent() {
        examAdapter.registerOnClickExamButtonEvent {
            viewModel.setCurrentExam(it)
            if (viewModel.listExam.value?.get(it)?.examState == ExamState.UNDEFINED.value) {
                viewModel.setVisibleFinishExamButton(true)

                findNavController().navigate(
                    R.id.action_nav_exam_to_nav_detail_exam,
                    bundleOf(
                        AppConstant.KEY_BUNDLE_CURRENT_LICENSE_TYPE to currentLicenseType
                    )
                )
            } else {
                viewModel.setVisibleFinishExamButton(false)

                findNavController().navigate(R.id.action_nav_exam_to_nav_exam_result)
            }
        }

        examAdapter.registerOnClickWatchExamHistoryEvent { examPosition, exam ->
             findNavController().navigate(
                 R.id.action_nav_exam_to_nav_watch_history,
                 bundleOf(
                     AppConstant.KEY_BUNDLE_CURRENT_EXAM to exam,
                     AppConstant.KEY_BUNDLE_CURRENT_EXAM_POSITION to examPosition,
                 )
             )
        }

        viewBinding.buttonAddExam.setOnClickListener {
            val builder = AlertDialog.Builder(context)
                .setTitle(DIALOG_TITLE)
                .setMessage(DIALOG_MESSAGE)
                .setPositiveButton(
                    DIALOG_POSITIVE_BUTTON_TEXT
                ) { _, _ ->
                    isAddExam = true
                    viewModel.createExam(currentLicenseType) {
                        context?.showToast(MESSAGE_SUCCESS_ADD_EXAM)
                    }
                }
                .setNegativeButton(DIALOG_NEGATIVE_BUTTON_TEXT) { _, _ ->
                    //Not-op
                }
                .setCancelable(false)
            val dialog = builder.create()
            dialog.show()
        }
    }

    override fun bindData() {
        viewModel.listExam.observe(viewLifecycleOwner) { listExam ->
            val newListExam = listExam.map { it.copy() }.toMutableList()
            if (newListExam.isEmpty()) {
                viewBinding.layoutVisibleWhenDataIsEmpty.root.isVisible = true
                viewBinding.layoutVisibleWhenDataIsEmpty.textNotFounded.text =
                    MESSAGE_ADD_A_NEW_EXAM
            } else {
                viewBinding.layoutVisibleWhenDataIsEmpty.root.isVisible = false
            }
            viewBinding.recyclerViewExam.recycledViewPool.clear()
            examAdapter.submitList(newListExam)

            //Kiểm tra cuộn xuống dưới cùng nếu đề thi được thêm
            if(isAddExam) {
                val lastExamPosition = maxOf(newListExam.size - 1, 0)
                smoothScroller.targetPosition = lastExamPosition
                viewBinding.recyclerViewExam.layoutManager?.startSmoothScroll(smoothScroller)
                isAddExam = false
            }
        }
    }

    override fun onDestroyView() {
        (activity as? MainActivity)?.removeCallbackExamInfoButton()
        ExamInfoDialog.shutDownDialog()
        super.onDestroyView()
    }
    companion object {
        const val DEFAULT_ID = 0
        const val MESSAGE_SUCCESS_ADD_EXAM = "Thêm đề thi mới thành công"
        const val DIALOG_TITLE = "Thông báo"
        const val DIALOG_MESSAGE = "Bạn có muốn tạo mới 1 đề không ?"
        const val DIALOG_POSITIVE_BUTTON_TEXT = "Có"
        const val DIALOG_NEGATIVE_BUTTON_TEXT = "Không"
        const val MESSAGE_ADD_A_NEW_EXAM = "Vui lòng tạo đề mới"
    }
}
