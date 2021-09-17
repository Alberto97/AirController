package org.alberto97.hisenseair.demo

import org.koin.core.context.loadKoinModules


interface IDemoModuleLoader {
    fun load()
}

class DemoModuleLoader : IDemoModuleLoader {

    override fun load() {
        loadKoinModules(demoModule)
    }
}