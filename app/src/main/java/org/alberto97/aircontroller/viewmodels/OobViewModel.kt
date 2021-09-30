package org.alberto97.aircontroller.viewmodels

import androidx.lifecycle.ViewModel
import org.alberto97.aircontroller.common.repositories.ISettingsRepository

class OobViewModel(repo: ISettingsRepository) : ViewModel() {
    init {
        repo.oob = false
    }
}