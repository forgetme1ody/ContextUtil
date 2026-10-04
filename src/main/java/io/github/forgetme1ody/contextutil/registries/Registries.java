package io.github.forgetme1ody.contextutil.registries;

import com.mojang.serialization.MapCodec;
import io.github.forgetme1ody.contextutil.ContextUtilMod;
import io.github.forgetme1ody.contextutil.loot.functions.ContextEntityFunction;
import io.github.forgetme1ody.contextutil.loot.functions.ContextFloatFunction;
import io.github.forgetme1ody.contextutil.loot.functions.ContextIntFunction;
import io.github.forgetme1ody.contextutil.loot.providers.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class Registries {
    public static final ResourceKey<Registry<MapCodec<? extends ContextEntityProvider>>> CONTEXT_ENTITY_PROVIDER_TYPE = key("context_entity_provider_type");
    public static final ResourceKey<Registry<MapCodec<? extends ContextItemProvider>>> CONTEXT_ITEM_PROVIDER_TYPE = key("context_item_provider_type");
    public static final ResourceKey<Registry<MapCodec<? extends ContextPositionProvider>>> CONTEXT_POSITION_PROVIDER_TYPE = key("context_position_provider_type");
    public static final ResourceKey<Registry<MapCodec<? extends ContextBlockPosProvider>>> CONTEXT_BLOCK_POS_PROVIDER_TYPE = key("context_block_pos_provider_type");
    public static final ResourceKey<Registry<MapCodec<? extends ContextSoundProvider>>> CONTEXT_SOUND_PROVIDER_TYPE = key("context_sound_provider_type");
    public static final ResourceKey<Registry<MapCodec<? extends ContextEntityFunction>>> CONTEXT_ENTITY_FUNCTION_TYPE = key("context_entity_function_type");
    public static final ResourceKey<Registry<MapCodec<? extends ContextIntFunction>>> CONTEXT_INT_FUNCTION_TYPE = key("context_float_function_type");
    public static final ResourceKey<Registry<MapCodec<? extends ContextFloatFunction>>> CONTEXT_FLOAT_FUNCTION_TYPE = key("context_float_function_type");
    public static final ResourceKey<Registry<ContextEntityProvider>> CONTEXT_ENTITY_PROVIDER = key("context_entity_provider");
    public static final ResourceKey<Registry<ContextItemProvider>> CONTEXT_ITEM_PROVIDER = key("context_item_provider");
    public static final ResourceKey<Registry<ContextPositionProvider>> CONTEXT_POSITION_PROVIDER = key("context_position_provider");
    public static final ResourceKey<Registry<ContextBlockPosProvider>> CONTEXT_BLOCK_POS_PROVIDER = key("context_block_pos_provider");
    public static final ResourceKey<Registry<ContextSoundProvider>> CONTEXT_SOUND_PROVIDER = key("context_sound_provider");
    public static final ResourceKey<Registry<ContextEntityFunction>> CONTEXT_ENTITY_FUNCTION = key("context_entity_function");
    public static final ResourceKey<Registry<ContextIntFunction>> CONTEXT_INT_FUNCTION = key("context_int_function");
    public static final ResourceKey<Registry<ContextFloatFunction>> CONTEXT_FLOAT_FUNCTION = key("context_float_function");

    private Registries() {
    }

    private static <T> ResourceKey<Registry<T>> key(String name) {
        return ResourceKey.createRegistryKey(ContextUtilMod.id(name));
    }
}
