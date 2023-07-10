package me.maximumpower55.tropics.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.maximumpower55.tropics.content.item.CustomBoatItem;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

@Mixin(BoatItem.class)
public abstract class BoatItemMixin {

	@Shadow
	@Final
	private boolean hasChest;

	@Inject(method = "getBoat", at = @At("RETURN"), cancellable = true)
	private void tropics$getBoatCustom(Level level, HitResult hit, CallbackInfoReturnable<Boat> cir) {
		if ((Object) this instanceof CustomBoatItem custom) cir.setReturnValue(custom.getBoat(hasChest, level, hit));
	}

}
