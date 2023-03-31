package com.example.quizzlerandroid

import com.example.quizzlerandroid.game.data.Quiz
import com.google.gson.annotations.SerializedName

class Data {
    @SerializedName("results")
    var data: List<Quiz>? = null
}