package me.maximumpower55.tropics.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import me.maximumpower55.tropics.Tropics;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin {

	private static final ResourceLocation MOD_ICON = Tropics.id("textures/icon.png");

	private PoseStack tropics$capturedPoseStack = null;
	private boolean tropics$renderingTropicsItemGroup = false;

	@Inject(method = "renderTabButton", at = @At("HEAD"))
	private void tropics$checkCurrentlyBeingRenderedItemGroupAndCapturePoseStack(PoseStack poseStack, CreativeModeTab itemGroup, CallbackInfo ci) {
		tropics$capturedPoseStack = poseStack;
		tropics$renderingTropicsItemGroup = itemGroup == Tropics.ITEM_GROUP;
	}

	@WrapWithCondition(method = "renderTabButton", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/entity/ItemRenderer.renderAndDecorateItem(Lnet/minecraft/world/item/ItemStack;II)V"))
	private boolean tropics$renderModIcon(ItemRenderer itemRenderer, ItemStack itemStack, int x, int y) {
		if (tropics$renderingTropicsItemGroup && tropics$capturedPoseStack != null) {
			RenderSystem.setShaderTexture(0, MOD_ICON);
			GuiComponent.blit(tropics$capturedPoseStack, x, y, 0, 0, 16, 16, 16, 16);
			return false;
		}

		return true;
	}

}
