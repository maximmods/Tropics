package me.maximumpower55.tropics.mixin;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.maximumpower55.tropics.duck.BoatEntityExtensions;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

@Mixin(ServerEntity.class)
public abstract class ServerEntityMixin {

	@Shadow
	@Final
	private Entity entity;

	@Inject(method = "sendPairingData", at = @At("RETURN"))
	private void tropics$sendCustomBoatType(ServerPlayer player, Consumer<Packet<ClientGamePacketListener>> packetSender, CallbackInfo ci) {
		if (entity instanceof BoatEntityExtensions extensions && extensions.tropics$getCustomType() != null) packetSender.accept(extensions.makeCustomTypePacket());
	}

}
