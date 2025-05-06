package com.example.applearndriver.app.ui.study

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.applearndriver.R
import com.example.applearndriver.base.BaseViewModel
import com.example.applearndriver.constant.AppConstant
import com.example.applearndriver.data.model.NewQuestion
import com.example.applearndriver.data.model.QuestionOptions
import com.example.applearndriver.data.model.QuestionType
import com.example.applearndriver.data.model.StudyCategory
import com.example.applearndriver.data.model.WrongAnswer
import com.example.applearndriver.data.repository.StudyRepository
import com.example.applearndriver.data.repository.WrongAnswerRepository
import com.example.applearndriver.utils.extensions.getCurrentLicenseType
import com.example.applearndriver.utils.generateEmptyQuestionStateList
import com.example.applearndriver.utils.interfaces.IResponseListener
import com.example.applearndriver.utils.provideEmptyQuestionOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudyViewModel @Inject constructor(
    private val studyRepository: StudyRepository,
    private val wrongAnswerRepository: WrongAnswerRepository,
    sharedPreferences: SharedPreferences
) : BaseViewModel(sharedPreferences) {

    init {
        onUpdateListStudyCategory()
    }

    private val _listStudyCategory = MutableLiveData(listDefaultStudyCategory)

    val listStudyCategory: LiveData<List<StudyCategory>>
        get() = _listStudyCategory

    private val _currentStudyCategoryIndex = MutableLiveData(AppConstant.NONE_POSITION)


    val currentStudyCategory: LiveData<Int>
        get() = _currentStudyCategoryIndex

    fun setCurrentStudyCategoryPosition(index: Int) {
        _currentStudyCategoryIndex.postValue(index)
    }

    fun saveProgressToDatabase() {
        viewModelScope.launch {
            listStudyCategory.value?.let {
                studyRepository.saveProgress(it)
            }
        }
    }

    fun saveWrongAnswerObjectToDatabase(wrongAnswer: WrongAnswer) {
        viewModelScope.launch {
            if (wrongAnswerRepository.findWrongAnswerQuestionByID(wrongAnswer.questionsID) != null) {
                wrongAnswerRepository.updateWrongAnswerQuestion(wrongAnswer)
            } else {
                wrongAnswerRepository.insertNewWrongAnswerQuestion(wrongAnswer)
            }
        }
    }

    private fun onUpdateListStudyCategory() {
        launchTask {
            val data = studyRepository.getAllInfoStudyCategory()

            if (data.isEmpty()) {
                studyRepository.saveProgress(listDefaultStudyCategory)
                studyRepository.getListQuestion(
                    object : IResponseListener<MutableList<NewQuestion>> {
                        override fun onSuccess(data: MutableList<NewQuestion>) {
                            viewModelScope.launch {
                                val newList = mutableListOf<StudyCategory>()
                                val oldList = _listStudyCategory.value ?: listDefaultStudyCategory

                                newList.apply {
                                    enumValues<QuestionType>().forEach {
                                        add(
                                            data.processPullListData(
                                                it.type,
                                                oldList[it.position],
                                                it.title
                                            )
                                        )
                                    }
                                }

                                studyRepository.saveProgress(newList)
                                _listStudyCategory.postValue(newList)
                                hideLoading()
                            }
                        }

                        override fun onError(exception: Exception?) {
                            this@StudyViewModel.exception.postValue(exception)
                            hideLoading()
                        }
                    }
                )
            } else {
                _listStudyCategory.postValue(data)
                hideLoading()
            }
        }
    }

    private fun MutableList<NewQuestion>.processPullListData(
        categoryFilter: String,
        lastData: StudyCategory,
        newTitleIfFix: String,
    ): StudyCategory {
        val listQuestion = if (categoryFilter == QuestionType.ALL.type) this.toMutableList()
        else this.filter { return@filter it.questionType == categoryFilter }.toMutableList()
        return StudyCategory(
            lastData.id,
            lastData.iconResourceID,
            newTitleIfFix,
            lastData.startIDQuestion,
            lastData.endIDQuestion,
            listQuestion,
            if (lastData.listQuestionsState.isEmpty() ||
                lastData.listQuestionsState.size != listQuestion.size
            )
                generateEmptyQuestionStateList(listQuestion)
            else lastData.listQuestionsState,
            listQuestion.size,
            lastData.numbersOfSelectedQuestions,
        )
    }

    fun updateDataQuestionPos(questionID: Int, questionsPosition: Int, item: QuestionOptions) {
        val currentStudyCategoryPos = currentStudyCategory.value
        val currentListStudy = _listStudyCategory.value

        if (currentStudyCategoryPos != null && currentListStudy != null) {
            val list = mutableListOf<StudyCategory>()
            list.addAll(currentListStudy)

            if (currentStudyCategoryPos != QuestionType.ALL.position) {
                list[currentStudyCategoryPos]
                list[currentStudyCategoryPos].listQuestionsState[questionsPosition] = item
                list[currentStudyCategoryPos].numbersOfSelectedQuestions =
                    list[currentStudyCategoryPos]
                        .listQuestionsState.count { it.position != AppConstant.NONE_POSITION }
            } else {
                enumValues<QuestionType>().forEach {
                    if (it.position != QuestionType.ALL.position) {
                        val questionPos = list[it.position].listQuestionsState
                            .indexOfFirst { elem -> elem.questionsID == questionID }

                        if (questionPos != AppConstant.NONE_POSITION) {
                            list[it.position].listQuestionsState[questionPos] = item
                            list[it.position].numbersOfSelectedQuestions =
                                list[it.position]
                                    .listQuestionsState.count { elem ->
                                        elem.position != AppConstant.NONE_POSITION
                                    }
                        }
                    }
                }
            }

            val questionIndex =
                list[QuestionType.ALL.position].listQuestionsState.indexOfFirst { elem ->
                    elem.questionsID == questionID
                }

            list[QuestionType.ALL.position].listQuestionsState[questionIndex] = item
            list[QuestionType.ALL.position].numbersOfSelectedQuestions =
                list[QuestionType.ALL.position]
                    .listQuestionsState
                    .count { it.position != AppConstant.NONE_POSITION }

            _listStudyCategory.postValue(list)
        }
    }

    fun getQuestionOptionSelectedByQuestionPosition(questionsPosition: Int): QuestionOptions? {
        val currentStudyCategoryPos = currentStudyCategory.value

        if (currentStudyCategoryPos != null
            && currentStudyCategoryPos != AppConstant.NONE_POSITION
            && questionsPosition != AppConstant.NONE_POSITION
        ) {
            _listStudyCategory.value?.let { notNullList ->
                if (questionsPosition < notNullList[currentStudyCategoryPos].listQuestionsState.size) {
                    return notNullList[currentStudyCategoryPos].listQuestionsState[questionsPosition]
                }
            }
        }

        return null
    }

    fun resetAllStudyCategoryState() {
        launchTask {
            val newList = listStudyCategory.value

            newList?.forEach {
                it.numbersOfSelectedQuestions = DEFAULT_NOT_SELECTED_STATE
                it.listQuestionsState.clear()
                it.listQuestionsState.addAll(generateEmptyQuestionStateList(it.listQuestions))
            }

            _listStudyCategory.value = newList

            newList?.let {
                studyRepository.saveProgress(it)
                hideLoading()
            }
        }
    }

    suspend fun getSelectedStateWrongAnswerByID(questionsID: Int): QuestionOptions {
        return wrongAnswerRepository.findWrongAnswerQuestionByID(questionsID)?.lastSelectedState ?: provideEmptyQuestionOption(questionsID)
    }

    companion object {
        const val DEFAULT_NOT_SELECTED_STATE = 0
        val listDefaultStudyCategory = listOf(
            StudyCategory(
                id = QuestionType.ALL.position,
                iconResourceID = R.drawable.ic_study,
                title = QuestionType.ALL.title,
                startIDQuestion = QuestionType.ALL.startIDQuestion,
                endIDQuestion = QuestionType.ALL.endIDQuestion,
            ),
            StudyCategory(
                id = QuestionType.TRAFFIC_CONCEPT_AND_RULES.position,
                iconResourceID = R.drawable.ic_traffic_concept_and_rules,
                title = QuestionType.TRAFFIC_CONCEPT_AND_RULES.title,
                startIDQuestion = QuestionType.TRAFFIC_CONCEPT_AND_RULES.startIDQuestion,
                endIDQuestion = QuestionType.TRAFFIC_CONCEPT_AND_RULES.endIDQuestion,
            ),
            StudyCategory(
                id = QuestionType.DRIVING_BUSINESS.position,
                iconResourceID = R.drawable.ic_driving_business,
                title = QuestionType.DRIVING_BUSINESS.title,
                startIDQuestion = QuestionType.DRIVING_BUSINESS.startIDQuestion,
                endIDQuestion = QuestionType.DRIVING_BUSINESS.endIDQuestion,
            ),
            StudyCategory(
                id = QuestionType.ETHICS_IN_DRIVING.position,
                iconResourceID = R.drawable.ic_ethics_in_driving,
                title = QuestionType.ETHICS_IN_DRIVING.title,
                startIDQuestion = QuestionType.ETHICS_IN_DRIVING.startIDQuestion,
                endIDQuestion = QuestionType.ETHICS_IN_DRIVING.endIDQuestion
            ),
            StudyCategory(
                id = QuestionType.DRIVING_TECHNIQUE.position,
                iconResourceID = R.drawable.ic_driving_technique,
                title = QuestionType.DRIVING_TECHNIQUE.title,
                startIDQuestion = QuestionType.DRIVING_TECHNIQUE.startIDQuestion,
                endIDQuestion = QuestionType.DRIVING_TECHNIQUE.endIDQuestion
            ),
            StudyCategory(
                id = QuestionType.FIXING_CAR_QUESTION.position,
                iconResourceID = R.drawable.ic_fixing_car,
                title = QuestionType.FIXING_CAR_QUESTION.title,
                startIDQuestion = QuestionType.FIXING_CAR_QUESTION.startIDQuestion,
                endIDQuestion = QuestionType.FIXING_CAR_QUESTION.endIDQuestion
            ),
            StudyCategory(
                id = QuestionType.TRAFFIC_SIGNAL.position,
                iconResourceID = R.drawable.ic_traffic_sign_question,
                title = QuestionType.TRAFFIC_SIGNAL.title,
                startIDQuestion = QuestionType.TRAFFIC_SIGNAL.startIDQuestion,
                endIDQuestion = QuestionType.TRAFFIC_SIGNAL.endIDQuestion
            ),
            StudyCategory(
                id = QuestionType.TRAFFIC_SITUATION.position,
                iconResourceID = R.drawable.ic_traffic_situation,
                title = QuestionType.TRAFFIC_SITUATION.type,
                startIDQuestion = QuestionType.TRAFFIC_SITUATION.startIDQuestion,
                endIDQuestion = QuestionType.TRAFFIC_SITUATION.endIDQuestion
            ),
        )
    }
}
