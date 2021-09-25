package org.alberto97.aircontroller.koin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

/*
 * From https://github.com/InsertKoinIO/koin/issues/1079#issuecomment-902215765
 */
@Composable
inline fun <reified VM : ViewModel> getNavGraphViewModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
    backStackEntry: NavBackStackEntry
): VM {
    return remember(qualifier,parameters) {
        backStackEntry.getViewModel(qualifier, parameters)
    }
}