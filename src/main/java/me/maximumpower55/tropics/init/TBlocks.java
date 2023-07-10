package me.maximumpower55.tropics.init;

import me.maximumpower55.tropics.Tropics;
import me.maximumpower55.tropics.content.block.CoconutBlock;
import me.maximumpower55.tropics.world.PalmSaplingGenerator;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class TBlocks {

	public static final WoodRegistrar PALM_WOOD = new WoodRegistrar("palm", new PalmSaplingGenerator(), PalmSaplingGenerator.MAY_PLACE_ON);

	public static final CoconutBlock COCONUT = new CoconutBlock(FabricBlockSettings.create()
					.strength(.3f)
					.sound(SoundType.WOOD)
				);


	public static void init() {
		Tropics.REG.registerFields(TBlocks.class, Block.class, Tropics.REG.registerFunction(BuiltInRegistries.BLOCK));
		Tropics.REG.registerFields(TBlocks.class, WoodRegistrar.class, Tropics.REG.deferringFunction());

		FlammableBlockRegistry.getDefaultInstance().add(COCONUT, 3, 20);
	}

}
