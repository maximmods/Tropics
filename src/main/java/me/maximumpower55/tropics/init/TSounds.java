package me.maximumpower55.tropics.init;

import me.maximumpower55.tropics.Tropics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public class TSounds {

	public static final SoundEvent BONK = SoundEvent.createVariableRangeEvent(Tropics.id("bonk"));

	public static void init() {
		Tropics.REG.registerFields(TSounds.class, SoundEvent.class, Tropics.REG.registerFunction(BuiltInRegistries.SOUND_EVENT));
	}

}
