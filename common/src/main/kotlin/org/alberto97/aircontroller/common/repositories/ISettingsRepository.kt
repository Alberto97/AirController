package org.alberto97.aircontroller.common.repositories

import org.alberto97.aircontroller.common.enums.Provider
import org.alberto97.aircontroller.common.enums.Region

interface ISettingsRepository {
    var oob: Boolean
    var region: Region?
    var loggedIn: Boolean
    var provider: Provider?
}