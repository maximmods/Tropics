package me.maximumpower55.tropics.mixin.coconut;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.maximumpower55.tropics.init.TTags;
import me.maximumpower55.tropics.mechanics.CoconutDamageSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	private LivingEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow
	abstract ItemStack getItemBySlot(EquipmentSlot slot);

	@Shadow
	abstract void hurtHelmet(DamageSource damageSource, float damage);

	@Shadow
	abstract boolean addEffect(MobEffectInstance effectInstance);

	@Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
	private void tropics$tryBlockCoconutDamage(DamageSource damageSource, float damage, CallbackInfoReturnable<Boolean> cir) {
		if (damageSource instanceof CoconutDamageSource && getItemBySlot(EquipmentSlot.HEAD).is(TTags.Item.PREVENTS_COCONUT_DAMAGE)) {
			hurtHelmet(damageSource, damage);
			addBonkEffect();
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "hurt", at = @At(value = "INVOKE", target = "net/minecraft/world/entity/LivingEntity.markHurt()V"))
	private void tropics$doBonkEffectsWhenHurt(DamageSource damageSource, float damage, CallbackInfoReturnable<Boolean> cir) {
		if (damageSource instanceof CoconutDamageSource) addBonkEffect();
	}

	private void addBonkEffect() {
		var level = level();
		var rand = level.random;
		addEffect(new MobEffectInstance(MobEffects.CONFUSION, rand.nextInt(60, 260), rand.nextInt(0, 2), true, true));
	}

}
