package com.pixelsam123;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class MixinFunctions {
	public static int getGlobalDespawnTimeFromConfig() {
		return Modespawner.GLOBAL_DESPAWN_TIME.getRealValue();
	}

	public static int getGlobalDespawnImmediatelyTimeFromConfig() {
		return Modespawner.GLOBAL_DESPAWN_TIME.getRealValue() - 1;
	}

	public static ItemEntity dropItemAndModifyItsAge(
		PlayerEntity player,
		ItemStack stack,
		boolean throwRandomly,
		boolean retainOwnership
	) {
		final ItemEntity droppedItem = player.dropItem(stack, throwRandomly, retainOwnership);

		if (droppedItem != null) {
			((ItemAgeSettable) droppedItem).setItemAge(Modespawner.GLOBAL_INITIAL_DEATH_ITEM_AGE.getRealValue());
		}

		return droppedItem;
	}
}
