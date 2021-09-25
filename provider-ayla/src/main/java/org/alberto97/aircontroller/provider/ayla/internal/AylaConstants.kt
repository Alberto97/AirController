package org.alberto97.aircontroller.provider.ayla.internal

import org.alberto97.aircontroller.common.models.AppDeviceState

@Suppress("unused")
internal object AylaDeviceState {
    const val ONLINE = "Online"
    const val OFFLINE = "Offline"
}

internal object AylaType {
    const val BOOLEAN = "boolean"
    const val DECIMAL = "decimal"
    const val INTEGER = "integer"
    const val STRING = "string"
}

internal val AylaPropertyToStateMap = mapOf(
    BACKLIGHT_PROP to AppDeviceState::backlight,
    WORK_MODE_PROP to AppDeviceState::workMode,
    HORIZONTAL_AIR_FLOW_PROP to AppDeviceState::horizontalAirFlow,
    VERTICAL_AIR_FLOW_PROP to AppDeviceState::verticalAirFlow,
    ECO_PROP to AppDeviceState::eco,
    QUIET_PROP to AppDeviceState::quiet,
    BOOST_PROP to AppDeviceState::boost,
    SLEEP_MODE_PROP to AppDeviceState::sleepMode,
    FAN_SPEED_PROP to AppDeviceState::fanSpeed,
    TEMP_PROP to AppDeviceState::temp,
    ROOM_TEMP_PROP to AppDeviceState::roomTemp,
    TEMP_TYPE_PROP to AppDeviceState::tempUnit,
    POWER_PROP to AppDeviceState::on
)