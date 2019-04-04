package com.example.applearndriver.app.ui.exam

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.applearndriver.base.BaseViewModel
import com.example.applearndriver.constant.AppConstant
import com.example.applearndriver.constant.AppConstant.FIRST_INDEX
import com.example.applearndriver.data.model.Exam
import com.example.applearndriver.data.model.ExamHistory
import com.example.applearndriver.data.model.ExamState
import com.example.applearndriver.data.model.NewQuestion
import com.example.applearndriver.data.model.QuestionOptions
import com.example.applearndriver.data.model.QuestionType
import com.example.applearndriver.data.model.StateQuestionOption
import com.example.applearndriver.data.model.WrongAnswer
import com.example.applearndriver.data.model.findCreateExamRuleByLicenseType
import com.example.applearndriver.data.model.findCreateExamRuleByLicenseTypeString
import com.example.applearndriver.data.model.getAllLowerQuestionList
import com.example.applearndriver.data.repository.ExamRepository
import com.example.applearndriver.data.repository.WrongAnswerRepository
import com.example.applearndriver.utils.CountDownInstance
import com.example.applearndriver.utils.extensions.convertMinutesToMillisecond
import com.example.applearndriver.utils.extensions.getCurrentLicenseType
import com.example.applearndriver.utils.extensions.isCurrentDarkMode
import com.example.applearndriver.utils.generateEmptyQuestionStateList
import com.example.applearndriver.utils.interfaces.IResponseListener
import com.example.applearndriver.utils.provideEmptyQuestionOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExamViewModel @Inject constructor(
    private val examRepository: ExamRepository,
    private val wrongAnswerRepository: WrongAnswerRepository,
    sharedPreferences: SharedPreferences,
) : BaseViewModel(sharedPreferences) {

    private val _listExam = MutableLiveData<MutableList<Exam>>()
    val listExam: LiveData<MutableList<Exam>>
        get() = _listExam

    private val _listQuestions = MutableLiveData<MutableList<NewQuestion>>()
    val listQuestions: LiveData<MutableList<NewQuestion>>
        get() = _listQuestions

    private val _currentExamPosition = MutableLiveData<Int>()
    val currentExamPosition: LiveData<Int>
        get() = _currentExamPosition

    private val _currentExamQuestionPosition = MutableLiveData<Int>()
    val currentExamQuestionPosition: LiveData<Int>
        get() = _currentExamQuestionPosition

    private val _currentTimeCountDown = MutableLiveData<String>()
    val currentTimeCountDown: LiveData<String>
        get() = _currentTimeCountDown

    val isDarkModeOn: Boolean
        get() = sharedPreferences.isCurrentDarkMode()

    init {
        launchTask {
            examRepository.getListQuestion(
                object : IResponseListener<MutableList<NewQuestion>> {
                    override fun onSuccess(data: MutableList<NewQuestion>) {
                        _listQuestions.value = data
                        hideLoading()
                    }

                    override fun onError(exception: Exception?) {
                        this@ExamViewModel.exception.postValue(exception)
                        hideLoading()
                    }
                }
            )
            hideLoading()
        }
    }

    fun getExamByLicenseType(licenseTypeString: String) {
        viewModelScope.launch {
            val data = examRepository.getAllExamByLicenseType(licenseTypeString)
            _listExam.postValue(data)
        }
    }

    private fun addExamToDatabase(exam: Exam, licenseTypeString: String) {
        launchTask {
            examRepository.insertNewExam(exam)
            val data = examRepository.getAllExamByLicenseType(licenseTypeString)
            _listExam.postValue(data)
            hideLoading()
        }
    }

    fun resetStateCurrentExam(licenseTypeString: String, onComplete: () -> Unit) {
        launchTask {
            val currentExam = _listExam.value?.get(
                _currentExamPosition.value
                    ?: AppConstant.NONE_POSITION
            )
            val examRules = findCreateExamRuleByLicenseTypeString(licenseTypeString)

            currentExam?.let { exam ->
                val newExam = Exam(
                    exam.id,
                    exam.listQuestions,
                    0,
                    timeExamDone = 0,
                    currentTimeStamp = examRules.examDurationByMinutes.toLong().convertMinutesToMillisecond(),
                    listQuestionOptions = generateEmptyQuestionStateList(exam.listQuestions),
                    examType = exam.examType,
                    listExamHistory = exam.listExamHistory,
                )

                examRepository.updateExam(newExam)
            }
            val data = examRepository.getAllExamByLicenseType(licenseTypeString)
            _listExam.postValue(data)
            hideLoading()
            onComplete()
        }
    }

    fun startCountDownEvent(onFinishExamEvent: () -> Unit) {
        val currentExam = _listExam.value?.get(
            _currentExamPosition.value
                ?: AppConstant.NONE_POSITION
        )
        var lastTimeStamp = currentExam?.currentTimeStamp ?: AppConstant.EXAM_TEST_FULL_TIME

        if (lastTimeStamp == AppConstant.DEFAULT_NOT_HAVE_TIME_STAMP) {
            currentExam?.let {
                lastTimeStamp =
                    findCreateExamRuleByLicenseTypeString(it.examType).examDurationByMinutes.toLong()
                        .convertMinutesToMillisecond()
            }
        }

        CountDownInstance.startCountDownFrom(
            lastTimeStamp,
            onTickEvent = {
                _currentTimeCountDown.postValue(CountDownInstance.CurrentTime)
                _listExam.value?.get(
                    _currentExamPosition.value
                        ?: AppConstant.NONE_POSITION
                )?.currentTimeStamp =
                    CountDownInstance.CurrentTimeStamp
            },
            onFinishEvent = {
                processFinishExamEvent(onFinishExamEvent)
            }
        )
    }

    fun processFinishExamEvent(onFinishExamEvent: () -> Unit) {
        launchTask {
            val currentExam = _listExam.value?.get(
                _currentExamPosition.value
                    ?: AppConstant.NONE_POSITION
            )

            CountDownInstance.cancelCountDown()

            currentExam?.let { exam ->
                var index = FIRST_INDEX
                var isWrongTheQuestionThatFailedTestImmediately = false
                val examRules =
                    findCreateExamRuleByLicenseType(sharedPreferences.getCurrentLicenseType())

                exam.timeExamDone = examRules.examDurationByMinutes.toLong().convertMinutesToMillisecond() - exam.currentTimeStamp

                exam.currentTimeStamp = END_TIME_STAMP

                exam.listQuestionOptions.forEach {
                    if (it.position == exam.listQuestions[index].correctAnswerPosition) {
                        exam.listQuestionOptions[index].stateNumber =
                            StateQuestionOption.CORRECT.type
                        exam.numbersOfCorrectAnswer++
                    } else {
                        exam.listQuestionOptions[index].stateNumber =
                            StateQuestionOption.INCORRECT.type

                        if (exam.listQuestions[index].isImmediateFailedTestWhenWrong) {
                            isWrongTheQuestionThatFailedTestImmediately = true
                        }

                        val questionID = exam.listQuestions[index].id
                        val wrongAnswer =
                            wrongAnswerRepository.findWrongAnswerQuestionByID(questionID)

                        if (wrongAnswer != null) {
                            val newWrongAnswerQuestion = wrongAnswer.copy(
                                lastWrongTime = System.currentTimeMillis()
                            )
                            wrongAnswerRepository.updateWrongAnswerQuestion(newWrongAnswerQuestion)
                        } else {
                            wrongAnswerRepository.insertNewWrongAnswerQuestion(
                                WrongAnswer(
                                    questionID,
                                    System.currentTimeMillis(),
                                    provideEmptyQuestionOption(questionID)
                                )
                            )
                        }
                    }
                    index++
                }

                if(isWrongTheQuestionThatFailedTestImmediately) {
                    exam.examState = ExamState.FAILED_BY_MUST_NOT_WRONG_QUESTION.value
                } else if (exam.numbersOfCorrectAnswer < examRules.minimumCorrectToPassExam) {
                    exam.examState = ExamState.FAILED_BY_N0T_ENOUGH_SCORE.value
                } else {
                    exam.examState = ExamState.PASSED.value
                }

                val findTimesDoneExamIndex = exam.listExamHistory.lastOrNull()?.times ?: FIRST_TIME

                val examHistory = ExamHistory(
                    times = findTimesDoneExamIndex + 1,
                    exam.numbersOfCorrectAnswer,
                    exam.listQuestions.size,
                    timeExamDone = System.currentTimeMillis(),
                    examState = exam.examState,
                ).apply {
                    description = when(exam.examState){
                        ExamState.PASSED.value -> "Chúc mừng bạn đã vượt qua bài thi!"
                        ExamState.FAILED_BY_N0T_ENOUGH_SCORE.value ->
                            "Bạn cần ${examRules.minimumCorrectToPassExam - numbersOfCorrectAnswer} câu nữa để vượt qua bài thi này!"
                        ExamState.FAILED_BY_MUST_NOT_WRONG_QUESTION.value ->
                            "Bạn đã sai ít nhất một câu điểm liệt!"
                        else -> ""
                    }
                }

                exam.listExamHistory.add(examHistory)

                examRepository.updateExam(exam)
                delay(DELAY_ON_FINISH_EXAM)
            }
            _currentExamQuestionPosition.postValue(FIRST_INDEX)
            _currentExamPosition.postValue(AppConstant.NONE_POSITION)
            hideLoading()
            onFinishExamEvent()
        }
    }

    fun setCurrentExam(index: Int) {
        _currentExamPosition.value = index
    }

    fun getCurrentExam(): Exam? {
        val currentPosition = _currentExamPosition.value

        currentPosition?.let {
            if (currentPosition != AppConstant.NONE_POSITION) {
                return _listExam.value?.get(currentPosition)
            }
        }

        return null
    }

    fun updateStateQuestion(questionsPosition: Int, item: QuestionOptions) {
        _listExam.value?.get(
            _currentExamPosition.value
                ?: AppConstant.NONE_POSITION
        )?.listQuestionOptions?.set(questionsPosition, item)
        _listExam.postValue(_listExam.value)
    }

    fun navigateToNextQuestion(currentPosition: Int) {
        val currentExamSize = _listExam.value?.get(
            _currentExamPosition.value
                ?: AppConstant.NONE_POSITION
        )?.listQuestionOptions?.size ?: AppConstant.EMPTY_SIZE
        if (currentPosition < currentExamSize) {
            _currentExamQuestionPosition.postValue(currentPosition + 1)
        }
    }

    fun getCurrentExamTimestampLeft() =
        _listExam.value?.get(
            _currentExamPosition.value
                ?: AppConstant.NONE_POSITION
        )?.currentTimeStamp
            ?: AppConstant.DEFAULT_NOT_HAVE_TIME_STAMP

    private fun getCategoryList(questions: List<NewQuestion>, key: String, takeAmount: Int) =
        questions.filter {
            return@filter it.questionType.lowercase() == key.lowercase()
        }.shuffled().take(takeAmount)

    fun saveTemporarilyExamStateWhenStop() {
        viewModelScope.launch {
            if (_currentExamPosition.value != AppConstant.NONE_POSITION) {
                _listExam.value?.get(
                    _currentExamPosition.value
                        ?: AppConstant.NONE_POSITION
                )?.let {
                    examRepository.updateExam(it)
                }
            }
        }
    }

    fun saveCurrentExamState() {
        viewModelScope.launch {
            CountDownInstance.cancelCountDown()
            if (_currentExamPosition.value != AppConstant.NONE_POSITION) {
                _listExam.value?.get(
                    _currentExamPosition.value
                        ?: AppConstant.NONE_POSITION
                )?.let {
                    examRepository.updateExam(it)
                    _currentExamQuestionPosition.postValue(FIRST_INDEX)
                    _currentExamPosition.postValue(AppConstant.NONE_POSITION)
                    _currentExamQuestionPosition.postValue(AppConstant.NONE_POSITION)
                }
            }
        }
    }

    fun createExam(licenseTypeString: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            val currentLicenseType = sharedPreferences.getCurrentLicenseType()
            val examRules = findCreateExamRuleByLicenseType(currentLicenseType)
            val exam = Exam(
                id = ExamFragment.DEFAULT_ID,
                examType = currentLicenseType.type
            )
            val listQuestions = listQuestions.value

            listQuestions?.let { listQuestion ->
                val filteredListQuestion = listQuestion.filter { it.minimumLicenseType in currentLicenseType.getAllLowerQuestionList() }
                exam.listQuestions.apply {
                    enumValues<QuestionType>().forEach {
                        if (it.type != QuestionType.ALL.type) {
                            val numOfQuestion = examRules.numbersOfQuestionByType[it] ?: 0
                            addAll(getCategoryList(filteredListQuestion, it.type, numOfQuestion))
                        }
                    }

                    if(examRules.isMixQuestionInMotorbikeExam) {
                        val mixedList = filteredListQuestion.filter {
                            it.questionType == QuestionType.FIXING_CAR_QUESTION.type ||
                                    it.questionType == QuestionType.DRIVING_TECHNIQUE.type
                        }

                        exam.listQuestions.add(mixedList.take(1)[0])
                    }
                }
                exam.listQuestionOptions.addAll(generateEmptyQuestionStateList(exam.listQuestions))
            }

            val task = async { addExamToDatabase(exam, licenseTypeString) }
            task.await()
            onComplete()
        }
    }

    companion object {
        const val DELAY_ON_FINISH_EXAM = 500L
        const val END_TIME_STAMP = 0L
        const val FIRST_TIME = 0
    }
}
