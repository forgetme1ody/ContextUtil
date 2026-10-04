package io.github.forgetme1ody.contextutil.loot.providers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.forgetme1ody.contextutil.registries.BuiltInRegistries;
import io.github.forgetme1ody.contextutil.registries.Registries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.Set;
import java.util.function.Function;

public interface LocationProvider extends LootContextUser {
    Codec<LocationProvider> DIRECT_CODEC = Codec.lazyInitialized(() -> BuiltInRegistries.CONTEXT_LOCATION_PROVIDER_TYPE.byNameCodec().dispatch(LocationProvider::codec, Function.identity()));
    Codec<Holder<LocationProvider>> REFERENCE_CODEC = RegistryFileCodec.create(Registries.CONTEXT_LOCATION_PROVIDER, DIRECT_CODEC, true);
    Codec<HolderSet<LocationProvider>> LIST_CODEC = RegistryCodecs.homogeneousList(Registries.CONTEXT_LOCATION_PROVIDER, DIRECT_CODEC, true);

    static LocationProvider origin() {
        return Origin.INSTANCE;
    }

    Vec3 getPosition(LootContext context);

    MapCodec<? extends LocationProvider> codec();

    default BlockPos getBlockPos(LootContext context) {
        return BlockPos.containing(this.getPosition(context));
    }

    record Origin() implements LocationProvider {
        public static final Origin INSTANCE = new Origin();
        public static final MapCodec<Origin> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override
        public Vec3 getPosition(LootContext context) {
            return context.getParam(LootContextParams.ORIGIN);
        }

        @Override
        public MapCodec<? extends LocationProvider> codec() {
            return MAP_CODEC;
        }

        @Override
        public Set<LootContextParam<?>> getReferencedContextParams() {
            return Set.of(LootContextParams.ORIGIN);
        }
    }
}
