package io.github.forgetme1ody.contextutil.data;

import io.github.forgetme1ody.contextutil.ContextUtilMod;
import io.github.forgetme1ody.contextutil.loot.functions.ContextIntFunction;
import io.github.forgetme1ody.contextutil.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public final class ContextIntFunctions {
    public static final ResourceKey<ContextIntFunction> ADD_2 = key("add_2");

    private ContextIntFunctions() {
    }

    public static void bootstrap(BootstrapContext<ContextIntFunction> context) {
        context.register(ADD_2, ContextIntFunction.add(new ConstantValue(2)));
    }

    private static ResourceKey<ContextIntFunction> key(String name) {
        return ResourceKey.create(Registries.CONTEXT_INT_FUNCTION, ContextUtilMod.id(name));
    }
}
