package com.github.kroki.exceptions

import com.github.kroki.io.EasyStringWriter
import java.io.PrintWriter

fun Throwable.getStackTraceAsString(): String =
    EasyStringWriter(256).use { stringWriter ->
        PrintWriter(stringWriter).use {
            this.printStackTrace(it)
        }
        stringWriter.toString()
    }
