package me.maximumpower55.tropics.client;

import me.maximumpower55.tropics.Tropics;
import me.maximumpower55.tropics.duck.BoatEntityExtensions;
import me.maximumpower55.tropics.duck.BoatEntityExtensions.CustomBoatType;
import me.maximumpower55.tropics.init.TBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.state.properties.WoodType;

public class TropicsClient implements ClientModInitializer {

	public static final ModelResourceLocation COCONUT_MODEL = new ModelResourceLocation(Tropics.id("coconut_model"), "inventory");

	@Override
	public void onInitializeClient() {
		ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> out.accept(COCONUT_MODEL));

		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), TBlocks.PALM_WOOD.sapling, TBlocks.PALM_WOOD.pottedSapling, TBlocks.PALM_WOOD.door, TBlocks.PALM_WOOD.trapdoor);

		var boatModel = BoatModel.createBodyModel();
		var chestBoatModel = ChestBoatModel.createBodyModel();
		for (CustomBoatType customBoatType : BoatEntityExtensions.CUSTOM_TYPES.values()) {
			EntityModelLayerRegistry.registerModelLayer(createCustomBoatModelName(customBoatType.woodType(), false), () -> boatModel);
			EntityModelLayerRegistry.registerModelLayer(createCustomBoatModelName(customBoatType.woodType(), true), () -> chestBoatModel);
		}

		ClientPlayNetworking.registerGlobalReceiver(BoatEntityExtensions.CUSTOM_BOAT_TYPE_PACKET, (client, handler, buf, responseSender) -> {
			buf.retain();
			client.execute(() -> {
				try {
					var entity = client.level.getEntity(buf.readInt());
					if (entity != null) ((BoatEntityExtensions) entity).tropics$setCustomType(BoatEntityExtensions.CUSTOM_TYPES.get(buf.readResourceLocation()));
				} catch (ClassCastException e) {
					Tropics.LOGGER.warn("Received custom boat type packet for an non boat entity!", e);
				} finally {
					buf.release();
				}
			});
		});
	}

	public static ModelLayerLocation createCustomBoatModelName(WoodType woodType, boolean hasChest) {
		var boatLayerLocation = hasChest ? ModelLayers.createChestBoatModelName(Boat.Type.BIRCH) : ModelLayers.createBoatModelName(Boat.Type.BIRCH);
		boatLayerLocation = new ModelLayerLocation(
			new ResourceLocation(boatLayerLocation.getModel().getNamespace(), boatLayerLocation.getModel().getPath().replace("birch", woodType.name())), boatLayerLocation.getLayer());
		return boatLayerLocation;
	}

}
