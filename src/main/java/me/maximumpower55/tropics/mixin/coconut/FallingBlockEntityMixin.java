package me.maximumpower55.tropics.mixin.coconut;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import me.maximumpower55.tropics.content.block.CoconutBlock;
import me.maximumpower55.tropics.duck.TropicsFallingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin implements TropicsFallingBlockEntity {

	private boolean tropics$shouldCrack = false;

	@Override
	public void tropics$setShouldCrack(boolean b) {
		tropics$shouldCrack = b;
	}

	@Shadow
	private BlockState blockState;

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "net/minecraft/world/level/block/state/BlockState.canSurvive(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z"))
	private boolean shouldBreakCoconut(BlockState self, LevelReader level, BlockPos pos) {
		if (blockState.getBlock() instanceof CoconutBlock coconut) {
			return !coconut.shouldBreak(level, pos);
		}

		return self.canSurvive(level, pos);
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "net/minecraft/world/entity/item/FallingBlockEntity.spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"))
	private ItemEntity shouldDropCrackedCoconuts(FallingBlockEntity self, ItemLike item) {
		BlockState state = self.getBlockState();

		if (tropics$shouldCrack && state.getBlock() instanceof CoconutBlock coconut) {
			return self.spawnAtLocation(coconut.getLoot(state));
		}

		return self.spawnAtLocation(item);
	}

}
