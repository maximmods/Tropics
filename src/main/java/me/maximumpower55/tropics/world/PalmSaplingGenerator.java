package me.maximumpower55.tropics.world;

import java.util.function.Consumer;
import java.util.function.Function;

import me.maximumpower55.tropics.content.block.CoconutBlock;
import me.maximumpower55.tropics.init.TBlocks;
import me.maximumpower55.tropics.util.DirectionUtils;
import net.minecraft.Util;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.Heightmap.Types;

public class PalmSaplingGenerator extends AbstractTreeGrower {

	public static final Function<BlockState, Boolean> MAY_PLACE_ON = soil -> soil.is(BlockTags.SAND) || soil.is(BlockTags.DIRT);

	private static final int MIN_STEM_HEIGHT = 6;
	private static final int MAX_STEM_HEIGHT = 8;

	private static final int LEAVES_LENGTH = 4;

	private static boolean generate(LevelAccessor level, MutableBlockPos pos, RandomSource random) {
		BlockState soil = level.getBlockState(pos);

		if (!MAY_PLACE_ON.apply(soil) || !level.getFluidState(pos.above()).isEmpty()) return false;

		final var TRUNK = TBlocks.PALM_WOOD.log.defaultBlockState();
		final var LEAVES = TBlocks.PALM_WOOD.leaves.defaultBlockState().setValue(LeavesBlock.DISTANCE, 1);

		TreePlot plot = new TreePlot(TBlocks.PALM_WOOD.sapling, TRUNK, LEAVES, pos.above(), level);

		Direction facing = Util.getRandom(DirectionUtils.HORIZONTAL_DIRECTIONS, random);

		int stemHeight = random.nextInt(MIN_STEM_HEIGHT, MAX_STEM_HEIGHT);

		boolean isLeaning = false;

		for (int i = 0; i < stemHeight; i++) {
			if (i != stemHeight - 1) {
				if (!plot.plotLog(decorator -> {}, leafGrower -> {})) return false;
			} else {
				if (!plot.plotLog(decorator -> decorate(plot, decorator), leafGrower -> growLeaves(plot, leafGrower))) return false;
			}

			int leanChance = isLeaning ? 2 : 3;

			if (random.nextInt(leanChance) == 0) {
				plot.move(facing);
				isLeaning = true;
			}

			plot.move(Direction.UP);
		}

		plot.place();

		return true;
	}

	private static void decorate(TreePlot plot, Consumer<TreePlot.Decoration> decorator) {
		for (Direction dir : DirectionUtils.HORIZONTAL_DIRECTIONS) {
			plot.push();
			plot.move(dir);
			if (plot.canPlot()) decorator.accept(new TreePlot.Decoration(plot.peek().immutable(), TBlocks.COCONUT.defaultBlockState().setValue(CoconutBlock.ATTACHED, true).setValue(CoconutBlock.FACING, dir)));
			plot.pop();
		}
	}

	private static void growLeaves(TreePlot plot, Consumer<BlockPos> leafGrower) {
		plot.move(Direction.UP);
		if (plot.canPlot()) leafGrower.accept(plot.peek().immutable());

		for (Direction dir : DirectionUtils.HORIZONTAL_DIRECTIONS) {
			plot.push();
			plot.move(dir);

			leaves: for (int i = 0; i < LEAVES_LENGTH; i++) {
				if (!plot.canPlot()) break leaves;

				if (i == LEAVES_LENGTH - 1) {
					plot.move(Direction.DOWN);

					plot.push();
					plot.move(dir, -1);
					if (plot.canPlot()) leafGrower.accept(plot.peek().immutable());
					plot.pop();
				}

				if (plot.canPlot()) leafGrower.accept(plot.peek().immutable());

				plot.move(dir);
			}
			plot.pop();
		}

		plot.move(Direction.UP);
		boolean topLeaves = plot.canPlot();
		if (topLeaves) leafGrower.accept(plot.peek().immutable());
		for (Direction dir : DirectionUtils.HORIZONTAL_DIRECTIONS) {
			plot.push();
			plot.move(dir);
			if (topLeaves && plot.canPlot()) leafGrower.accept(plot.peek().immutable());

			plot.move(dir.getClockWise());
			plot.move(Direction.DOWN);
			if (plot.canPlot()) leafGrower.accept(plot.peek().immutable());
			plot.pop();
		}
	}

	public static void generateNatural(LevelAccessor level, long seed, ChunkAccess chunk) {
		WorldgenRandom random = new WorldgenRandom(new XoroshiroRandomSource(seed));
		random.setDecorationSeed(seed, chunk.getPos().x, chunk.getPos().z);

		if (random.nextInt(10) == 0) {
			int x = random.nextInt(16);
			int z = random.nextInt(16);
			random.setDecorationSeed(seed, x, z);

			MutableBlockPos pos = new MutableBlockPos(chunk.getPos().getMinBlockX() + x, 0, chunk.getPos().getMinBlockZ() + z);
			Holder<Biome> biome = level.getBiome(pos);

			if (biome.is(BiomeTags.IS_BEACH)) {
				pos.setY(chunk.getHeight(Types.OCEAN_FLOOR, x, z));
				if (chunk.getHeight(Types.WORLD_SURFACE, x, z) - pos.getY() <= 0) {
					generate(level, pos, random);
				}
			}
		}
	}

	@Override
	public boolean growTree(ServerLevel level, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, RandomSource random) {
		return generate(level, pos.below().mutable(), random);
	}

	@Override
	public ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource p0, boolean p1) {
		return null;
	}

}
