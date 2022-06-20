package com.pixelsam123.mixin;

import com.pixelsam123.Modespawner;
import net.minecraft.entity.ItemEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
	// Thanks jsq (check "View > Show Bytecode")
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
		return Modespawner.despawnTime;
	}

	@ModifyConstant(
		method = "canMerge()Z",
		constant = @Constant(intValue = 6000),
		slice = @Slice(
			from = @At(
				value = "FIELD",
				target = "Lnet/minecraft/entity/ItemEntity;itemAge:I",
				ordinal = 1,
				opcode = Opcodes.GETFIELD,
				shift = At.Shift.AFTER
			),
			to = @At(
				value = "JUMP",
				opcode = Opcodes.IF_ICMPGE,
				shift = At.Shift.BEFORE
			)
		)
	)
	public int changeDespawnTimeInCanMergeMethod(int constant) {
		return Modespawner.despawnTime;
	}

	@ModifyConstant(method = "setDespawnImmediately", constant = @Constant(intValue = 5999))
	public int changeTimeInSetDespawnImmediatelyMethod(int constant) {
		return Modespawner.despawnTime - 1;
	}
}
