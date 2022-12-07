package me.maximumpower55.tropics.util;

import java.util.LinkedList;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;

public class BlockPosStack {

	private final LinkedList<MutableBlockPos> positions = new LinkedList<>();

	public BlockPosStack(BlockPos start) { positions.add(start.mutable()); }

	public BlockPosStack move(Direction dir, int amount) {
		positions.getLast().move(dir, amount);
		return this;
	}

	public BlockPosStack move(Direction dir) {
		return move(dir, 1);
	}

	public void push() {
		positions.addLast(peek().mutable());
	}

	public BlockPos peek() {
		return positions.peekLast().immutable();
	}

	public BlockPos pop() {
		return positions.pollLast().immutable();
	}

}
