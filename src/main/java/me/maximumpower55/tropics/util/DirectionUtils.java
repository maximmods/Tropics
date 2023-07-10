package me.maximumpower55.tropics.util;

import java.util.stream.Collectors;

import net.minecraft.core.Direction;

public class DirectionUtils {

	public static final Direction[] HORIZONTAL_DIRECTIONS = Direction
		.stream()
		.filter(dir -> dir.getAxis() != Direction.Axis.Y)
		.collect(Collectors.toSet())
		.toArray(new Direction[] {});

}
