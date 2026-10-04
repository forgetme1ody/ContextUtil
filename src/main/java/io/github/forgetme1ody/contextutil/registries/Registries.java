package io.github.forgetme1ody.contextutil.registries;

import com.mojang.serialization.MapCodec;
import io.github.forgetme1ody.contextutil.ContextUtilMod;
import io.github.forgetme1ody.contextutil.loot.functions.LootEntityFunction;
import io.github.forgetme1ody.contextutil.loot.functions.LootNumberFunction;
import io.github.forgetme1ody.contextutil.loot.providers.EntityProvider;
import io.github.forgetme1ody.contextutil.loot.providers.ItemProvider;
import io.github.forgetme1ody.contextutil.loot.providers.LocationProvider;
import io.github.forgetme1ody.contextutil.loot.providers.SoundProvider;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class Registries {
    public static final ResourceKey<Registry<MapCodec<? extends EntityProvider>>> CONTEXT_ENTITY_PROVIDER_TYPE = key("context_entity_provider_type");
    public static final ResourceKey<Registry<MapCodec<? extends ItemProvider>>> CONTEXT_ITEM_PROVIDER_TYPE = key("context_item_provider_type");
    public static final ResourceKey<Registry<MapCodec<? extends LocationProvider>>> CONTEXT_LOCATION_PROVIDER_TYPE = key("context_location_provider_type");
    public static final ResourceKey<Registry<MapCodec<? extends SoundProvider>>> CONTEXT_SOUND_PROVIDER_TYPE = key("context_sound_provider_type");
    public static final ResourceKey<Registry<MapCodec<? extends LootEntityFunction>>> ENTITY_MODIFIER_TYPE = key("entity_modifier_type");
    public static final ResourceKey<Registry<MapCodec<? extends LootNumberFunction>>> NUMBER_MODIFIER_TYPE = key("number_modifier_type");
    public static final ResourceKey<Registry<EntityProvider>> CONTEXT_ENTITY_PROVIDER = key("context_entity_provider");
    public static final ResourceKey<Registry<ItemProvider>> CONTEXT_ITEM_PROVIDER = key("context_item_provider");
    public static final ResourceKey<Registry<LocationProvider>> CONTEXT_LOCATION_PROVIDER = key("context_location_provider");
    public static final ResourceKey<Registry<SoundProvider>> CONTEXT_SOUND_PROVIDER = key("context_sound_provider");
    public static final ResourceKey<Registry<LootEntityFunction>> ENTITY_MODIFIER = key("entity_modifier");
    public static final ResourceKey<Registry<LootNumberFunction>> NUMBER_MODIFIER = key("number_modifier");

    private Registries() {
    }

    private static <T> ResourceKey<Registry<T>> key(String name) {
        return ResourceKey.createRegistryKey(ContextUtilMod.id(name));
    }
}
