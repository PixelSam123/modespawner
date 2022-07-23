package com.pixelsam123.mixin;

import com.pixelsam123.ItemAgeSettable;
import net.minecraft.entity.ItemEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;

import static com.pixelsam123.MixinFunctions.getGlobalDespawnImmediatelyTimeFromConfig;
import static com.pixelsam123.MixinFunctions.getGlobalDespawnTimeFromConfig;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin implements ItemAgeSettable {
	@Shadow
	private int itemAge;

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
		return getGlobalDespawnTimeFromConfig();
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
		return getGlobalDespawnTimeFromConfig();
	}

	@ModifyConstant(method = "setDespawnImmediately", constant = @Constant(intValue = 5999))
	public int changeTimeInSetDespawnImmediatelyMethod(int constant) {
		return getGlobalDespawnImmediatelyTimeFromConfig();
	}

	@Override
	public void setItemAge(int itemAge) {
		this.itemAge = itemAge;
	}
}
