package com.wumple.cannycomposter;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Reference.MOD_ID)
public class MyObjectHolder {
    // ----------------------------------------------------------------------
    // Blocks, Items, etc.
    //@ObjectHolder("cannycomposter:compost")
    public static /*final*/ CompostItem itemCompost = null;

    // ----------------------------------------------------------------------
    // Events

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistrationHandler {

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            final IForgeRegistry<Item> registry = event.getRegistry();

            Item.Properties properties = new Item.Properties();

            itemCompost = new CompostItem(properties);
            itemCompost.setRegistryName("cannycomposter:compost");
            registry.register(itemCompost);
        }
    }
}