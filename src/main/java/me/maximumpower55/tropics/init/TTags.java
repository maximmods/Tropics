package me.maximumpower55.tropics.init;

import me.maximumpower55.tropics.Tropics;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TTags {

	public static final TagKey<Item> PREVENTS_COCONUT_DAMAGE = TagKey.create(Registry.ITEM_REGISTRY, Tropics.id("prevents_coconut_damage"));
	public static final TagKey<Block> PREVENTS_COCONUT_CRACK = TagKey.create(Registry.BLOCK_REGISTRY, Tropics.id("prevents_coconut_crack"));

	public static void init() {}

}
