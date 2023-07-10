package me.maximumpower55.tropics.util;

import net.minecraft.resources.ResourceLocation;

// this is a temp fix for a really bad issue in fapi, fapi's WoodTypeRegistry.register will try to register the wood type with the name `namespace:path` instead of `path`, which is bad since this breaks sign texture loading.
// And breaks anything that uses WoodType.name, looks like someone forgot to review their code.
public final class WoodTypeResourceLocation extends ResourceLocation {

	public WoodTypeResourceLocation(ResourceLocation resourceLocation) {
		super(resourceLocation.getNamespace(), resourceLocation.getPath());
	}

	@Override
	public String toString() {
		return getPath();
	}

}
