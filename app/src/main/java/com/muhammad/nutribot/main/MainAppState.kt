package com.muhammad.nutribot.main

data class MainAppState(
    val isUserLoggedIn : Boolean?=null,
    val isCheckingLogin : Boolean = true
)