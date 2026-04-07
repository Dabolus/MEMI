package dev.emi.emi.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;

// BakedModel, ItemRenderer and renderBakedItemModel removed in MC 26.1
@Mixin(targets = "net.minecraft.client.renderer.entity.ItemRenderer")
public interface ItemRendererAccessor {
}
