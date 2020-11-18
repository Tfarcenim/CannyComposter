package com.wumple.cannycomposter.mixin;

import com.wumple.cannycomposter.ConfigManager;
import com.wumple.cannycomposter.CannyComposterEvent;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(ComposterBlock.class)
public class ComposterBlockMixin {

    @Inject(method = "tick",at = @At("HEAD"))
    private void tickCompost(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand, CallbackInfo ci) {
        if (!worldIn.isRemote()) {
            int level = state.get(ComposterBlock.LEVEL);
            if ((level > 0) && (level < 7))
            {
                if (ConfigManager.cookSteam.get())
                {
                    CannyComposterEvent.generateSteam(worldIn, pos, level);
                }
                // tick again for more smoke later
                BlockState blockstate = worldIn.getBlockState(pos);

                worldIn.getPendingBlockTicks().scheduleTick(pos, blockstate.getBlock(), CannyComposterEvent.getWaitTicks(worldIn));
            }
        }
    }

}
