package me.maximumpower55.tropics;

import com.unascribed.lib39.core.api.ModPostInitializer;

import me.maximumpower55.tropics.compat.TropicsKahurCompat;
import net.fabricmc.loader.api.FabricLoader;

public class TropicsPostInit implements ModPostInitializer {

	@Override
	public void onPostInitialize() {
		if (FabricLoader.getInstance().isModLoaded("kahur")) TropicsKahurCompat.init();
	}

}
