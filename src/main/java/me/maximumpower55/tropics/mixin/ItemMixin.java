package me.maximumpower55.tropics.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import me.maximumpower55.tropics.duck.ItemExtensions;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

@Mixin(Item.class)
public abstract class ItemMixin implements ItemExtensions {

	@Shadow
	@Mutable
	@Final
	private CreativeModeTab category;

	@Override
	public void tropics$setGroup(CreativeModeTab group) {
		category = group;
	}

}
