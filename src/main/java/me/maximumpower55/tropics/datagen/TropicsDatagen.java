package me.maximumpower55.tropics.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class TropicsDatagen implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		var pack = fabricDataGenerator.createPack();

		var blockTagProvider = pack.addProvider(BlockTagProvider::new);
		pack.addProvider((output, completableFuture) -> new ItemTagProvider(output, completableFuture, blockTagProvider));
		pack.addProvider(RecipeProvider::new);
		pack.addProvider(LootTableProvider::new);

		pack.addProvider(ModelProvider::new);
	}

}
