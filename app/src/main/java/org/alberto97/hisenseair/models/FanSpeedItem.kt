package org.alberto97.hisenseair.models

import org.alberto97.hisenseair.features.FanSpeed

class FanSpeedItem(override val id: FanSpeed,
                   val name: String,
                   override var current: Boolean = false): ListItemBase<FanSpeed>()