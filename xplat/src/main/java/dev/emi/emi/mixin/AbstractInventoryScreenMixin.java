package dev.emi.emi.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.EffectsInInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.emi.emi.config.EffectLocation;
import dev.emi.emi.config.EmiConfig;

@Mixin(EffectsInInventory.class)
public class AbstractInventoryScreenMixin {

	@Inject(at = @At("HEAD"), method = "extractRenderState", cancellable = true)
	private void emi$hideEffects(GuiGraphicsExtractor draw, int mouseX, int mouseY, CallbackInfo info) {
		if (EmiConfig.effectLocation == EffectLocation.HIDDEN) {
			info.cancel();
		}
	}
}
