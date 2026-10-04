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

public interface ContextLocationProvider extends LootContextUser {
    Codec<ContextLocationProvider> DIRECT_CODEC = Codec.lazyInitialized(() -> BuiltInRegistries.CONTEXT_LOCATION_PROVIDER_TYPE.byNameCodec().dispatch(ContextLocationProvider::codec, Function.identity()));
    Codec<Holder<ContextLocationProvider>> REFERENCE_CODEC = RegistryFileCodec.create(Registries.CONTEXT_LOCATION_PROVIDER, DIRECT_CODEC, true);
    Codec<HolderSet<ContextLocationProvider>> LIST_CODEC = RegistryCodecs.homogeneousList(Registries.CONTEXT_LOCATION_PROVIDER, DIRECT_CODEC, true);

    static ContextLocationProvider origin() {
        return Origin.INSTANCE;
    }

    Vec3 getPosition(LootContext context);

    BlockPos getBlockPos(LootContext context);

    MapCodec<? extends ContextLocationProvider> codec();

    record Origin() implements ContextLocationProvider {
        public static final Origin INSTANCE = new Origin();
        public static final MapCodec<Origin> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override
        public Vec3 getPosition(LootContext context) {
            return context.getParam(LootContextParams.ORIGIN);
        }

        @Override
        public BlockPos getBlockPos(LootContext context) {
            return BlockPos.containing(context.getParam(LootContextParams.ORIGIN));
        }

        @Override
        public MapCodec<? extends ContextLocationProvider> codec() {
            return MAP_CODEC;
        }

        @Override
        public Set<LootContextParam<?>> getReferencedContextParams() {
            return Set.of(LootContextParams.ORIGIN);
        }
    }
}
