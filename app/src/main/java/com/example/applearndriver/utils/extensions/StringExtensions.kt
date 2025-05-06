package com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions

fun String?.processEndline(): String {
    if (this == null) {
        return ""
    }
    return this.replace("\\n", System.getProperty("line.separator"))
}

fun String?.processDoubleQuotes(): String {
    if (this == null) {
        return ""
    }
    return this.replace("\\\"", "\"")
}

fun String.processExplainQuestion() = this.ifBlank { "Không có giải thích" }
