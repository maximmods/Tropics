package me.maximumpower55.tropics.util;

import java.util.stream.Collectors;

import net.minecraft.core.Direction;

public class DirectionUtils {

	public static final Direction[] HORIZONTAL_DIRECTIONS = Direction.stream().filter(dir -> !isAlongY(dir)).collect(Collectors.toSet()).toArray(new Direction[]{});

	public static boolean isAlongY(Direction dir) { return dir.getAxis() == Direction.Axis.Y; }

}
