package io.github.forgetme1ody.contextutil.registries;

import io.github.forgetme1ody.contextutil.ContextUtilMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class Registries {
    private Registries() {
    }

    private static <T> ResourceKey<Registry<T>> key(String name) {
        return ResourceKey.createRegistryKey(ContextUtilMod.id(name));
    }
}
