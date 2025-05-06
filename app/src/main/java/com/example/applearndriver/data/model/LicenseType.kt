package com.example.applearndriver.data.model

data class LicenseTypeData(
    val licenseType: LicenseType,
    val isSelected: Boolean = false,
)

// question priority đẻ đánh giá độ ưu tiên lấy câu hỏi
// Ví dụ priority = 2 cho A2 sẽ lấy tất cả các câu hỏi priority <= 2
// (tức là lấy câu hỏi của A1, A2)

enum class LicenseType(
    val type: String,
    val description: String,
    val questionPriority: Int,
) {
    A1(
        type = "A1",
        description = "Người lái xe mô tô hai bánh có dung tích xi-lanh từ 50cm3 đến dưới 175cm3;\n" +
                "Người khuyết tật điều khiển xe mô tô ba bánh dùng cho người khuyết tật.",
        questionPriority = 1
    ),
    A2(
        type = "A2",
        description = "Người lái xe mô tô hai bánh có dung tích xi-lanh từ 175cm3 trở lên" +
                " và các loại xe quy định cho giấy phép lái xe hạng A1.",
        questionPriority = 2
    ),
    A3(
        type = "A3",
        description = "Người lái xe mô tô ba bánh, các loại xe quy định cho " +
                "giấy phép lái xe hạng A1 và các xe tương tự.",
        questionPriority = 3
    ),
    A4(
        type = "A4",
        description = "Người lái máy kéo có trọng tải đến 1.000 kg.",
        questionPriority = 3, //Priority giống A3 vì lượng câu hỏi như nhau
    ),
    B1(
        type = "B1",
        description = "Người không hành nghề lái xe để điều khiển:\n" +
                "- Ô tô số tự động chở người đến 9 chỗ ngồi, kể cả chỗ cho người lái xe;\n" +
                "- Ô tô tải, kể cả ô tô tải chuyên dùng số tự động có trọng tải thiết kế dưới 3.500 kg;\n" +
                "- Ô tô dùng cho người khuyết tật.",
        questionPriority = 4,
    ),
    B2(
        type = "B2",
        description = "Người hành nghề lái xe để điều khiển:\n" +
                "- Ô tô chuyên dùng có trọng tải thiết kế dưới 3.500kg;\n" +
                "- Các loại xe quy định cho giấy phép lái xe hạng B1.",
        questionPriority = 5, //Priority cao nhất
    ),
    C(
        type = "C",
        description = "Người lái xe để điều khiển:\n" +
                "- Ô tô tải, kể cả ô tô tải chuyên dùng, ô tô chuyên dùng có trọng tải thiết kế từ 3.500kg trở lên;\n" +
                "- Máy kéo kéo một rơ moóc có trọng tải thiết kế từ 3.500kg trở lên;\n" +
                "- Các loại xe quy định cho giấy phép lái xe hạng B1, B2.",
        questionPriority = 5, //Priority cao nhất
    ),
    D(
        type = "D",
        description = "Người lái xe để điều khiển:\n" +
                "- Ô tô chở người từ 10 đến 30 chỗ ngồi, kể cả chỗ ngồi cho người lái xe;\n" +
                "- Các loại xe quy định cho giấy phép lái xe hạng B1, B2 và C.",
        questionPriority = 5, //Priority cao nhất
    ),
    E(
        type = "E",
        description = "Người lái xe để điều khiển:\n" +
                "- Ô tô chở người trên 30 chỗ ngồi;\n" +
                "- Các loại xe quy định cho giấy phép lái xe hạng B1, B2, C và D.",
        questionPriority = 5, //Priority cao nhất
    ),
    F(
        type = "F",
        description = "Người lái xe ô tô để lái các loại xe có kéo rơ moóc, ô tô đầu kéo kéo sơ mi rơ moóc",
        questionPriority = 5, //Priority cao nhất
    ),
}

fun getAllMotorbikeLicenseType()
    = listOf(LicenseType.A1, LicenseType.A2, LicenseType.A3, LicenseType.A4)

fun LicenseType.getAllLowerQuestionList(): List<String> {
    return when (this.questionPriority) {
        1 -> listOf(LicenseType.A1.type)
        2 -> listOf(LicenseType.A1.type, LicenseType.A2.type)
        3 -> listOf(LicenseType.A1.type, LicenseType.A2.type, LicenseType.A4.type)
        4 -> listOf(
            LicenseType.A1.type,
            LicenseType.A2.type,
            LicenseType.A4.type,
            LicenseType.B1.type
        )
        5 -> listOf(
            LicenseType.A1.type,
            LicenseType.A2.type,
            LicenseType.A4.type,
            LicenseType.B1.type,
            LicenseType.B2.type
        )
        else -> listOf(LicenseType.A1.type)
    }
}