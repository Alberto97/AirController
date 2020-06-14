package org.alberto97.hisenseair.models

import org.alberto97.hisenseair.features.WorkMode

class AppDevice(val dsn: String, val name: String, var temp: String, var isPower: Boolean, var mode: WorkMode)