package me.maximumpower55.tropics.mechanics;

import me.maximumpower55.tropics.Tropics;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

public class CoconutDamageSource extends DamageSource {

	public CoconutDamageSource(Holder<DamageType> damageType) {
		super(damageType);
	}

	public static final ResourceKey<DamageType> RESOURCE_KEY = ResourceKey.create(Registries.DAMAGE_TYPE, Tropics.id("coconut"));

	public static interface Getter {
		CoconutDamageSource tropics$instance();
	}

}
