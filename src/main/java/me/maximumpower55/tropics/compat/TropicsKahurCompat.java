package me.maximumpower55.tropics.compat;

import com.unascribed.kahur.api.KahurImpactBehavior;
import com.unascribed.kahur.api.KahurImpactBehavior.ImpactResult;

import me.maximumpower55.tropics.init.TItems;
import me.maximumpower55.tropics.init.TSounds;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class TropicsKahurCompat {

	public static void init() {
		KahurImpactBehavior.register((shot, stack, hit) -> {
			Level level = shot.getLevel();
			Vec3 hitPos = hit.getLocation();

			if (hit instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity)
				level.playSound(null, hitPos.x(), hitPos.y(), hitPos.z(), TSounds.BONK, shot.getSoundSource(), 1.5f, 1);
			else level.playSound(null, hitPos.x(), hitPos.y(), hitPos.z(), SoundType.WOOD.getBreakSound(), shot.getSoundSource(), 0.4f, 1.3f);

			return ImpactResult.DEFAULT;
		}, TItems.COCONUT);
	}

}
