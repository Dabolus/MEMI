package dev.emi.emi.mixin.accessor;

import net.minecraft.client.resources.model.ModelManager;
import org.spongepowered.asm.mixin.Mixin;

// BakedModel removed in MC 26.1 - ModelManager no longer has a models map
@Mixin(ModelManager.class)
public interface BakedModelManagerAccessor {
}
