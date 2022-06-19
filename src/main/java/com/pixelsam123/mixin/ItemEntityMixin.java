package com.pixelsam123.mixin;

import net.minecraft.entity.ItemEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
	// donated by jsq (note to self: go to "View > Show Bytecode" for the reasoning)
	@ModifyConstant(
		method = "tick",
		constant = @Constant(intValue = 6000),
		slice = @Slice(
			from = @At(
				value = "FIELD",
				target = "Lnet/minecraft/entity/ItemEntity;itemAge:I",
				ordinal = 2,
				opcode = Opcodes.GETFIELD,
				shift = At.Shift.AFTER
			),
			to = @At(
				value = "JUMP",
				opcode = Opcodes.IF_ICMPLT,
				shift = At.Shift.BEFORE
			)
		)
	)
	public int changeDespawnTimeInTickMethod(int constant) {
		return 100;
	}

	@ModifyConstant(method = "canMerge()Z", constant = @Constant(intValue = 6000))
	public int changeDespawnTimeInCanMergeMethod(int constant) {
		return 100;
	}
}
