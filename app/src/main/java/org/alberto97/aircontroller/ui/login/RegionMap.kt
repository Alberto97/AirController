package org.alberto97.aircontroller.ui.login

import org.alberto97.aircontroller.R
import org.alberto97.aircontroller.common.enums.Region
import org.alberto97.aircontroller.models.RegionData

val regionMap = mapOf(
    Region.EU to RegionData(R.string.region_eu, R.drawable.ic_eu),
    Region.US to RegionData(R.string.region_us, R.drawable.ic_us)
)
