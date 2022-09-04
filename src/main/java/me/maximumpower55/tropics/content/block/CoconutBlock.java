package me.maximumpower55.tropics.content.block;

import com.google.common.collect.ImmutableMap;
import com.unascribed.lib39.dessicant.api.SimpleLootBlock;

import me.maximumpower55.tropics.duck.FallingBlockEntityExtensions;
import me.maximumpower55.tropics.init.TItems;
import me.maximumpower55.tropics.init.TTags;
import me.maximumpower55.tropics.mechanics.CoconutDamageSource;
import me.maximumpower55.tropics.util.DirectionUtils;
import me.maximumpower55.tropics.util.VoxelShapeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CoconutBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, Fallable, SimpleLootBlock {

	public static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	private static final VoxelShape SHAPE = VoxelShapeUtils.horizontallyCenteredBox(8, 0, 8, 8, 8, 8);

	private static final VoxelShape BASE_ATTACHED_SHAPE = SHAPE.move(0, 0.125, 0.25);

	private static final ImmutableMap<Direction, VoxelShape> ATTACHED_SHAPES = ImmutableMap.<Direction, VoxelShape>builder()
				.put(Direction.NORTH, BASE_ATTACHED_SHAPE)
				.put(Direction.EAST, VoxelShapeUtils.rotate(BASE_ATTACHED_SHAPE, Direction.EAST))
				.put(Direction.SOUTH, VoxelShapeUtils.rotate(BASE_ATTACHED_SHAPE, Direction.SOUTH))
				.put(Direction.WEST, VoxelShapeUtils.rotate(BASE_ATTACHED_SHAPE, Direction.WEST))
				.build();

	public CoconutBlock(Properties properties) {
		super(properties);

		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ATTACHED, false).setValue(WATERLOGGED, false));
	}

	@Override
	public void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
		if (state.getValue(ATTACHED)) fall(state, level, hit.getBlockPos());
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
								 BlockHitResult hit) {
		if (!level.isClientSide && player.getAbilities().mayBuild && player.getItemInHand(hand).isEmpty() && !state.getValue(ATTACHED)) {
			ItemStack itemStack = new ItemStack(TItems.COCONUT);

			if (player.getInventory().add(itemStack)) {
				level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

				level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2f, (level.random.nextFloat() - level.random.nextFloat()) * 1.4f + 2);

				return InteractionResult.SUCCESS;
			} else {
				return InteractionResult.FAIL;
			}
		}

		return InteractionResult.PASS;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ATTACHED, FACING, WATERLOGGED);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		if (state.getValue(ATTACHED)) {
			return ATTACHED_SHAPES.get(state.getValue(FACING));
		}

		return SHAPE;
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
		return Shapes.empty();
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		level.scheduleTick(pos, this, 2);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos,
								  BlockPos facingPos) {
		level.scheduleTick(currentPos, this, 2);

		if (state.getValue(WATERLOGGED)) level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));

		return state;
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
		BlockPos attachedPos = state.getValue(ATTACHED) ? pos.relative(state.getValue(FACING).getOpposite()) : pos.below();

		if (FallingBlock.isFree(level.getBlockState(attachedPos))) fall(state, level, pos);
	}

	private void fall(BlockState state, Level level, BlockPos pos) {
		if (pos.getY() < level.getMinBuildHeight()) return;

		state = state.setValue(ATTACHED, false);
		FallingBlockEntity be = FallingBlockEntity.fall(level, pos, state);
		be.setHurtsEntities(2.5f, 60);
	}

	@Override
	public DamageSource getFallDamageSource() {
		return new CoconutDamageSource();
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		Direction facing = DirectionUtils.isAlongY(ctx.getClickedFace()) ? Direction.NORTH : ctx.getClickedFace();

		BlockPos attachedPos = ctx.getClickedPos().relative(facing.getOpposite());
		boolean attached = ctx.getPlayer().getAbilities().instabuild
			? !FallingBlock.isFree(ctx.getLevel().getBlockState(attachedPos)) : false;

		return defaultBlockState()
			.setValue(ATTACHED, attached)
			.setValue(FACING, facing)
			.setValue(WATERLOGGED, ctx.getLevel().getFluidState(ctx.getClickedPos()).is(FluidTags.WATER));
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
	}

	public boolean shouldBreak(BlockGetter level, BlockPos pos) {
		boolean shouldBreak = !level.getBlockState(pos.below()).is(TTags.PREVENTS_COCONUT_CRACK);
		shouldBreak = level.getFluidState(pos).is(FluidTags.WATER) ? false : shouldBreak;
		return shouldBreak;
	}

	@Override
	public void onBrokenAfterFall(Level level, BlockPos pos, FallingBlockEntity fallingBlockEntity) {
		boolean shouldCrack = shouldBreak(level, pos);
		((FallingBlockEntityExtensions)fallingBlockEntity).tropics$setShouldCrack(shouldCrack);

		if (shouldCrack) level.playSound(null, pos, soundType.getBreakSound(), SoundSource.BLOCKS, 0.4f, 1.3f);
	}

	@Override
	public ItemStack getLoot(BlockState state) {
		return new ItemStack(TItems.CRACKED_COCONUT, 2);
	}

}
