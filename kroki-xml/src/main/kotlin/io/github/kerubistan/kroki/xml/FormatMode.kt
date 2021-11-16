package io.github.kerubistan.kroki.xml

/**
 * XML format modes.
 */
enum class FormatMode {
	/**
	 * No indentation, no line-breaks added.
	 */
	COMPACT,

	/**
	 * Indented using tabs.
	 */
	PRETTY_TABS,

	/**
	 * Indented using four spaces.
	 */
	PRETTY_BIG_SPACE_NAZI,

	/**
	 * Indented using 2 spaces.
	 */
	PRETTY_SMALL_SPACE_NAZI,
}