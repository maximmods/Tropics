package me.maximumpower55.tropics.datagen;

import me.maximumpower55.tropics.content.block.CoconutBlock;
import me.maximumpower55.tropics.init.TBlocks;
import me.maximumpower55.tropics.init.TItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;

public class ModelProvider extends FabricModelProvider {

	public ModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
		blockStateModelGenerator.blockStateOutput
			.accept(
				MultiVariantGenerator.multiVariant(TBlocks.COCONUT)
					.with(
						PropertyDispatch.property(CoconutBlock.ATTACHED)
							.generate(
								(attached) -> Variant.variant()
										.with(
											VariantProperties.MODEL,
											TextureMapping.getBlockTexture(TBlocks.COCONUT, (attached ? "_attached" : ""))
										)
							)
					)
					.with(BlockModelGenerators.createHorizontalFacingDispatch())
			);

		blockStateModelGenerator.woodProvider(TBlocks.PALM_WOOD.log).logWithHorizontal(TBlocks.PALM_WOOD.log).wood(TBlocks.PALM_WOOD.wood);
		blockStateModelGenerator.woodProvider(TBlocks.PALM_WOOD.strippedLog).logWithHorizontal(TBlocks.PALM_WOOD.strippedLog).wood(TBlocks.PALM_WOOD.strippedWood);

		blockStateModelGenerator.createTrivialCube(TBlocks.PALM_WOOD.leaves);
		blockStateModelGenerator.createPlant(TBlocks.PALM_WOOD.sapling, TBlocks.PALM_WOOD.pottedSapling, BlockModelGenerators.TintState.NOT_TINTED);

		blockStateModelGenerator.family(TBlocks.PALM_WOOD.blockFamily.getBaseBlock()).generateFor(TBlocks.PALM_WOOD.blockFamily);
		blockStateModelGenerator.createHangingSign(TBlocks.PALM_WOOD.strippedLog, TBlocks.PALM_WOOD.hangingSign, TBlocks.PALM_WOOD.wallHangingSign);
	}

	@Override
	public void generateItemModels(ItemModelGenerators itemModelGenerator) {
		itemModelGenerator.generateFlatItem(TBlocks.PALM_WOOD.boatItem, ModelTemplates.FLAT_ITEM);
		itemModelGenerator.generateFlatItem(TBlocks.PALM_WOOD.chestBoatItem, ModelTemplates.FLAT_ITEM);

		itemModelGenerator.generateFlatItem(TItems.COCONUT, ModelTemplates.FLAT_ITEM);
		itemModelGenerator.generateFlatItem(TItems.CRACKED_COCONUT, ModelTemplates.FLAT_ITEM);
		itemModelGenerator.generateFlatItem(TItems.COCONUT_SHELL, ModelTemplates.FLAT_ITEM);
	}

}
