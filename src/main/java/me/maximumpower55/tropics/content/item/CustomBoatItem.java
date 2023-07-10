package me.maximumpower55.tropics.content.item;

import java.util.function.Supplier;

import me.maximumpower55.tropics.duck.BoatEntityExtensions;
import me.maximumpower55.tropics.duck.BoatEntityExtensions.CustomBoatType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public final class CustomBoatItem extends BoatItem {

	private final Supplier<CustomBoatType> customTypeSupplier;

	public CustomBoatItem(boolean hasChest, Supplier<CustomBoatType> customTypeSupplier, Properties properties) {
		super(hasChest, Boat.Type.BIRCH, properties);
		this.customTypeSupplier = customTypeSupplier;
	}

	public Boat getBoat(boolean hasChest, Level level, HitResult hit) {
		var boat = hasChest
			? new ChestBoat(level, hit.getLocation().x, hit.getLocation().y, hit.getLocation().z)
			: new Boat(level, hit.getLocation().x, hit.getLocation().y, hit.getLocation().z);
		((BoatEntityExtensions) boat).tropics$setCustomType(customTypeSupplier.get());
		return boat;
	}

}
