package me.maximumpower55.tropics.init;

import me.maximumpower55.tropics.Tropics;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;

public class TSounds {

	public static final SoundEvent BONK = new SoundEvent(Tropics.id("bonk"));

	public static void init() {
		Tropics.AUTOREG.autoRegister(Registry.SOUND_EVENT, TSounds.class, SoundEvent.class);
	}

}
