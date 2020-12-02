package com.wumple.cannycomposter.mixin;

import com.wumple.cannycomposter.ConfigManager;
import com.wumple.cannycomposter.CannyComposterEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
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
                    CannyComposterEvents.generateSteam(worldIn, pos, level);
                }
                // tick again for more smoke later
                BlockState blockstate = worldIn.getBlockState(pos);

                worldIn.getPendingBlockTicks().scheduleTick(pos, blockstate.getBlock(), CannyComposterEvents.getWaitTicks(worldIn));
            }
        }
    }

    //patch can extract item?
    @Redirect(method = {"onBlockActivated","createInventory"},at = @At(value = "FIELD",target = "Lnet/minecraft/item/Items;BONE_MEAL:Lnet/minecraft/item/Item;"))
    private Item changeBoneMeal() {
        return Registry.ITEM.getOrDefault(new ResourceLocation(ConfigManager.compostItem.get()));
    }
}
