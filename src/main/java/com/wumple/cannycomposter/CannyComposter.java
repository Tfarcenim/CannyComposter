package com.wumple.cannycomposter;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLFingerprintViolationEvent;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

@Mod(CannyComposter.MOD_ID)
public class CannyComposter {
	// /*
	public static final String MOD_ID = "cannycomposter";
	public static /*final*/ CompostItem itemCompost = null;



	public Logger getLogger()
	{
		return LogManager.getLogger(MOD_ID);
	}
	
	public CannyComposter() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(ConfigManager::onReload);
		EVENT_BUS.addListener(CompostItem::tryUseEvent);
		EVENT_BUS.addListener(CannyComposterEvents::useComposterEvent);
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class,CannyComposter::registerItems);
	}

	public static final ConfigManager SERVER;
	public static final ForgeConfigSpec SERVER_SPEC;

	static {
		final Pair<ConfigManager, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(ConfigManager::new);
		SERVER_SPEC = specPair2.getRight();
		SERVER = specPair2.getLeft();
	}

	public static void registerItems(RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();

		Item.Properties properties = new Item.Properties().group(ItemGroup.MATERIALS);

		itemCompost = new CompostItem(properties);
		itemCompost.setRegistryName("cannycomposter:compost");
		registry.register(itemCompost);
	}

	//@SubscribeEvent
	public void onFingerprintViolation(final FMLFingerprintViolationEvent event)
	{
		getLogger().warn("Invalid fingerprint detected! The file " + event.getSource().getName()
				+ " may have been tampered with. This version will NOT be supported by the author!");
		getLogger().warn("Expected " + event.getExpectedFingerprint() + " found " + event.getFingerprints().toString());
	}
}
