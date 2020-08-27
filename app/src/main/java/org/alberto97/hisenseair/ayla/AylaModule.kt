package org.alberto97.hisenseair.ayla

import org.alberto97.hisenseair.ayla.features.controllers.*
import org.alberto97.hisenseair.features.controllers.*
import org.koin.dsl.module

val aylaModule = module {
    single<IAirFlowHorizontalController> { AirFlowHorizontalController() }
    single<IAirFlowVerticalController> { AirFlowVerticalController() }
    single<IBacklightController> { BacklightController() }
    single<IBoostController> { BoostController() }
    single<IEcoController> { EcoController() }
    single<IFanSpeedController> { FanSpeedController() }
    single<IMaxTempController> { MaxTempController() }
    single<IMinTempController> { MinTempController() }
    single<IModeController> { ModeController() }
    single<IPowerController> { PowerController() }
    single<IQuietController> { QuietController() }
    single<IRoomTempController> { RoomTempController() }
    single<ISleepModeController> { SleepModeController() }
    single<ITempControlController> { TempControlController() }
}