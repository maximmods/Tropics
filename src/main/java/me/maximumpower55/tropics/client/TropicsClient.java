package me.maximumpower55.tropics.client;

import com.unascribed.lib39.ripple.api.SplashTextRegistry;

import me.maximumpower55.tropics.client.render.CoconutItemRenderer;
import me.maximumpower55.tropics.init.TItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;

public class TropicsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		BuiltinItemRendererRegistry.INSTANCE.register(TItems.COCONUT, CoconutItemRenderer::render);
		ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> CoconutItemRenderer.registerModels(out));

		SplashTextRegistry.registerStatic("Bonk!");
	}

}
