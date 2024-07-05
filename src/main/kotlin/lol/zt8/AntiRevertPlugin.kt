package lol.zt8

import com.lambda.client.plugin.api.Plugin

import lol.zt8.modules.AntiRevert

internal object AntiRevertPlugin : Plugin() {

    override fun onLoad() {
        // Load any modules, commands, or HUD elements here
        modules.add(AntiRevert)
    }

    override fun onUnload() {
        // Here you can unregister threads etc...
    }
}