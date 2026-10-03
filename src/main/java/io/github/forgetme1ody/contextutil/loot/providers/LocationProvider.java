package io.github.forgetme1ody.contextutil.loot.providers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;
import net.minecraft.world.phys.Vec3;

public interface LocationProvider extends LootContextUser {
    Codec<LocationProvider> DIRECT_CODEC;
    Codec<Holder<LocationProvider>> REFERENCE_CODEC;

    Vec3 getPosition(LootContext context);

    MapCodec<? extends LocationProvider> codec();

    default BlockPos getBlockPos(LootContext context) {
        return BlockPos.containing(this.getPosition(context));
    }
}
