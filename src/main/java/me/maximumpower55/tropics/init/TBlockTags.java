package me.maximumpower55.tropics.init;

import me.maximumpower55.tropics.Tropics;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class TBlockTags {

	public static final TagKey<Block> PREVENTS_COCONUT_CRACK = create("prevents_coconut_crack");

	private static TagKey<Block> create(String path) {
		return TagKey.create(Registry.BLOCK_REGISTRY, Tropics.id(path));
	}

	public static void init() {}

}
