package me.maximumpower55.tropics.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxelShapeUtils {

	public static VoxelShape horizontallyCenteredBox(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Block.box(
			x1 - (x1 * 0.5), y1, z1 - (z1 * 0.5),
			x2 * 1.5, y2, z2 * 1.5
		);
	}

	public static VoxelShape rotate(VoxelShape shape, Direction dir) {
		List<VoxelShape> shapes = new ArrayList<>();

		shape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
			shapes.add(switch (dir) {
				case WEST -> Shapes.box(z1, y1, x1, z2, y2, x2);
				case SOUTH -> Shapes.box(1 - x2, y1, 1 - z2, 1 - x1, y2, 1 - z1);
				case EAST -> Shapes.box(1 - z2, y1, 1 - x2, 1 - z1, y2, 1 - x1);
				default -> Shapes.box(x1, y1, z1, x2, y2, z2);
			});
		});

		return Shapes.or(Shapes.empty(), shapes.toArray(new VoxelShape[]{}));
	}

}
