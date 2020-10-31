package io.github.kerubistan.kroki.flyweight.annotations

/**
 * Tells the flyweight runtime library to leave this property as is and
 * not try to deduplicate.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class IgnoreFlyWeight
