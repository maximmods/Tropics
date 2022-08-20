package me.maximumpower55.tropics.init;

import me.maximumpower55.tropics.Tropics;
import me.maximumpower55.tropics.accessor.ItemExtensions;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class TItemGroups {

	public static final CreativeModeTab MAIN = FabricItemGroupBuilder.create(Tropics.id("tropics"))
			.icon(() -> new ItemStack(TItems.ICON))
			.build();

	public static void init() {
		Registry.ITEM.forEach(item -> {
			ResourceLocation id = Registry.ITEM.getKey(item);

			if (id != null && id.getNamespace().equals("tropics")) {
				((ItemExtensions)item).tropics$setGroup(MAIN);
			}
		});
	}

}
