package me.maximumpower55.tropics.init;

import me.maximumpower55.tropics.Tropics;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class TItems {

	public static final BlockItem COCONUT = new BlockItem(TBlocks.COCONUT, new Item.Properties());

	public static final Item CRACKED_COCONUT = new Item(new Item.Properties()
					.food(new FoodProperties.Builder()
						.nutrition(3)
						.saturationMod(1.3f)
						.build()
					)
				) {
			@Override
			public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
				super.finishUsingItem(stack, level, livingEntity);

				if (livingEntity instanceof ServerPlayer player) {
					CriteriaTriggers.CONSUME_ITEM.trigger(player, stack);
					player.awardStat(Stats.ITEM_USED.get(this));
				}

				if (stack.isEmpty()) {
					return new ItemStack(COCONUT_SHELL);
				} else {
					if (livingEntity instanceof Player player && !player.getAbilities().instabuild) {
						ItemStack newStack = new ItemStack(COCONUT_SHELL);

						if (!player.getInventory().add(newStack)) {
							player.drop(newStack, false);
						}
					}

					return stack;
				}
			};

			public UseAnim getUseAnimation(ItemStack stack) {
				return UseAnim.DRINK;
			};

			public SoundEvent getDrinkingSound() {
				return getEatingSound();
			};
		};
	public static final Item COCONUT_SHELL = new Item(new Item.Properties());

	public static void init() {
		Tropics.AUTOREG.autoRegister(Registry.ITEM, TItems.class, Item.class);

		FuelRegistry.INSTANCE.add(COCONUT_SHELL, 40);
	}

}
