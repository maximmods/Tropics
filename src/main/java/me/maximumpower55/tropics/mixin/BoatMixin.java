package me.maximumpower55.tropics.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import me.maximumpower55.tropics.duck.BoatEntityExtensions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

@Mixin(Boat.class)
public abstract class BoatMixin extends Entity implements BoatEntityExtensions {

	private CustomBoatType tropics$customType = null;

	private BoatMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public CustomBoatType tropics$getCustomType() {
		return tropics$customType;
	}
	@Override
	public void tropics$setCustomType(CustomBoatType type) {
		tropics$customType = type;
	}

	@Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
	private void tropics$writeWoodTypeToNBT(CompoundTag tag, CallbackInfo ci) {
		if (tropics$customType != null) tag.putString("Tropics$CustomType", tropics$customType.id().toString());
	}

	@Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
	private void tropics$readWoodTypeFromNBT(CompoundTag tag, CallbackInfo ci) {
		var customTypeId = tag.getString("Tropics$CustomType");
		if (customTypeId != "") tropics$customType = BoatEntityExtensions.CUSTOM_TYPES.get(new ResourceLocation(customTypeId));
	}

	@Inject(method = "getDropItem", at = @At("RETURN"), cancellable = true)
	private void tropics$getCustomDropItem(CallbackInfoReturnable<Item> cir) {
		if (tropics$customType != null) cir.setReturnValue(tropics$customType.boat());
	}

	@WrapOperation(method = "checkFallDamage", at = @At(value = "INVOKE", target = "net/minecraft/world/entity/vehicle/Boat$Type.getPlanks()Lnet/minecraft/world/level/block/Block;"))
	private Block tropics$dropCustomPlanks(Boat.Type instance, Operation<Block> original) {
		if (tropics$customType == null) {
			return original.call(instance);
		} else {
			return tropics$customType.planks();
		}
	}

}
