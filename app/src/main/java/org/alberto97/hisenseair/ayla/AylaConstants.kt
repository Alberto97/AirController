package org.alberto97.hisenseair.ayla

import org.alberto97.hisenseair.features.*
import org.alberto97.hisenseair.models.AppDeviceState

val AylaPropertyToStateMap = mapOf(
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