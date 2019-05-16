package com.github.kroki.numbers

import java.math.BigDecimal
import java.math.BigInteger

operator fun BigDecimal.times(nr : BigInteger) = this.times(nr.toBigDecimal())
