package me.maximumpower55.tropics.duck;

import java.util.Map;

import com.google.common.collect.Maps;

import io.netty.buffer.Unpooled;
import me.maximumpower55.tropics.Tropics;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.WoodType;

public interface BoatEntityExtensions {

	static final ResourceLocation CUSTOM_BOAT_TYPE_PACKET = Tropics.id("custom_boat_type");
	static final Map<ResourceLocation, CustomBoatType> CUSTOM_TYPES = Maps.newHashMap();

	static CustomBoatType registerBoatType(ResourceLocation id, WoodType woodType, Block planks, Item boat, Item chestBoat) {
		var v = new CustomBoatType(id, woodType, planks, boat, chestBoat);
		CUSTOM_TYPES.put(id, v);
		return CUSTOM_TYPES.put(id, new CustomBoatType(id, woodType, planks, boat, chestBoat));
	}

	CustomBoatType tropics$getCustomType();
	void tropics$setCustomType(CustomBoatType type);

	public record CustomBoatType(ResourceLocation id, WoodType woodType, Block planks, Item boat, Item chestBoat) {}

	default Packet<ClientGamePacketListener> makeCustomTypePacket() {
		var buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeInt(((Entity) this).getId());
		buf.writeResourceLocation(tropics$getCustomType().id);
		return ServerPlayNetworking.createS2CPacket(CUSTOM_BOAT_TYPE_PACKET, buf);
	}

}
