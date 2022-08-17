package me.maximumpower55.tropics.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

@Mixin(Item.class)
public interface ItemAccessor {

	@Accessor("category")
	void tropics$setGroup(CreativeModeTab group);

}
