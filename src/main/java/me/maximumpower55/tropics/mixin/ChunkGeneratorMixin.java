package me.maximumpower55.tropics.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.maximumpower55.tropics.world.PalmSaplingGenerator;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;

@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin {

	@Inject(method = "applyBiomeDecoration", at = @At("RETURN"))
	private void tropics$afterBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager, CallbackInfo ci) {
		PalmSaplingGenerator.generateNatural(level, level.getSeed(), chunk);
	}

}
