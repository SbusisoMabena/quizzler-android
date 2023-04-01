package com.example.quizzlerandroid.game.data

import com.google.gson.annotations.SerializedName
import org.apache.commons.lang3.StringEscapeUtils

class Quiz {
    @SerializedName("question")
    var question: String = ""
        get() = StringEscapeUtils.unescapeHtml4(field)

    @SerializedName("correct_answer")
    var isAnswer = false
}