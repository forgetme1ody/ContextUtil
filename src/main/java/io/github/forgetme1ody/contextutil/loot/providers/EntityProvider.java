package io.github.forgetme1ody.contextutil.loot.providers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;

public interface EntityProvider extends LootContextUser {
    Codec<EntityProvider> DIRECT_CODEC;
    Codec<Holder<EntityProvider>> REFERENCE_CODEC;

    Entity get(LootContext context);

    MapCodec<? extends EntityProvider> codec();
}
