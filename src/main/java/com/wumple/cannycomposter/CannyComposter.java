package com.wumple.cannycomposter;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLFingerprintViolationEvent;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

@Mod(Reference.MOD_ID)
public class CannyComposter
{
	public Logger getLogger()
	{
		return LogManager.getLogger(Reference.MOD_ID);
	}
	
	public CannyComposter() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(ConfigManager::onReload);
		EVENT_BUS.addListener(CompostItem::tryUseEvent);
		EVENT_BUS.addListener(CannyComposterEvent::useComposterEvent);
	}

	public static final ConfigManager SERVER;
	public static final ForgeConfigSpec SERVER_SPEC;

	static {
		final Pair<ConfigManager, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(ConfigManager::new);
		SERVER_SPEC = specPair2.getRight();
		SERVER = specPair2.getLeft();
	}

	//@SubscribeEvent
	public void onFingerprintViolation(final FMLFingerprintViolationEvent event)
	{
		getLogger().warn("Invalid fingerprint detected! The file " + event.getSource().getName()
				+ " may have been tampered with. This version will NOT be supported by the author!");
		getLogger().warn("Expected " + event.getExpectedFingerprint() + " found " + event.getFingerprints().toString());
	}
}
