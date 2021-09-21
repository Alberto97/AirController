package org.alberto97.hisenseair.common.repositories

import org.alberto97.hisenseair.common.enums.Provider
import org.alberto97.hisenseair.common.enums.Region

interface ISettingsRepository {
    var region: Region?
    var loggedIn: Boolean
    var provider: Provider?
}