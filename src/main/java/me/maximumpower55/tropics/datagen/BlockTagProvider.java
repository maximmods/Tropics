package me.maximumpower55.tropics.datagen;

import java.util.concurrent.CompletableFuture;

import me.maximumpower55.tropics.init.TBlocks;
import me.maximumpower55.tropics.init.TTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;

public class BlockTagProvider extends FabricTagProvider.BlockTagProvider {

	public BlockTagProvider(FabricDataOutput output, CompletableFuture<Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(Provider provider) {
		((FabricTagBuilder) tag(BlockTags.PLANKS)).add(TBlocks.PALM_WOOD.planks);

		((FabricTagBuilder) tag(BlockTags.WOODEN_BUTTONS)).add(TBlocks.PALM_WOOD.button);

		((FabricTagBuilder) tag(BlockTags.WOODEN_DOORS)).add(TBlocks.PALM_WOOD.door);
		((FabricTagBuilder) tag(BlockTags.WOODEN_STAIRS)).add(TBlocks.PALM_WOOD.stairs);
		((FabricTagBuilder) tag(BlockTags.WOODEN_SLABS)).add(TBlocks.PALM_WOOD.slab);
		((FabricTagBuilder) tag(BlockTags.WOODEN_FENCES)).add(TBlocks.PALM_WOOD.fence);
		((FabricTagBuilder) tag(BlockTags.WOODEN_TRAPDOORS)).add(TBlocks.PALM_WOOD.trapdoor);

		((FabricTagBuilder) tag(BlockTags.STANDING_SIGNS)).add(TBlocks.PALM_WOOD.sign);
		((FabricTagBuilder) tag(BlockTags.CEILING_HANGING_SIGNS)).add(TBlocks.PALM_WOOD.hangingSign);
		((FabricTagBuilder) tag(BlockTags.WALL_SIGNS)).add(TBlocks.PALM_WOOD.wallSign);
		((FabricTagBuilder) tag(BlockTags.WALL_HANGING_SIGNS)).add(TBlocks.PALM_WOOD.wallHangingSign);

		((FabricTagBuilder) tag(TTags.Block.PALM_LOGS)).add(TBlocks.PALM_WOOD.log, TBlocks.PALM_WOOD.wood, TBlocks.PALM_WOOD.strippedLog, TBlocks.PALM_WOOD.strippedWood);
		tag(BlockTags.LOGS_THAT_BURN).addTag(TTags.Block.PALM_LOGS);
		((FabricTagBuilder) tag(BlockTags.OVERWORLD_NATURAL_LOGS)).add(TBlocks.PALM_WOOD.log);

		((FabricTagBuilder) tag(BlockTags.WOODEN_PRESSURE_PLATES)).add(TBlocks.PALM_WOOD.pressurePlate);

		((FabricTagBuilder) tag(BlockTags.LEAVES)).add(TBlocks.PALM_WOOD.leaves);
		((FabricTagBuilder) tag(BlockTags.MINEABLE_WITH_HOE)).add(TBlocks.PALM_WOOD.leaves);
		((FabricTagBuilder) tag(BlockTags.SAPLINGS)).add(TBlocks.PALM_WOOD.sapling);
		((FabricTagBuilder) tag(BlockTags.FLOWER_POTS)).add(TBlocks.PALM_WOOD.pottedSapling);

		((FabricTagBuilder) tag(TTags.Block.PREVENTS_COCONUT_CRACK))
			.addOptionalTag(BlockTags.WOOL)
			.addOptionalTag(BlockTags.WOOL_CARPETS)
			.add(Blocks.SLIME_BLOCK, Blocks.HONEY_BLOCK);
	}

}
