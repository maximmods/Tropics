package me.maximumpower55.tropics;

import com.unascribed.lib39.core.api.AutoRegistry;
import com.unascribed.lib39.crowbar.api.WorldGenerationEvents;
import com.unascribed.lib39.dessicant.api.DessicantControl;

import me.maximumpower55.tropics.init.TBlockTags;
import me.maximumpower55.tropics.init.TBlocks;
import me.maximumpower55.tropics.init.TItemGroups;
import me.maximumpower55.tropics.init.TItemTags;
import me.maximumpower55.tropics.init.TItems;
import me.maximumpower55.tropics.init.TSounds;
import me.maximumpower55.tropics.world.PalmTreeGenerator;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;

public class Tropics implements ModInitializer {

	public static final AutoRegistry AUTOREG = AutoRegistry.of("tropics");

	@Override
	public void onInitialize() {
		TItems.init();
		TItemTags.init();
		TBlocks.init();
		TBlockTags.init();
		TSounds.init();

		TItemGroups.init();

		DessicantControl.optIn("tropics");

		WorldGenerationEvents.AFTER_GENERATE_FEATURES.register(ctx -> {
			PalmTreeGenerator.generateTrees(ctx.region(), ctx.region().getSeed(), ctx.chunk());
		});
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation("tropics", path);
	}

}
