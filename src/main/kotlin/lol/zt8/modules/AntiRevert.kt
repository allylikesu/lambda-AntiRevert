package lol.zt8.modules

import akka.io.Tcp.Message
import com.lambda.client.event.events.WindowClickEvent
import com.lambda.client.event.listener.listener
import com.lambda.client.module.Category
import com.lambda.client.plugin.api.PluginModule
import com.lambda.client.util.items.id
import com.lambda.client.util.text.MessageSendHelper
import lol.zt8.AntiRevertPlugin
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.inventory.Slot
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList


/**
 * This is a module. First set properties then settings then add listener.
 * **/
internal object AntiRevert : PluginModule(
    name = "AntiRevert",
    category = Category.PLAYER,
    description = "Prevents inventory interactions with superillegals and overstacked items",
    pluginMain = AntiRevertPlugin
) {
    private var illegals = arrayOf(7, 52, 120, 137, 166, 210, 211, 217, 255, 383, 422)

    init {
        listener<WindowClickEvent> {
            var stack : ItemStack?
            try {
                stack = (mc.currentScreen as GuiContainer).slotUnderMouse?.stack
            } catch (e: Exception) {
                return@listener
            }
            if (stack != null) {
                if (stack.isIllegal()) {
                    it.cancel()
                    MessageSendHelper.sendChatMessage("Oops! you almost made a mistake!")
                }
            }
        }
    }

    private fun Slot.isIllegal(): Boolean {
        return this.stack.isIllegal()
    }
    private fun ItemStack.isIllegal(): Boolean {
        // Stack size check
        if (this.count > this.maxStackSize) {
            return true
        }

        // Over-enchanted check
        val enchants = EnchantmentHelper.getEnchantments(this)
        for (enchant in enchants.keys) {
            if (enchants.get(enchant)!! > enchant.maxLevel) { return true }
        }

        val compound = this.tagCompound
        if (compound != null) {
            // Unbreakable check
            if (compound.hasKey("Unbreakable")) { return true }
            // Shulker contents check
            if (compound.hasKey("BlockEntityTag", 10)) {
                val tags = compound.getCompoundTag("BlockEntityTag")
                if (tags.hasKey("Items", 9)) {
                    val contents = NonNullList.withSize(27, ItemStack.EMPTY)
                    ItemStackHelper.loadAllItems(tags, contents)
                    for (item in contents) {
                        if (item.isIllegal()) { return true }
                    }
                }
            }
        }

        return this.item.isIllegal()
    }
    private fun Item.isIllegal(): Boolean {
        // Superillegal check
        if (this.id in illegals) {
            return true
        }
        return false
    }
}