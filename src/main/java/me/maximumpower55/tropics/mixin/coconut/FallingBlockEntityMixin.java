package me.maximumpower55.tropics.mixin.coconut;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;

import me.maximumpower55.tropics.accessor.FallingBlockEntityExtensions;
import me.maximumpower55.tropics.content.block.CoconutBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity implements FallingBlockEntityExtensions {

	private FallingBlockEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	private boolean tropics$shouldCrack = false;

	@Override
	public void tropics$setShouldCrack(boolean b) {
		tropics$shouldCrack = b;
	}

	@Shadow
	private BlockState blockState;

	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "net/minecraft/world/level/block/state/BlockState.canSurvive(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z"))
	private boolean tropics$shouldBreakCoconut(boolean original) {
		if (blockState.getBlock() instanceof CoconutBlock coconut) {
			return !coconut.shouldBreak(level, blockPosition());
		}

		return original;
	}

	@WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "net/minecraft/world/entity/item/FallingBlockEntity.spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"))
	private boolean tropics$dropCrackedCoconuts(FallingBlockEntity self, ItemLike item) {
		if (tropics$shouldCrack && blockState.getBlock() instanceof CoconutBlock coconut) {
			spawnAtLocation(coconut.getLoot(blockState));
			return false;
		}

		return true;
	}

}
