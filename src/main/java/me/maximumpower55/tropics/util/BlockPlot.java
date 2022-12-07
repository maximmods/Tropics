package me.maximumpower55.tropics.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.CommonLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class BlockPlot extends BlockPosStack {

	private final Map<BlockPos, BlockState> plan = new HashMap<>();
	private final CommonLevelAccessor level;

	public BlockPlot(BlockPos start, CommonLevelAccessor level) {
		super(start);
		this.level = level;
	}

	public boolean canPlot(boolean ignoreSelf) {
		var curPos = peek();
		var stateAtCurPos = ignoreSelf ? level.getBlockState(curPos) : plan.getOrDefault(curPos, level.getBlockState(curPos));
		return stateAtCurPos.isAir() || stateAtCurPos.getMaterial().isReplaceable();
	}

	public boolean tryPlot(BlockState state) {
		return tryPlot(state, false);
	}

	public boolean tryPlot(BlockState state, boolean ignoreSelf) {
		if (!canPlot(ignoreSelf)) return false;
		plan.put(peek().immutable(), state);
		return true;
	}

	public void place() {
		for (Map.Entry<BlockPos, BlockState> block : plan.entrySet()) {
			level.setBlock(block.getKey(), block.getValue(), 3);
		}
	}

}
