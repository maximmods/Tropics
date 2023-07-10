package me.maximumpower55.tropics;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import me.maximumpower55.tropics.init.TBlocks;
import me.maximumpower55.tropics.init.TTags;
import me.maximumpower55.tropics.util.CrabReg;
import me.maximumpower55.tropics.init.TItems;
import me.maximumpower55.tropics.init.TSounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class Tropics implements ModInitializer {

	public static final CrabReg REG = new CrabReg("tropics");
	public static final Logger LOGGER = LoggerFactory.getLogger("Tropics");

	@Override
	public void onInitialize() {
		TItems.init();
		TBlocks.init();
		TSounds.init();

		TTags.init();

		final List<ItemStack> stacks = Lists.newArrayList();
		REG.forEachRegistered(Item.class, item -> stacks.add(new ItemStack(item)));
		TItems.ITEM_GROUP = REG.delegateRegister(BuiltInRegistries.CREATIVE_MODE_TAB, "item_group", FabricItemGroup.builder()
			.title(Component.translatable("itemGroup.tropics.item_group"))
			.icon(() -> stacks.get(stacks.size() - 1))
			.displayItems((flags, output) -> output.acceptAll(stacks))
			.build()
		);
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation("tropics", path);
	}

}
