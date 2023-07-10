package me.maximumpower55.tropics.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.maximumpower55.tropics.duck.BoatEntityExtensions;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;

@Mixin(ChestBoat.class)
public abstract class ChestBoatMixin {

	@Inject(method = "getDropItem", at = @At("RETURN"), cancellable = true)
	private void tropics$getCustomDropItem(CallbackInfoReturnable<Item> cir) {
		var customType = ((BoatEntityExtensions) (Object) this).tropics$getCustomType();
		if (customType != null) cir.setReturnValue(customType.chestBoat());
	}

}
