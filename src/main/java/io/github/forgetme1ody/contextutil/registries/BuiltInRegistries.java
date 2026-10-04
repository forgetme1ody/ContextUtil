package io.github.forgetme1ody.contextutil.registries;

import com.mojang.serialization.MapCodec;
import io.github.forgetme1ody.contextutil.loot.functions.LootEntityFunction;
import io.github.forgetme1ody.contextutil.loot.functions.LootNumberFunction;
import io.github.forgetme1ody.contextutil.loot.providers.EntityProvider;
import io.github.forgetme1ody.contextutil.loot.providers.ItemProvider;
import io.github.forgetme1ody.contextutil.loot.providers.LocationProvider;
import io.github.forgetme1ody.contextutil.loot.providers.SoundProvider;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;

public final class BuiltInRegistries {
    public static final Registry<MapCodec<? extends LootEntityFunction>> ENTITY_MODIFIER_TYPE = create(Registries.ENTITY_MODIFIER_TYPE);
    public static final Registry<MapCodec<? extends LootNumberFunction>> NUMBER_MODIFIER_TYPE = create(Registries.NUMBER_MODIFIER_TYPE);
    public static final Registry<MapCodec<? extends EntityProvider>> CONTEXT_ENTITY_PROVIDER_TYPE = create(Registries.CONTEXT_ENTITY_PROVIDER_TYPE);
    public static final Registry<MapCodec<? extends ItemProvider>> CONTEXT_ITEM_PROVIDER_TYPE = create(Registries.CONTEXT_ITEM_PROVIDER_TYPE);
    public static final Registry<MapCodec<? extends LocationProvider>> CONTEXT_LOCATION_PROVIDER_TYPE = create(Registries.CONTEXT_LOCATION_PROVIDER_TYPE);
    public static final Registry<MapCodec<? extends SoundProvider>> CONTEXT_SOUND_PROVIDER_TYPE = create(Registries.CONTEXT_SOUND_PROVIDER_TYPE);

    private BuiltInRegistries() {
    }

    private static <T> Registry<T> create(ResourceKey<Registry<T>> key) {
        return new RegistryBuilder<>(key).create();
    }
}
