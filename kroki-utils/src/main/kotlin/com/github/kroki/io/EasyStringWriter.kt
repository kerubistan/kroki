package com.github.kroki.io

import java.io.Writer

class EasyStringWriter(preAllocatedSize : Int = 64) : Writer() {

    private val builder = StringBuilder(preAllocatedSize)

    override fun append(ch: Char): Writer {
        builder.append(ch)
        return this
    }

    override fun write(str: String?) {
        builder.append(str)
    }

    override fun write(str: String?, from: Int, to: Int) {
        builder.append(str, from, to)
    }

    override fun write(data: CharArray, from: Int, to: Int) {
        builder.append(data, from, to)
    }

    override fun flush() {}

    override fun close() {}

    override fun toString() = builder.toString()
}