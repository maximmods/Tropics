package me.maximumpower55.tropics.init;

import me.maximumpower55.tropics.Tropics;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class TItemTags {

	public static final TagKey<Item> PREVENTS_COCONUT_DAMAGE = create("prevents_coconut_damage");

	private static TagKey<Item> create(String path) {
		return TagKey.create(Registry.ITEM_REGISTRY, Tropics.id(path));
	}

	public static void init() {}

}
