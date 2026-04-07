package dev.emi.emi.recipe.special;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import com.google.common.collect.Lists;

import dev.emi.emi.api.recipe.EmiPatternCraftingRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.GeneratedSlotWidget;
import dev.emi.emi.api.widget.SlotWidget;

public class EmiArmorDyeRecipe extends EmiPatternCraftingRecipe {
	private static final DyeColor[] COLORS = DyeColor.values();
	private final Item armor;

	public EmiArmorDyeRecipe(Item armor, Identifier id) {
		super(List.of(
			EmiIngredient.of(Stream.of(COLORS).map(c -> (EmiIngredient) EmiStack.of(dyeItem(c))).collect(Collectors.toList())),
			EmiStack.of(armor)), EmiStack.of(armor), id);
		this.armor = armor;
	}

	@Override
	public SlotWidget getInputWidget(int slot, int x, int y) {
		if (slot == 0) {
			return new SlotWidget(EmiStack.of(armor), x, y);
		} else {
			final int s = slot - 1;
			return new GeneratedSlotWidget(r -> {
				List<DyeColor> dyes = getDyeColors(r);
				if (s < dyes.size()) {
					return EmiStack.of(dyeItem(dyes.get(s)));
				}
				return EmiStack.EMPTY;
			}, unique, x, y);
		}
	}

	@Override
	public SlotWidget getOutputWidget(int x, int y) {
		return new GeneratedSlotWidget(r -> {
			return EmiStack.of(DyedItemColor.applyDyes(new ItemStack(armor), getDyeColors(r)));
		}, unique, x, y);
	}
	
	private List<DyeColor> getDyeColors(Random random) {
		List<DyeColor> dyes = Lists.newArrayList();
		int amount = 1 + random.nextInt(8);
		for (int i = 0; i < amount; i++) {
			dyes.add(COLORS[random.nextInt(COLORS.length)]);
		}
		return dyes;
	}

	static Item dyeItem(DyeColor color) {
		return switch (color) {
			case WHITE -> Items.WHITE_DYE;
			case ORANGE -> Items.ORANGE_DYE;
			case MAGENTA -> Items.MAGENTA_DYE;
			case LIGHT_BLUE -> Items.LIGHT_BLUE_DYE;
			case YELLOW -> Items.YELLOW_DYE;
			case LIME -> Items.LIME_DYE;
			case PINK -> Items.PINK_DYE;
			case GRAY -> Items.GRAY_DYE;
			case LIGHT_GRAY -> Items.LIGHT_GRAY_DYE;
			case CYAN -> Items.CYAN_DYE;
			case PURPLE -> Items.PURPLE_DYE;
			case BLUE -> Items.BLUE_DYE;
			case BROWN -> Items.BROWN_DYE;
			case GREEN -> Items.GREEN_DYE;
			case RED -> Items.RED_DYE;
			case BLACK -> Items.BLACK_DYE;
		};
	}
}
