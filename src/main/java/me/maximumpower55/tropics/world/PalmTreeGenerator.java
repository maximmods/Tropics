package me.maximumpower55.tropics.world;

import java.util.LinkedHashMap;
import java.util.Map;

import me.maximumpower55.tropics.content.block.CoconutBlock;
import me.maximumpower55.tropics.init.TBlocks;
import me.maximumpower55.tropics.util.DirectionUtils;
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

	private static final BlockState LOG = Blocks.JUNGLE_LOG.defaultBlockState();
	private static final BlockState LEAVES = Blocks.JUNGLE_LEAVES.defaultBlockState().setValue(LeavesBlock.DISTANCE, 1);
	private static final BlockState COCONUT = TBlocks.COCONUT.defaultBlockState().setValue(CoconutBlock.ATTACHED, true);

	private static final int MIN_STEM_HEIGHT = 6;
	private static final int MAX_STEM_HEIGHT = 8;

	private static final int LEAVES_LENGTH = 4;

	private static void tryGenerate(LevelAccessor level, MutableBlockPos pos, RandomSource random) {
		BlockState soil = level.getBlockState(pos);

		if (!(soil.is(BlockTags.SAND) || soil.is(BlockTags.DIRT)) || !level.getFluidState(pos.above()).isEmpty()) return;

		Map<BlockPos, BlockState> plan = new LinkedHashMap<>();
		MutableBlockPos cur = pos.above().mutable();

		Direction facing = Util.getRandom(DirectionUtils.HORIZONTAL_DIRECTIONS, random);

		int stemHeight = random.nextInt(MIN_STEM_HEIGHT, MAX_STEM_HEIGHT);

		boolean isLeaning = false;

		for (int i = 0; i < stemHeight; i++) {
			int leanChance = isLeaning ? 2 : 3;

			if (random.nextInt(leanChance) == 0) {
				cur.move(facing);
				isLeaning = true;
			}

			if (!canPlace(plan, true, cur, level)) return;
			plan.put(cur.immutable(), LOG);
			cur.move(Direction.UP);
		}

		cur.move(Direction.DOWN);

		for (Direction dir : DirectionUtils.HORIZONTAL_DIRECTIONS) {
			BlockPos coconutPos = cur.relative(dir).immutable();
			if (canPlace(plan, true, coconutPos, level)) plan.put(coconutPos, COCONUT.setValue(CoconutBlock.FACING, dir));

			MutableBlockPos leavesCur = cur.above().mutable();
			plan.put(leavesCur.immutable(), LEAVES);
			leavesCur.move(dir);

			for (int i = 0; i < LEAVES_LENGTH; i++) {
				if (!canPlace(plan, false, leavesCur, level)) break;

				if (i == LEAVES_LENGTH - 1) {
					leavesCur.move(Direction.DOWN);

					BlockPos connectingPos = leavesCur.relative(dir, -1).immutable();
					if (canPlace(plan, false, connectingPos, level)) plan.put(connectingPos, LEAVES);
				}

				plan.put(leavesCur.immutable(), LEAVES);
				leavesCur.move(dir);
			}
		}

		MutableBlockPos topLeavesCur = cur.above().mutable();

		if (canPlace(plan, false, topLeavesCur.above(), level)) {
			topLeavesCur.move(Direction.UP);

			plan.put(topLeavesCur.immutable(), LEAVES);

			for (Direction dir : DirectionUtils.HORIZONTAL_DIRECTIONS) {
				topLeavesCur.move(dir);

				if (canPlace(plan, false, topLeavesCur, level)) plan.put(topLeavesCur.immutable(), LEAVES);

				BlockPos connectingPos = topLeavesCur.below().relative(dir.getClockWise()).immutable();
				if (canPlace(plan, false, connectingPos, level)) plan.put(connectingPos, LEAVES);

				topLeavesCur.move(dir, -1);
			}
		}

		for (Map.Entry<BlockPos, BlockState> block : plan.entrySet()) {
			level.setBlock(block.getKey(), block.getValue(), 3);
		}
	}

	private static boolean canPlace(Map<BlockPos, BlockState> plan, boolean detectSelf, BlockPos pos, LevelAccessor level) {
		BlockState state = detectSelf ? plan.getOrDefault(pos, level.getBlockState(pos)) : level.getBlockState(pos);
		return state.isAir() || state.getMaterial().isReplaceable();
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
