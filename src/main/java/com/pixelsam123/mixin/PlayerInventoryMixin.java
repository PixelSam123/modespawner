package com.pixelsam123.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.pixelsam123.MixinFunctions.dropItemAndModifyItsAge;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {
	@Redirect(
		method = "dropAll",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/entity/player/PlayerEntity;dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;"
		)
	)
	public ItemEntity changeDropAllItemAge(
		PlayerEntity player,
		ItemStack stack,
		boolean throwRandomly,
		boolean retainOwnership
	) {
		return dropItemAndModifyItsAge(player, stack, throwRandomly, retainOwnership);
	}
}
