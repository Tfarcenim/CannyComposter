package com.wumple.cannycomposter;

import com.wumple.cannycomposter.ConfigManager;
import com.wumple.cannycomposter.MyObjectHolder;
import net.minecraft.block.BlockState;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.BonemealEvent;

public class CompostItem extends BoneMealItem
{
	public CompostItem(Item.Properties properties)
	{
		super(properties);
	}

	protected static int NUM_PARTICLES = 5;

	protected static boolean tryUseCompost(World worldIn) {

		return worldIn.rand.nextDouble() <= ConfigManager.compostChance.get();
	}

	protected static void spawnSuccessParticles(World worldIn, BlockPos pos)
	{
		if (!worldIn.isRemote)
		{
			BoneMealItem.spawnBonemealParticles(worldIn,pos,15);
		}
	}

	protected static void spawnTryFailureParticles(World worldIn, BlockPos pos) {
		int data = 15;
		if (!worldIn.isRemote) {
			BlockState blockstate = worldIn.getBlockState(pos);
			if (!blockstate.isAir(worldIn, pos)) {
				for(int i = 0; i < data; ++i) {
					double d0 = random.nextGaussian() * 0.02D;
					double d1 = random.nextGaussian() * 0.02D;
					double d2 = random.nextGaussian() * 0.02D;
					worldIn.addParticle(ParticleTypes.SMOKE,
							(float)pos.getX() + random.nextFloat(), (double)pos.getY() + (double)random.nextFloat() * blockstate.getShape(worldIn, pos).getEnd(Direction.Axis.Y),
							(float)pos.getZ() + random.nextFloat(), d0, d1, d2);
				}

			}
		}
	}

	public static void tryUseEvent(BonemealEvent e) {
		if (e.getStack().getItem() == MyObjectHolder.itemCompost) {
			World world = e.getWorld();
			BlockPos pos = e.getPos();
			if (tryUseCompost(e.getWorld())) {
				spawnSuccessParticles(world,pos);
			} else {
				spawnTryFailureParticles(world,pos);
				e.setCanceled(true);
			}
		}
	}
}
