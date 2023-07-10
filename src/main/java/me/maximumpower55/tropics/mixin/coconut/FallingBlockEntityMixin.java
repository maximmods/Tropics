package me.maximumpower55.tropics.mixin.coconut;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;

import me.maximumpower55.tropics.content.block.CoconutBlock;
import me.maximumpower55.tropics.duck.FallingBlockEntityExtensions;
import me.maximumpower55.tropics.init.TItems;
import me.maximumpower55.tropics.init.TSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
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
	public void tropics$crack() {
		tropics$shouldCrack = true;
	}

	@Shadow
	private BlockState blockState;

	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "net/minecraft/world/level/block/state/BlockState.canSurvive(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z"))
	private boolean tropics$shouldBreakCoconut(boolean original) {
		if (blockState.getBlock() instanceof CoconutBlock coconut) {
			return !coconut.shouldBreak(level(), blockPosition());
		}
		return original;
	}

	@WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "net/minecraft/world/entity/item/FallingBlockEntity.spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"))
	private boolean tropics$customLoot(FallingBlockEntity fallingBlockEntity, ItemLike item) {
		if (blockState.getBlock() instanceof CoconutBlock coconut && tropics$shouldCrack) {
			spawnAtLocation(new ItemStack(TItems.CRACKED_COCONUT, 2));
			return false;
		}
		return true;
	}

	@Inject(
		method = "lambda$causeFallDamage$0(Lnet/minecraft/world/damagesource/DamageSource;FLnet/minecraft/world/entity/Entity;)V",
		at = @At(value = "INVOKE", target = "net/minecraft/world/entity/Entity.hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", shift = At.Shift.AFTER)
	)
	private static void tropics$playBonkSound(DamageSource damageSource, float damage, Entity entity, CallbackInfo ci) {
		entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), TSounds.BONK, SoundSource.BLOCKS, 1.5f, 1);
	}

}
