package me.maximumpower55.tropics.mixin.client;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Maps;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;

import me.maximumpower55.tropics.client.TropicsClient;
import me.maximumpower55.tropics.duck.BoatEntityExtensions;
import me.maximumpower55.tropics.duck.BoatEntityExtensions.CustomBoatType;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;

@Mixin(BoatRenderer.class)
public abstract class BoatRendererMixin {

	private final Map<CustomBoatType, Pair<ResourceLocation, ListModel<Boat>>> tropics$customBoatModels = Maps.newHashMap();
	private CustomBoatType tropics$capturedCustomType = null;

	@Shadow
	private static String getTextureLocation(Boat.Type boatType, boolean hasChest) {
		throw new IllegalStateException("not shadowed");
	}

	private static String tropics$getTextureLocation(CustomBoatType customBoatType, boolean hasChest) {
		return getTextureLocation(Boat.Type.BIRCH, hasChest).replace("birch", customBoatType.woodType().name());
	}

	@Inject(method = "<init>", at = @At("RETURN"))
	private void tropics$buildCustomBoatModels(EntityRendererProvider.Context context, boolean hasChest, CallbackInfo ci) {
		for (var customBoatType : BoatEntityExtensions.CUSTOM_TYPES.values()) {
			var modelRoot = context.bakeLayer(TropicsClient.createCustomBoatModelName(customBoatType.woodType(), hasChest));
			var model = hasChest ? new ChestBoatModel(modelRoot) : new BoatModel(modelRoot);
			tropics$customBoatModels.put(customBoatType, Pair.of(new ResourceLocation(customBoatType.id().getNamespace(), tropics$getTextureLocation(customBoatType, hasChest)), (ListModel<Boat>) model));
		}
	}

	@Inject(method = "render", at = @At("HEAD"))
	private void tropics$captureWoodType(Boat boat, float yaw, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
		tropics$capturedCustomType = ((BoatEntityExtensions) boat).tropics$getCustomType();
	}

	@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "java/util/Map.get(Ljava/lang/Object;)Ljava/lang/Object;"))
	private Object tropics$useCustomBoatModel(Map<Object, Object> instance, Object key, Operation<Object> original) {
		if (tropics$capturedCustomType == null) {
			return original.call(instance, key);
		} else {
			var model = tropics$customBoatModels.get(tropics$capturedCustomType);
			tropics$capturedCustomType = null;
			return model;
		}
	}

}
