package io.github.forgetme1ody.contextutil.registries;

import com.mojang.serialization.MapCodec;
import io.github.forgetme1ody.contextutil.loot.functions.ContextEntityFunction;
import io.github.forgetme1ody.contextutil.loot.functions.ContextFloatFunction;
import io.github.forgetme1ody.contextutil.loot.functions.ContextIntFunction;
import io.github.forgetme1ody.contextutil.loot.providers.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;

public final class BuiltInRegistries {
    public static final Registry<MapCodec<? extends ContextEntityProvider>> CONTEXT_ENTITY_PROVIDER_TYPE = create(Registries.CONTEXT_ENTITY_PROVIDER_TYPE);
    public static final Registry<MapCodec<? extends ContextItemProvider>> CONTEXT_ITEM_PROVIDER_TYPE = create(Registries.CONTEXT_ITEM_PROVIDER_TYPE);
    public static final Registry<MapCodec<? extends ContextPositionProvider>> CONTEXT_POSITION_PROVIDER_TYPE = create(Registries.CONTEXT_POSITION_PROVIDER_TYPE);
    public static final Registry<MapCodec<? extends ContextBlockPosProvider>> CONTEXT_BLOCK_POS_PROVIDER_TYPE = create(Registries.CONTEXT_BLOCK_POS_PROVIDER_TYPE);
    public static final Registry<MapCodec<? extends ContextSoundProvider>> CONTEXT_SOUND_PROVIDER_TYPE = create(Registries.CONTEXT_SOUND_PROVIDER_TYPE);
    public static final Registry<MapCodec<? extends ContextEntityFunction>> CONTEXT_ENTITY_FUNCTION_TYPE = create(Registries.CONTEXT_ENTITY_FUNCTION_TYPE);
    public static final Registry<MapCodec<? extends ContextIntFunction>> CONTEXT_INT_MODIFIER_TYPE = create(Registries.CONTEXT_INT_FUNCTION_TYPE);
    public static final Registry<MapCodec<? extends ContextFloatFunction>> CONTEXT_FLOAT_MODIFIER_TYPE = create(Registries.CONTEXT_FLOAT_FUNCTION_TYPE);

    private BuiltInRegistries() {
    }

    private static <T> Registry<T> create(ResourceKey<Registry<T>> key) {
        return new RegistryBuilder<>(key).create();
    }
}
