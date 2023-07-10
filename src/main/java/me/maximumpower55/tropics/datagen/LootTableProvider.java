package me.maximumpower55.tropics.datagen;

import me.maximumpower55.tropics.init.TBlocks;
import me.maximumpower55.tropics.init.TItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class LootTableProvider extends FabricBlockLootTableProvider {

	public LootTableProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generate() {
		dropSelf(TBlocks.PALM_WOOD.planks);
		dropSelf(TBlocks.PALM_WOOD.sapling);
		add(TBlocks.PALM_WOOD.leaves, block -> createLeavesDrops(block, TBlocks.PALM_WOOD.sapling, NORMAL_LEAVES_SAPLING_CHANCES));
		dropSelf(TBlocks.PALM_WOOD.log);
		dropSelf(TBlocks.PALM_WOOD.strippedLog);
		dropSelf(TBlocks.PALM_WOOD.wood);
		dropSelf(TBlocks.PALM_WOOD.strippedWood);
		dropSelf(TBlocks.PALM_WOOD.sign);
		dropSelf(TBlocks.PALM_WOOD.hangingSign);
		dropSelf(TBlocks.PALM_WOOD.pressurePlate);
		dropSelf(TBlocks.PALM_WOOD.trapdoor);
		dropSelf(TBlocks.PALM_WOOD.stairs);
		dropSelf(TBlocks.PALM_WOOD.button);
		dropSelf(TBlocks.PALM_WOOD.fenceGate);
		dropSelf(TBlocks.PALM_WOOD.fence);
		dropPottedContents(TBlocks.PALM_WOOD.pottedSapling);
		add(TBlocks.PALM_WOOD.slab, block -> createSlabItemTable(block));
		add(TBlocks.PALM_WOOD.door, block -> createDoorTable(block));


		add(TBlocks.COCONUT, block -> createSingleItemTable(TItems.CRACKED_COCONUT, ConstantValue.exactly(2)));
	}

}
