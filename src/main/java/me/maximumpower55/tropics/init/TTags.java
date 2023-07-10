package me.maximumpower55.tropics.init;

import me.maximumpower55.tropics.Tropics;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;

public class TTags {

	public static final class Item {

		public static final TagKey<net.minecraft.world.item.Item> PALM_LOGS = TagKey.create(Registries.ITEM, Tropics.id("palm_logs"));
		public static final TagKey<net.minecraft.world.item.Item> PREVENTS_COCONUT_DAMAGE = TagKey.create(Registries.ITEM, Tropics.id("prevents_coconut_damage"));

		public static void init() {}

	}

	public static final class Block {

		public static final TagKey<net.minecraft.world.level.block.Block> PALM_LOGS = TagKey.create(Registries.BLOCK, Tropics.id("palm_logs"));
		public static final TagKey<net.minecraft.world.level.block.Block> PREVENTS_COCONUT_CRACK = TagKey.create(Registries.BLOCK, Tropics.id("prevents_coconut_crack"));

		public static void init() {}

	}

	public static void init() {
		Item.init();
		Block.init();
	}

}
