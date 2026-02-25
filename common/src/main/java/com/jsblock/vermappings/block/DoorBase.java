package com.jsblock.vermappings.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;

public class DoorBase extends DoorBlock {
	protected DoorBase(Properties properties) {
		super(properties, net.minecraft.world.level.block.state.properties.BlockSetType.IRON);
	}

	@Override
	public final void tick(BlockState state, ServerLevel world, BlockPos pos, net.minecraft.util.RandomSource random) {
		super.tick(state, world, pos, random);
		tick(state, world, pos);
	}

	public void tick(BlockState state, ServerLevel world, BlockPos pos) {}
}