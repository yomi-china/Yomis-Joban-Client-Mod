package com.jsblock.vermappings.block;

import mtr.mappings.BlockMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ParticleBase extends BlockMapper {

	public ParticleBase(Properties properties) {
		super(properties);
	}

	@Override
	public final void animateTick(BlockState state, Level world, BlockPos pos, RandomSource randomSource) {
		animateTick(state, world, pos);
		super.animateTick(state, world, pos, randomSource);
	}

	public void animateTick(BlockState state, Level world, BlockPos pos) {
	}
}