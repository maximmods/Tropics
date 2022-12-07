package me.maximumpower55.tropics;

import com.unascribed.lib39.core.api.AutoRegistry;
import com.unascribed.lib39.crowbar.api.WorldGenerationEvents;
import com.unascribed.lib39.dessicant.api.DessicantControl;

import me.maximumpower55.tropics.duck.ItemExtensions;
import me.maximumpower55.tropics.init.TBlocks;
import me.maximumpower55.tropics.init.TTags;
import me.maximumpower55.tropics.init.TItems;
import me.maximumpower55.tropics.init.TSounds;
import me.maximumpower55.tropics.world.PalmTreeGenerator;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

public class Tropics implements ModInitializer {

	public static final AutoRegistry AUTOREG = AutoRegistry.of("tropics");

	public static final CreativeModeTab ITEM_GROUP = FabricItemGroupBuilder.create(id("item_group")).build();

	@Override
	public void onInitialize() {
		TItems.init();
		TBlocks.init();
		TSounds.init();

		TTags.init();

		Registry.ITEM.forEach(item -> {
			ResourceLocation id = Registry.ITEM.getKey(item);

			if (id != null && id.getNamespace().equals("tropics")) {
				((ItemExtensions)item).tropics$setGroup(ITEM_GROUP);
			}
		});

		DessicantControl.optIn("tropics");

		WorldGenerationEvents.AFTER_GENERATE_FEATURES.register(ctx -> {
			PalmTreeGenerator.generateTrees(ctx.region(), ctx.region().getSeed(), ctx.chunk());
		});
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation("tropics", path);
	}

}
