package com.example.drinkmate

import java.util.*

data class Message(
        var senderUId: String = "",
        var senderUserName: String = "",
        var receiverUId: String = "",
        var content: String = "",
        val timestamp: Date? = null,
) {
        constructor() : this("","", "", "", null)
}


