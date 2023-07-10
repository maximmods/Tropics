package me.maximumpower55.tropics.datagen;

import java.util.function.Consumer;

import me.maximumpower55.tropics.init.TBlocks;
import me.maximumpower55.tropics.init.TTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;

public class RecipeProvider extends FabricRecipeProvider {

	public RecipeProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void buildRecipes(Consumer<FinishedRecipe> exporter) {
		generateRecipes(exporter, TBlocks.PALM_WOOD.blockFamily);
		planksFromLogs(exporter, TBlocks.PALM_WOOD.planks, TTags.Item.PALM_LOGS, 4);
		woodFromLogs(exporter, TBlocks.PALM_WOOD.wood, TBlocks.PALM_WOOD.log);
		woodFromLogs(exporter, TBlocks.PALM_WOOD.strippedWood, TBlocks.PALM_WOOD.strippedLog);
		woodenBoat(exporter, TBlocks.PALM_WOOD.boatItem, TBlocks.PALM_WOOD.planks);
		chestBoat(exporter, TBlocks.PALM_WOOD.chestBoatItem, TBlocks.PALM_WOOD.boatItem);
		hangingSign(exporter, TBlocks.PALM_WOOD.hangingSign, TBlocks.PALM_WOOD.strippedLog);
	}

}
