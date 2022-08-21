package me.maximumpower55.tropics.client.render;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;

import me.maximumpower55.tropics.Tropics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CoconutItemRenderer {

	private static final Minecraft MC = Minecraft.getInstance();

	public static final ModelResourceLocation COCONUT_MODEL = new ModelResourceLocation(Tropics.id("coconut_model"), "inventory");

	public static final ModelResourceLocation COCONUT_GUI_MODEL = new ModelResourceLocation(Tropics.id("coconut_gui"), "inventory");

	public static void render(ItemStack stack, TransformType transformType, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
		BakedModel coconutModel = MC.getModelManager().getModel(COCONUT_MODEL);
		BakedModel coconutGuiModel = MC.getModelManager().getModel(COCONUT_GUI_MODEL);

		boolean left = transformType == TransformType.FIRST_PERSON_LEFT_HAND || transformType == TransformType.THIRD_PERSON_LEFT_HAND;

		// FIXME: Figure out a better fix than this.
		poseStack.translate(0.5, 0.5, 0.5);

		if (transformType == TransformType.GUI) {
			Lighting.setupForFlatItems();
			MC.getItemRenderer().render(stack, transformType, left, poseStack, buffer, light, overlay, coconutGuiModel);

			if (buffer instanceof MultiBufferSource.BufferSource imm) imm.endBatch();
			Lighting.setupFor3DItems();
		} else {
			MC.getItemRenderer().render(stack, transformType, left, poseStack, buffer, light, overlay, coconutModel);
		}
	}

}
