package com.example.smackchat.model

class channel (val name: String, val description: String, val id: String){
    override fun toString(): String {
        return "#$name"
    }
}