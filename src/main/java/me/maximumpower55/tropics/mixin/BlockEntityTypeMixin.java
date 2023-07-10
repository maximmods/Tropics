package me.maximumpower55.tropics.mixin;

import java.util.HashSet;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import me.maximumpower55.tropics.init.TBlocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

@Mixin(BlockEntityType.class)
public abstract class BlockEntityTypeMixin {

	@ModifyArg(
		method = "<clinit>",
		at = @At(
			value = "INVOKE",
			target = "net/minecraft/world/level/block/entity/BlockEntityType.register(Ljava/lang/String;Lnet/minecraft/world/level/block/entity/BlockEntityType$Builder;)Lnet/minecraft/world/level/block/entity/BlockEntityType;",
			ordinal = 7
		),
		index = 1
	)
	private static <T extends BlockEntity> BlockEntityType.Builder<T> tropics$registerPalmSignBlockEntity(BlockEntityType.Builder<T> original) {
		final var validBlocks = new HashSet<>(original.validBlocks);
		validBlocks.add(TBlocks.PALM_WOOD.sign);
		validBlocks.add(TBlocks.PALM_WOOD.wallSign);
		return new BlockEntityType.Builder<T>(original.factory, validBlocks);
	}

	@ModifyArg(
		method = "<clinit>",
		at = @At(
			value = "INVOKE",
			target = "net/minecraft/world/level/block/entity/BlockEntityType.register(Ljava/lang/String;Lnet/minecraft/world/level/block/entity/BlockEntityType$Builder;)Lnet/minecraft/world/level/block/entity/BlockEntityType;",
			ordinal = 8
		),
		index = 1
	)
	private static <T extends BlockEntity> BlockEntityType.Builder<T> tropics$registerPalmHangingSignBlockEntity(BlockEntityType.Builder<T> original) {
		final var validBlocks = new HashSet<>(original.validBlocks);
		validBlocks.add(TBlocks.PALM_WOOD.hangingSign);
		validBlocks.add(TBlocks.PALM_WOOD.wallHangingSign);
		return new BlockEntityType.Builder<T>(original.factory, validBlocks);
	}

}
