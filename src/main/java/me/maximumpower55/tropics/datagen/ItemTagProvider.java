package me.maximumpower55.tropics.datagen;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import me.maximumpower55.tropics.init.TBlocks;
import me.maximumpower55.tropics.init.TTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

public class ItemTagProvider extends FabricTagProvider.ItemTagProvider {

	public ItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable FabricTagProvider.BlockTagProvider blockTagProvider) {
		super(output, completableFuture, blockTagProvider);
	}

	@Override
	protected void addTags(Provider provider) {
		copy(TTags.Block.PALM_LOGS, TTags.Item.PALM_LOGS);
		copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
		((FabricTagBuilder) tag(ItemTags.PLANKS)).add(TBlocks.PALM_WOOD.planks.asItem());

		((FabricTagBuilder) tag(TTags.Item.PREVENTS_COCONUT_DAMAGE)).add(Items.IRON_HELMET, Items.GOLDEN_HELMET, Items.DIAMOND_HELMET, Items.NETHERITE_HELMET, Items.TURTLE_HELMET);

		((FabricTagBuilder) tag(ItemTags.BOATS)).add(TBlocks.PALM_WOOD.boatItem);
		((FabricTagBuilder) tag(ItemTags.CHEST_BOATS)).add(TBlocks.PALM_WOOD.chestBoatItem);
	}

}
