package me.gopro336.zenith.event.render;

import net.minecraft.block.state.IBlockState;

public class BlockStateAtEntityViewpointEvent {
	
	private IBlockState iBlockState;
	
	public BlockStateAtEntityViewpointEvent(IBlockState iBlockState) {
		this.iBlockState = iBlockState;
	}
	
	public IBlockState getiBlockState() {
		return iBlockState;
	}
	
	public void setiBlockState(IBlockState iBlockState) {
		this.iBlockState = iBlockState;
	}
}
