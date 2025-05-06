package com.example.applearndriver.data.model

enum class CreateExamRules(
    val licenseType: LicenseType,
    val numbersOfQuestionByType: Map<QuestionType, Int>,
    val minimumCorrectToPassExam: Int,
    val totalNumberOfQuestion: Int,
    val examDurationByMinutes: Int,
    val isMixQuestionInMotorbikeExam: Boolean = false
) {
    RULE_A1(
        licenseType = LicenseType.A1,
        numbersOfQuestionByType = mapOf(
            QuestionType.TRAFFIC_CONCEPT_AND_RULES to 9,
            QuestionType.DRIVING_BUSINESS to 0,
            QuestionType.ETHICS_IN_DRIVING to 1,
            QuestionType.DRIVING_TECHNIQUE to 0,
            QuestionType.FIXING_CAR_QUESTION to 0,
            QuestionType.TRAFFIC_SIGNAL to 7,
            QuestionType.TRAFFIC_SITUATION to 7
        ),
        minimumCorrectToPassExam = 21,
        totalNumberOfQuestion = 25,
        examDurationByMinutes = 19,
        isMixQuestionInMotorbikeExam = true
    ),
    RULE_A2(
        licenseType = LicenseType.A2,
        numbersOfQuestionByType = mapOf(
            QuestionType.TRAFFIC_CONCEPT_AND_RULES to 9,
            QuestionType.DRIVING_BUSINESS to 0,
            QuestionType.ETHICS_IN_DRIVING to 1,
            QuestionType.DRIVING_TECHNIQUE to 0,
            QuestionType.FIXING_CAR_QUESTION to 0,
            QuestionType.TRAFFIC_SIGNAL to 7,
            QuestionType.TRAFFIC_SITUATION to 7
        ),
        minimumCorrectToPassExam = 23,
        totalNumberOfQuestion = 25,
        examDurationByMinutes = 19,
        isMixQuestionInMotorbikeExam = true
    ),
    RULE_A3(
        licenseType = LicenseType.A3,
        numbersOfQuestionByType = mapOf(
            QuestionType.TRAFFIC_CONCEPT_AND_RULES to 9,
            QuestionType.DRIVING_BUSINESS to 0,
            QuestionType.ETHICS_IN_DRIVING to 1,
            QuestionType.DRIVING_TECHNIQUE to 0,
            QuestionType.FIXING_CAR_QUESTION to 0,
            QuestionType.TRAFFIC_SIGNAL to 7,
            QuestionType.TRAFFIC_SITUATION to 7
        ),
        minimumCorrectToPassExam = 23,
        totalNumberOfQuestion = 25,
        examDurationByMinutes = 19,
        isMixQuestionInMotorbikeExam = true
    ),
    RULE_A4(
        licenseType = LicenseType.A4,
        numbersOfQuestionByType = mapOf(
            QuestionType.TRAFFIC_CONCEPT_AND_RULES to 9,
            QuestionType.DRIVING_BUSINESS to 0,
            QuestionType.ETHICS_IN_DRIVING to 1,
            QuestionType.DRIVING_TECHNIQUE to 0,
            QuestionType.FIXING_CAR_QUESTION to 0,
            QuestionType.TRAFFIC_SIGNAL to 7,
            QuestionType.TRAFFIC_SITUATION to 7
        ),
        minimumCorrectToPassExam = 23,
        totalNumberOfQuestion = 25,
        examDurationByMinutes = 19,
        isMixQuestionInMotorbikeExam = true
    ),
    RULE_B1(
        licenseType = LicenseType.B1,
        numbersOfQuestionByType = mapOf(
            QuestionType.TRAFFIC_CONCEPT_AND_RULES to 9,
            QuestionType.DRIVING_BUSINESS to 0,
            QuestionType.ETHICS_IN_DRIVING to 1,
            QuestionType.DRIVING_TECHNIQUE to 1,
            QuestionType.FIXING_CAR_QUESTION to 1,
            QuestionType.TRAFFIC_SIGNAL to 9,
            QuestionType.TRAFFIC_SITUATION to 9
        ),
        minimumCorrectToPassExam = 27,
        totalNumberOfQuestion = 30,
        examDurationByMinutes = 20,
    ),
    RULE_B2(
        licenseType = LicenseType.B2,
        numbersOfQuestionByType = mapOf(
            QuestionType.TRAFFIC_CONCEPT_AND_RULES to 10,
            QuestionType.DRIVING_BUSINESS to 1,
            QuestionType.ETHICS_IN_DRIVING to 1,
            QuestionType.DRIVING_TECHNIQUE to 2,
            QuestionType.FIXING_CAR_QUESTION to 1,
            QuestionType.TRAFFIC_SIGNAL to 10,
            QuestionType.TRAFFIC_SITUATION to 10
        ),
        minimumCorrectToPassExam = 32,
        totalNumberOfQuestion = 35,
        examDurationByMinutes = 22,
    ),
    RULE_C(
        licenseType = LicenseType.C,
        numbersOfQuestionByType = mapOf(
            QuestionType.TRAFFIC_CONCEPT_AND_RULES to 10,
            QuestionType.DRIVING_BUSINESS to 1,
            QuestionType.ETHICS_IN_DRIVING to 1,
            QuestionType.DRIVING_TECHNIQUE to 2,
            QuestionType.FIXING_CAR_QUESTION to 1,
            QuestionType.TRAFFIC_SIGNAL to 14,
            QuestionType.TRAFFIC_SITUATION to 11
        ),
        minimumCorrectToPassExam = 36,
        totalNumberOfQuestion = 40,
        examDurationByMinutes = 24,
    ),
    RULE_D(
        licenseType = LicenseType.D,
        numbersOfQuestionByType = mapOf(
            QuestionType.TRAFFIC_CONCEPT_AND_RULES to 10,
            QuestionType.DRIVING_BUSINESS to 1,
            QuestionType.ETHICS_IN_DRIVING to 1,
            QuestionType.DRIVING_TECHNIQUE to 2,
            QuestionType.FIXING_CAR_QUESTION to 1,
            QuestionType.TRAFFIC_SIGNAL to 16,
            QuestionType.TRAFFIC_SITUATION to 14
        ),
        minimumCorrectToPassExam = 41,
        totalNumberOfQuestion = 45,
        examDurationByMinutes = 26,
    ),
    RULE_E(
        licenseType = LicenseType.E,
        numbersOfQuestionByType = mapOf(
            QuestionType.TRAFFIC_CONCEPT_AND_RULES to 10,
            QuestionType.DRIVING_BUSINESS to 1,
            QuestionType.ETHICS_IN_DRIVING to 1,
            QuestionType.DRIVING_TECHNIQUE to 2,
            QuestionType.FIXING_CAR_QUESTION to 1,
            QuestionType.TRAFFIC_SIGNAL to 16,
            QuestionType.TRAFFIC_SITUATION to 14
        ),
        minimumCorrectToPassExam = 41,
        totalNumberOfQuestion = 45,
        examDurationByMinutes = 26,
    ),
    RULE_F(
        licenseType = LicenseType.F,
        numbersOfQuestionByType = mapOf(
            QuestionType.TRAFFIC_CONCEPT_AND_RULES to 10,
            QuestionType.DRIVING_BUSINESS to 1,
            QuestionType.ETHICS_IN_DRIVING to 1,
            QuestionType.DRIVING_TECHNIQUE to 2,
            QuestionType.FIXING_CAR_QUESTION to 1,
            QuestionType.TRAFFIC_SIGNAL to 16,
            QuestionType.TRAFFIC_SITUATION to 14
        ),
        minimumCorrectToPassExam = 41,
        totalNumberOfQuestion = 45,
        examDurationByMinutes = 26,
    ),
}

fun findCreateExamRuleByLicenseType(licenseType: LicenseType) : CreateExamRules {
    return enumValues<CreateExamRules>().first { it.licenseType == licenseType }
}

fun findCreateExamRuleByLicenseTypeString(licenseTypeString: String) : CreateExamRules {
    return enumValues<CreateExamRules>().first { it.licenseType.type == licenseTypeString}
}