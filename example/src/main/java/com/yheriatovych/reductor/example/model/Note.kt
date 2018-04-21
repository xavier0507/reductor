package com.yheriatovych.reductor.example.model

data class Note(val id: Int, val note: String, val checked: Boolean) {
    override fun toString(): String {
        return String.format("[%s] %s", if (checked) "+" else " ", note)
    }
}
