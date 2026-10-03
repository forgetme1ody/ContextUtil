package io.github.forgetme1ody.contextutil;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(ContextUtilMod.MOD_ID)
public class ContextUtilMod {
    public static final String MOD_ID = "context_util";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ContextUtilMod(IEventBus modEventBus, ModContainer modContainer) {

    }

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }
}
