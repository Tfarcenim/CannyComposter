package com.wumple.cannycomposter;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class CannyComposterEvents {

	protected static boolean isCompostable(ItemStack itemstack) {
		return ConfigManager.compost.containsKey(itemstack.getItem());
	}

	protected static float getCompostLevelOf(ItemStack itemstack) {
		if (isCompostable(itemstack)) {
			Integer ilevel = ConfigManager.compost.get(itemstack.getItem());
			return ilevel.floatValue() / 100.0F;
		}
		return 0;
	}

	protected static boolean addItem(BlockState blockstateIn, IWorld worldIn, BlockPos posIn, ItemStack stackIn) {
		int i = blockstateIn.get(ComposterBlock.LEVEL);
		float f = getCompostLevelOf(stackIn);
		if ( i == 8 || (i != 0 || !(f > 0.0F)) && !(worldIn.getRandom().nextDouble() < (double) f))
		{
			return false;
		}
		else
		{
			// increase compost level
			int j = i + 1;
			worldIn.setBlockState(posIn, blockstateIn.with(ComposterBlock.LEVEL, j), 3);
			
			// schedule smoke and compost generation (if tick not already scheduled)
			worldIn.getPendingBlockTicks().scheduleTick(posIn, blockstateIn.getBlock(), getWaitTicks(worldIn));

			//if (!player.abilities.isCreativeMode) {
				stackIn.shrink(1);
			//}

			return true;
		}
	}
	
	public static void generateSteam(World worldIn, BlockPos pos, int level) {
		double yadd = 0.0D; 		
				
		BlockState blockstate = worldIn.getBlockState(pos);
		
		if (blockstate.getMaterial() != Material.AIR)
		{
			yadd += blockstate.getShape(worldIn, pos).getEnd(Direction.Axis.Y);
		}

		float ratio = level >> 3;
		double x = (double) pos.getX() + 0.5D;
		double y = (double) pos.getY() + yadd;
		double z = (double) pos.getZ() + 0.5D;
		// more particles the more full the bin is
		int num = 1 + Math.round(ratio);
		
		((ServerWorld) worldIn).spawnParticle(ParticleTypes.CLOUD, x, y, z, num, 0.0D, 0.0D, 0.0D, 0.0D);
	}

	public static int getWaitTicks(IWorld worldIn) {
		int randomTime = ConfigManager.cookMaxTime.get() - ConfigManager.cookMinTime.get();
		int seconds = ConfigManager.cookMaxTime.get() + worldIn.getRandom().nextInt(randomTime);
		return Math.max(1, 20 * seconds);
	}


	public static void useComposterEvent(PlayerInteractEvent.RightClickBlock e) {
		World world = e.getWorld();
		BlockState state = world.getBlockState(e.getPos());
		if (state.getBlock() == Blocks.COMPOSTER && !e.getItemStack().isEmpty() && !world.isRemote) {
			if (addItem(state, world, e.getPos(), e.getItemStack()))
			e.setCanceled(true);
		}
	}
}
