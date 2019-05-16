package io.github.kerubistan.kroki.exceptions

import io.github.kerubistan.kroki.io.EasyStringWriter
import java.io.PrintWriter

fun Throwable.getStackTraceAsString(): String =
    EasyStringWriter(256).use { stringWriter ->
        PrintWriter(stringWriter).use {
            this.printStackTrace(it)
        }
        stringWriter.toString()
    }
