package com.example.kubaattendance.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Coroutines {

    fun main(work: suspend (() -> Unit)) = //unit same as java void
        CoroutineScope(Dispatchers.Main).launch {
            work()
        }


    fun io(work: suspend (() -> Unit)) = //unit same as java void
        CoroutineScope(Dispatchers.IO).launch {
            work()
        }
}