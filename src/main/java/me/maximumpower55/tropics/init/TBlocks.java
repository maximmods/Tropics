package me.maximumpower55.tropics.init;

import me.maximumpower55.tropics.Tropics;
import me.maximumpower55.tropics.content.block.CoconutBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class TBlocks {

	public static final CoconutBlock COCONUT = new CoconutBlock(FabricBlockSettings.of(Material.WOOD)
					.strength(.3f)
					.sound(SoundType.WOOD)
				);

	public static void init() {
		Tropics.AUTOREG.autoRegister(Registry.BLOCK, TBlocks.class, Block.class);

		FlammableBlockRegistry.getDefaultInstance().add(COCONUT, 3, 20);
	}

}
