package me.maximumpower55.tropics;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import com.unascribed.lib39.core.api.AutoMixin;

public class TropicsMixin extends AutoMixin {
	@Override
	public void onLoad(String pkg) {
		MixinExtrasBootstrap.init();
		super.onLoad(pkg);
	}
}
