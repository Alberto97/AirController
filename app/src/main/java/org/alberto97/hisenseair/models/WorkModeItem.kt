package org.alberto97.hisenseair.models

import org.alberto97.hisenseair.features.WorkMode

class WorkModeItem(override val id: WorkMode,
                   val icon: Int,
                   val name: String,
                   val tint: Int,
                   override var current: Boolean = false): ListItemBase<WorkMode>()