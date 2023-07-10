package me.maximumpower55.tropics.init;

import java.util.function.Function;
import java.util.function.Supplier;

import me.maximumpower55.tropics.Tropics;
import me.maximumpower55.tropics.content.item.CustomBoatItem;
import me.maximumpower55.tropics.duck.BoatEntityExtensions;
import me.maximumpower55.tropics.duck.BoatEntityExtensions.CustomBoatType;
import me.maximumpower55.tropics.util.WoodTypeResourceLocation;
import me.maximumpower55.tropics.util.CrabReg;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamilies;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class WoodRegistrar implements CrabReg.DeferringFunction {

	private final String baseName;
	private final BlockSetType blockSetType;
	private final WoodType woodType;

	public final Block log;
	public final Block strippedLog;
	public final Block wood;
	public final Block strippedWood;
	public final Block planks;
	public final Block sapling;
	public final Block pottedSapling;
	public final Block leaves;
	public final SlabBlock slab;
	public final StairBlock stairs;
	public final FenceBlock fence;
	public final FenceGateBlock fenceGate;
	public final DoorBlock door;
	public final ButtonBlock button;
	public final PressurePlateBlock pressurePlate;
	public final StandingSignBlock sign;
	public final CeilingHangingSignBlock hangingSign;
	public final WallSignBlock wallSign;
	public final WallHangingSignBlock wallHangingSign;
	public final TrapDoorBlock trapdoor;

	public final Item boatItem;
	public final Item chestBoatItem;

	public final BlockFamily blockFamily;

	WoodRegistrar(String baseName, AbstractTreeGrower treeGrower, Function<BlockState, Boolean> saplingMayPlaceOn) {
		this.baseName = baseName;
		final var woodTypeId = new WoodTypeResourceLocation(Tropics.id(baseName));
		this.blockSetType = BlockSetTypeRegistry.registerWood(woodTypeId);
		this.woodType = WoodTypeRegistry.register(woodTypeId, blockSetType);

		this.log = new RotatedPillarBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_LOG));
		this.strippedLog = new RotatedPillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_BIRCH_LOG));
		this.wood = new RotatedPillarBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_WOOD));
		this.strippedWood = new RotatedPillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_BIRCH_WOOD));
		this.planks = new Block(FabricBlockSettings.copyOf(Blocks.BIRCH_PLANKS));
		this.sapling = new SaplingBlock(treeGrower, FabricBlockSettings.copyOf(Blocks.BIRCH_SAPLING)) {
			@Override
			protected boolean mayPlaceOn(BlockState state, BlockGetter blockGetter, BlockPos pos) {
				return saplingMayPlaceOn.apply(state);
			}
		};
		this.pottedSapling = new FlowerPotBlock(this.sapling, FabricBlockSettings.copyOf(Blocks.POTTED_BIRCH_SAPLING));
		this.leaves = new LeavesBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_LEAVES));
		this.slab = new SlabBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_SLAB));
		this.stairs = new StairBlock(planks.defaultBlockState(), FabricBlockSettings.copyOf(Blocks.BIRCH_STAIRS));
		this.fence = new FenceBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_FENCE));
		this.fenceGate = new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_FENCE_GATE), woodType);
		this.door = new DoorBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_DOOR), blockSetType);
		this.button = new ButtonBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_BUTTON), blockSetType, 30, true);
		this.pressurePlate = new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, FabricBlockSettings.copyOf(Blocks.BIRCH_PRESSURE_PLATE), blockSetType);
		this.sign = new StandingSignBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_SIGN), woodType);
		this.hangingSign = new CeilingHangingSignBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_HANGING_SIGN), woodType);
		this.wallSign = new WallSignBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_WALL_SIGN), woodType);
		this.wallHangingSign = new WallHangingSignBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_WALL_HANGING_SIGN), woodType);
		this.trapdoor = new TrapDoorBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_TRAPDOOR), blockSetType);

		var customBoatTypeSupplier = new Supplier<CustomBoatType>() {
			public CustomBoatType value;
			@Override
			public CustomBoatType get() {
				return value;
			}
		};
		this.boatItem = new CustomBoatItem(false, customBoatTypeSupplier, new Item.Properties().stacksTo(1));
		this.chestBoatItem = new CustomBoatItem(true, customBoatTypeSupplier, new Item.Properties().stacksTo(1));

		customBoatTypeSupplier.value = BoatEntityExtensions.registerBoatType(Tropics.id(baseName), woodType, planks, boatItem, chestBoatItem);

		this.blockFamily = BlockFamilies.familyBuilder(planks)
			.button(button)
			.fence(fence)
			.fenceGate(fenceGate)
			.pressurePlate(pressurePlate)
			.sign(sign, wallSign)
			.slab(slab)
			.stairs(stairs)
			.door(door)
			.trapdoor(trapdoor)
			.recipeGroupPrefix("wooden")
			.recipeUnlockedBy("has_planks")
			.getFamily();
	}

	private void registerBlockWithItem(CrabReg reg, String path, Block block) {
		reg.delegateRegister(BuiltInRegistries.BLOCK, path, block);
		reg.delegateRegister(BuiltInRegistries.ITEM, path, new BlockItem(block, new Item.Properties()));
	}

	@Override
	public void register(CrabReg reg) {
		registerBlockWithItem(reg, baseName + "_log", log);
		registerBlockWithItem(reg, "stripped_" + baseName + "_log", strippedLog);
		registerBlockWithItem(reg, baseName + "_wood", wood);
		registerBlockWithItem(reg, "stripped_" + baseName + "_wood", strippedWood);
		registerBlockWithItem(reg, baseName + "_planks", planks);
		reg.delegateRegister(BuiltInRegistries.BLOCK, "potted_" + baseName + "_sapling", pottedSapling);
		registerBlockWithItem(reg, baseName + "_slab", slab);
		registerBlockWithItem(reg, baseName + "_stairs", stairs);
		registerBlockWithItem(reg, baseName + "_fence", fence);
		registerBlockWithItem(reg, baseName + "_fence_gate", fenceGate);
		registerBlockWithItem(reg, baseName + "_door", door);
		registerBlockWithItem(reg, baseName + "_button", button);
		registerBlockWithItem(reg, baseName + "_pressure_plate", pressurePlate);
		registerBlockWithItem(reg, baseName + "_trapdoor", trapdoor);
		final var signId = baseName + "_sign";
		reg.delegateRegister(BuiltInRegistries.BLOCK, signId, sign);
		reg.delegateRegister(BuiltInRegistries.BLOCK, baseName + "_wall_sign", wallSign);
		reg.delegateRegister(BuiltInRegistries.ITEM, signId, new SignItem(new Item.Properties().stacksTo(16), sign, wallSign));
		final var hangingSignId = baseName + "_hanging_sign";
		reg.delegateRegister(BuiltInRegistries.BLOCK, hangingSignId, hangingSign);
		reg.delegateRegister(BuiltInRegistries.BLOCK, baseName + "_wall_hanging_sign", wallHangingSign);
		reg.delegateRegister(BuiltInRegistries.ITEM, hangingSignId, new HangingSignItem(hangingSign, wallHangingSign, new Item.Properties().stacksTo(16)));
		reg.delegateRegister(BuiltInRegistries.ITEM, baseName + "_boat", boatItem);
		reg.delegateRegister(BuiltInRegistries.ITEM, baseName + "_chest_boat", chestBoatItem);
		registerBlockWithItem(reg, baseName + "_leaves", leaves);
		registerBlockWithItem(reg, baseName + "_sapling", sapling);

		FlammableBlockRegistry.getDefaultInstance().add(log, FlammableBlockRegistry.getDefaultInstance().get(Blocks.BIRCH_LOG));
		FlammableBlockRegistry.getDefaultInstance().add(strippedLog, FlammableBlockRegistry.getDefaultInstance().get(Blocks.STRIPPED_BIRCH_LOG));
		FlammableBlockRegistry.getDefaultInstance().add(wood, FlammableBlockRegistry.getDefaultInstance().get(Blocks.BIRCH_WOOD));
		FlammableBlockRegistry.getDefaultInstance().add(strippedWood, FlammableBlockRegistry.getDefaultInstance().get(Blocks.STRIPPED_BIRCH_WOOD));
		FlammableBlockRegistry.getDefaultInstance().add(planks, FlammableBlockRegistry.getDefaultInstance().get(Blocks.BIRCH_PLANKS));
		FlammableBlockRegistry.getDefaultInstance().add(leaves, FlammableBlockRegistry.getDefaultInstance().get(Blocks.BIRCH_LEAVES));
		FlammableBlockRegistry.getDefaultInstance().add(slab, FlammableBlockRegistry.getDefaultInstance().get(Blocks.BIRCH_SLAB));
		FlammableBlockRegistry.getDefaultInstance().add(stairs, FlammableBlockRegistry.getDefaultInstance().get(Blocks.BIRCH_STAIRS));
		FlammableBlockRegistry.getDefaultInstance().add(fence, FlammableBlockRegistry.getDefaultInstance().get(Blocks.BIRCH_FENCE));
		FlammableBlockRegistry.getDefaultInstance().add(fenceGate, FlammableBlockRegistry.getDefaultInstance().get(Blocks.BIRCH_FENCE_GATE));
		FlammableBlockRegistry.getDefaultInstance().add(door, FlammableBlockRegistry.getDefaultInstance().get(Blocks.BIRCH_DOOR));
		FlammableBlockRegistry.getDefaultInstance().add(button, FlammableBlockRegistry.getDefaultInstance().get(Blocks.BIRCH_BUTTON));
		FlammableBlockRegistry.getDefaultInstance().add(pressurePlate, FlammableBlockRegistry.getDefaultInstance().get(Blocks.BIRCH_PRESSURE_PLATE));
		FlammableBlockRegistry.getDefaultInstance().add(trapdoor, FlammableBlockRegistry.getDefaultInstance().get(Blocks.BIRCH_TRAPDOOR));

		StrippableBlockRegistry.register(log, strippedLog);
		StrippableBlockRegistry.register(wood, strippedWood);
	}

}
