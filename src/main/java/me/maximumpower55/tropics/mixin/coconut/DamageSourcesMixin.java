package me.maximumpower55.tropics.mixin.coconut;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.maximumpower55.tropics.mechanics.CoconutDamageSource;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;

@Mixin(DamageSources.class)
public abstract class DamageSourcesMixin implements CoconutDamageSource.Getter {

	@Shadow
	@Final
	private Registry<DamageType> damageTypes;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void tropics$registerCoconutDamageSource(RegistryAccess registryAccess, CallbackInfo ci) {
		this.tropics$coconutDamageSource = new CoconutDamageSource(this.damageTypes.getHolderOrThrow(CoconutDamageSource.RESOURCE_KEY));
	}

	private CoconutDamageSource tropics$coconutDamageSource;

	@Override
	public CoconutDamageSource tropics$instance() {
		return tropics$coconutDamageSource;
	}

}
