package lol.zt8.mixins;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiContainer.class)
public interface GuiContainerMixin {
   @Accessor("draggedStack")
   ItemStack getDraggedStack();
}
