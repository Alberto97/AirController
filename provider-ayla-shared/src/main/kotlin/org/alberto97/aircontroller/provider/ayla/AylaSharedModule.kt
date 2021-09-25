package org.alberto97.aircontroller.provider.ayla

import org.alberto97.aircontroller.provider.ayla.features.controllers.*
import org.alberto97.aircontroller.provider.features.controllers.*
import org.koin.dsl.module

/**
 * Ayla controllers are defined in a separated module to be able to share them with the demo provider
 */
val aylaControllers = module {
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
    single<ISupportedFanSpeedController> { SupportedFanSpeedController() }
    single<ISupportedModesController> { SupportedModesController() }
    single<ISupportedSleepModesController> { SupportedSleepModesController() }
    single<ITempControlController> { TempControlController() }
}