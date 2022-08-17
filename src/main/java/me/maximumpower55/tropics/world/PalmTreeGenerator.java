package me.maximumpower55.tropics.world;

import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

import me.maximumpower55.tropics.content.block.CoconutBlock;
import me.maximumpower55.tropics.init.TBlocks;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.Heightmap.Types;

public class PalmTreeGenerator {

	private static final Direction[] horizontalDirections = Direction.stream()
			.filter(dir -> dir.getAxis() != Direction.Axis.Y)
			.collect(Collectors.toList()).toArray(Direction[]::new);

	private static final BlockState LOG = Blocks.JUNGLE_LOG.defaultBlockState();
	private static final BlockState LEAVES = Blocks.JUNGLE_LEAVES.defaultBlockState().setValue(LeavesBlock.DISTANCE, 1);
	private static final BlockState COCONUT = TBlocks.COCONUT.defaultBlockState().setValue(CoconutBlock.ATTACHED, true);

	private static final int MIN_STEM_HEIGHT = 6;
	private static final int MAX_STEM_HEIGHT = 8;

	private static final int LEAVES_LENGTH = 3;

	private static void generate(LevelAccessor level, MutableBlockPos pos, RandomSource random) {
		BlockState soil = level.getBlockState(pos);

		if (!((soil.is(BlockTags.SAND) || soil.is(BlockTags.DIRT)) && level.getFluidState(pos.above()).isEmpty())) return;

		pos.move(Direction.UP);

		Map<BlockPos, BlockState> plan = Maps.newLinkedHashMap();
		MutableBlockPos cur = pos.mutable();

		Direction facing = Util.getRandom(horizontalDirections, random);

		int stemHeight = random.nextInt(MAX_STEM_HEIGHT);
		if (stemHeight < MIN_STEM_HEIGHT) stemHeight = MIN_STEM_HEIGHT;

		boolean isLeaning = false;

		for (int i = 0; i < stemHeight; i++) {
			int leanChance = isLeaning ? 2 : 3;

			if (i > 3 && random.nextInt(leanChance) == 0) {
				cur.move(facing);
				isLeaning = true;
			}

			plan.put(cur.immutable(), LOG);
			cur.move(Direction.UP);
		}

		cur.move(Direction.DOWN);

		for (Direction direction : horizontalDirections) {
			plan.put(cur.relative(direction).immutable(), COCONUT.setValue(CoconutBlock.FACING, direction));

			MutableBlockPos leavesCur = cur.mutable();
			leavesCur.move(Direction.UP);

			plan.put(leavesCur.immutable(), LEAVES);
			leavesCur.move(direction);

			for (int i = 0; i < LEAVES_LENGTH; i++) {
				if (i == LEAVES_LENGTH - 1) leavesCur.move(Direction.DOWN);
				plan.put(leavesCur.immutable(), LEAVES);
				leavesCur.move(direction);
			}
		}

		for (Map.Entry<BlockPos, BlockState> block : plan.entrySet()) {
			level.setBlock(block.getKey(), block.getValue(), 3);
		}
	}

	public static void generateTrees(LevelAccessor level, long seed, ChunkAccess chunk) {
		WorldgenRandom random = new WorldgenRandom(new XoroshiroRandomSource(seed));
		random.setDecorationSeed(seed, chunk.getPos().x, chunk.getPos().z);

		if (random.nextInt(10) == 0) {
			int x = random.nextInt(16);
			int z = random.nextInt(16);
			random.setDecorationSeed(seed, x, z);

			// Figure out how many allocations this MutableBlockPos really saves?
			MutableBlockPos pos = new MutableBlockPos(chunk.getPos().getMinBlockX() + x, 0, chunk.getPos().getMinBlockZ() + z);
			Holder<Biome> biome = level.getBiome(pos);

			if (biome.is(BiomeTags.IS_BEACH)) {
				int y = chunk.getHeight(Types.OCEAN_FLOOR, x, z);
				pos.setY(y);

				PalmTreeGenerator.generate(level, pos, random);
			}
		}
	}

}
