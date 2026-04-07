package dev.emi.emi.handler;

import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SelectableRecipe;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import dev.emi.emi.EmiPort;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.recipe.handler.EmiCraftContext;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;

public class StonecuttingRecipeHandler implements StandardRecipeHandler<StonecutterMenu> {

	@Override
	public List<Slot> getInputSources(StonecutterMenu handler) {
		List<Slot> list = Lists.newArrayList();
		list.add(handler.getSlot(0));
		int invStart = 2;
		for (int i = invStart; i < invStart + 36; i++) { 
			list.add(handler.getSlot(i));
		}
		return list;
	}

	@Override
	public List<Slot> getCraftingSlots(StonecutterMenu handler) {
		return List.of(handler.slots.get(0));
	}

	@Override
	public boolean supportsRecipe(EmiRecipe recipe) {
		return recipe.getCategory() == VanillaEmiRecipeCategories.STONECUTTING;
	}

	@Override
	public @Nullable Slot getOutputSlot(StonecutterMenu handler) {
		return handler.getSlot(1);
	}

	@Override
	public boolean craft(EmiRecipe recipe, EmiCraftContext<StonecutterMenu> context) {
		boolean action = StandardRecipeHandler.super.craft(recipe, context);
		Minecraft client = Minecraft.getInstance();
		StonecutterMenu sh = context.getScreenHandler();
		List<SelectableRecipe.SingleInputEntry<StonecutterRecipe>> entries = sh.getVisibleRecipes().entries();
		for (int i = 0; i < entries.size(); i++) {
			Optional<RecipeHolder<StonecutterRecipe>> optHolder = entries.get(i).recipe().recipe();
			if (optHolder.isPresent()) {
				StonecutterRecipe rec = optHolder.get().value();
				if (EmiPort.getId(rec) != null && EmiPort.getId(rec).equals(recipe.getId())) {
					client.gameMode.handleInventoryButtonClick(sh.containerId, i);
					if (context.getDestination() == EmiCraftContext.Destination.CURSOR) {
						client.gameMode.handleContainerInput(sh.containerId, 1, 0, ContainerInput.PICKUP, client.player);
					} else if (context.getDestination() == EmiCraftContext.Destination.INVENTORY) {
						client.gameMode.handleContainerInput(sh.containerId, 1, 0, ContainerInput.QUICK_MOVE, client.player);
					}
					break;
				}
			}
		}
		return action;
	}
}
