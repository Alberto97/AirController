package org.alberto97.hisenseair.features

val TempTypeMap = mapOf(
    true to TempType.Fahrenheit,
    false to TempType.Celsius
)

enum class TempType(val value: Int) {
    Celsius(0),
    Fahrenheit(1);

    companion object {
        private val map = values().associateBy(TempType::value)
        fun from(type: Int) = map.getValue(type)
    }
}