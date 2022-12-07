package me.maximumpower55.tropics.world;

import me.maximumpower55.tropics.content.block.CoconutBlock;
import me.maximumpower55.tropics.init.TBlocks;
import me.maximumpower55.tropics.util.BlockPlot;
import me.maximumpower55.tropics.util.DirectionUtils;
import net.minecraft.Util;
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

	private static final BlockState LOG = Blocks.JUNGLE_LOG.defaultBlockState();
	private static final BlockState LEAVES = Blocks.JUNGLE_LEAVES.defaultBlockState().setValue(LeavesBlock.DISTANCE, 1);
	private static final BlockState COCONUT = TBlocks.COCONUT.defaultBlockState().setValue(CoconutBlock.ATTACHED, true);

	private static final int MIN_STEM_HEIGHT = 6;
	private static final int MAX_STEM_HEIGHT = 8;

	private static final int LEAVES_LENGTH = 4;

	private static void tryGenerate(LevelAccessor level, MutableBlockPos pos, RandomSource random) {
		BlockState soil = level.getBlockState(pos);

		if (!(soil.is(BlockTags.SAND) || soil.is(BlockTags.DIRT)) || !level.getFluidState(pos.above()).isEmpty()) return;

		BlockPlot cur = new BlockPlot(pos.above(), level);

		Direction facing = Util.getRandom(DirectionUtils.HORIZONTAL_DIRECTIONS, random);

		int stemHeight = random.nextInt(MIN_STEM_HEIGHT, MAX_STEM_HEIGHT);

		boolean isLeaning = false;

		for (int i = 0; i < stemHeight; i++) {
			int leanChance = isLeaning ? 2 : 3;

			if (random.nextInt(leanChance) == 0) {
				cur.move(facing);
				isLeaning = true;
			}

			if (!cur.tryPlot(LOG)) return;

			cur.move(Direction.UP);
		}

		cur.tryPlot(LEAVES, true);

		cur.push();
		for (Direction dir : DirectionUtils.HORIZONTAL_DIRECTIONS) {
			cur.push();
			cur.move(Direction.DOWN).move(dir);
			cur.tryPlot(COCONUT.setValue(CoconutBlock.FACING, dir), false);
			cur.pop();

			cur.push();
			cur.move(dir);

			leaves: for (int i = 0; i < LEAVES_LENGTH; i++) {
				if (!cur.canPlot(true)) break leaves;

				if (i == LEAVES_LENGTH - 1) {
					cur.move(Direction.DOWN);

					cur.push();
					cur.move(dir, -1);
					cur.tryPlot(LEAVES, true);
					cur.pop();
				}

				cur.tryPlot(LEAVES, true);

				cur.move(dir);
			}
			cur.pop();
		}
		cur.pop();

		cur.push();
		cur.move(Direction.UP);
		if (cur.tryPlot(LEAVES, true)) {
			for (Direction dir : DirectionUtils.HORIZONTAL_DIRECTIONS) {
				cur.push();
				cur.move(dir);
				cur.tryPlot(LEAVES, true);

				cur.move(dir.getClockWise());
				cur.move(Direction.DOWN);
				cur.tryPlot(LEAVES, true);
				cur.pop();
			}
		}
		cur.pop();

		cur.place();
	}

	public static void generateTrees(LevelAccessor level, long seed, ChunkAccess chunk) {
		WorldgenRandom random = new WorldgenRandom(new XoroshiroRandomSource(seed));
		random.setDecorationSeed(seed, chunk.getPos().x, chunk.getPos().z);

		if (random.nextInt(10) == 0) {
			int x = random.nextInt(16);
			int z = random.nextInt(16);
			random.setDecorationSeed(seed, x, z);

			MutableBlockPos pos = new MutableBlockPos(chunk.getPos().getMinBlockX() + x, 0, chunk.getPos().getMinBlockZ() + z);
			Holder<Biome> biome = level.getBiome(pos);

			if (biome.is(BiomeTags.IS_BEACH)) {
				int y = chunk.getHeight(Types.OCEAN_FLOOR, x, z);
				pos.setY(y);

				int surface = chunk.getHeight(Types.WORLD_SURFACE, x, z);

				if (surface - y <= 0) {
					tryGenerate(level, pos, random);
				}
			}
		}
	}

}
