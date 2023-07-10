package me.maximumpower55.tropics.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import me.maximumpower55.tropics.client.TropicsClient;
import me.maximumpower55.tropics.init.TItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

	@Shadow
	@Final
	private ItemModelShaper itemModelShaper;

	@ModifyVariable(method = "render", at = @At(value = "HEAD"), argsOnly = true)
	private BakedModel tropics$replaceCoconutItemModel(BakedModel model, ItemStack stack, ItemDisplayContext ctx) {
		if (stack.is(TItems.COCONUT) && ctx != ItemDisplayContext.GUI) {
			return itemModelShaper.getModelManager().getModel(TropicsClient.COCONUT_MODEL);
		}
		return model;
	}

}
