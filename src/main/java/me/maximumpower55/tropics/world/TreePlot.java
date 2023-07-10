package me.maximumpower55.tropics.world;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import it.unimi.dsi.fastutil.longs.Long2ReferenceOpenHashMap;
import me.maximumpower55.tropics.util.BlockPosStack;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public class TreePlot extends BlockPosStack {

	private final Block saplingBlock;
	private final BlockState trunkBlock;
	private final BlockState leavesBlock;

	private final Long2ReferenceOpenHashMap<Log> trunk = new Long2ReferenceOpenHashMap<>();

	private final LevelAccessor level;

	public TreePlot(Block saplingBlock, BlockState trunkBlock, BlockState leavesBlock, BlockPos start, LevelAccessor level) {
		super(start);
		this.saplingBlock = saplingBlock;
		this.trunkBlock = trunkBlock;
		this.leavesBlock = leavesBlock;
		this.level = level;
	}

	public boolean canPlot() {
		var cur = peek();
		var stateAtCur = level.getBlockState(cur);
		return stateAtCur.isAir() || stateAtCur.is(BlockTags.REPLACEABLE_BY_TREES) || stateAtCur.is(saplingBlock);
	}

	public boolean plotLog(Consumer<Consumer<Decoration>> decorator, Consumer<Consumer<BlockPos>> leafGrower) {
		if (!canPlot()) return false;
		push();
		Set<Decoration> decorations = new HashSet<>();
		decorator.accept(decorations::add);
		pop();
		push();
		Set<BlockPos> leaves = new HashSet<>();
		leafGrower.accept(leaves::add);
		pop();
		trunk.put(peek().immutable().asLong(), new Log(decorations.toArray(new Decoration[]{}), leaves.toArray(new BlockPos[]{})));
		return true;
	}

	public void place() {
		for (Long2ReferenceOpenHashMap.Entry<Log> entry : trunk.long2ReferenceEntrySet()) {
			for (BlockPos leafPos : entry.getValue().leaves) {
				level.setBlock(leafPos, leavesBlock.setValue(LeavesBlock.DISTANCE, 1), 3);
			}

			for (Decoration decoration : entry.getValue().decorations) {
				level.setBlock(decoration.pos, decoration.state, 3);
			}

			level.setBlock(BlockPos.of(entry.getLongKey()), trunkBlock, 3);
		}
	}

	private record Log(Decoration[] decorations, BlockPos[] leaves) {}

	public record Decoration(BlockPos pos, BlockState state) {}

}
