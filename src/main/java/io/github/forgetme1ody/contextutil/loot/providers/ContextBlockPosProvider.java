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

import java.util.Set;
import java.util.function.Function;

public interface ContextBlockPosProvider extends LootContextUser {
    Codec<ContextBlockPosProvider> DIRECT_CODEC = Codec.lazyInitialized(() -> BuiltInRegistries.CONTEXT_BLOCK_POS_PROVIDER_TYPE.byNameCodec().dispatch(ContextBlockPosProvider::codec, Function.identity()));
    Codec<Holder<ContextBlockPosProvider>> REFERENCE_CODEC = RegistryFileCodec.create(Registries.CONTEXT_BLOCK_POS_PROVIDER, DIRECT_CODEC, true);
    Codec<HolderSet<ContextBlockPosProvider>> LIST_CODEC = RegistryCodecs.homogeneousList(Registries.CONTEXT_BLOCK_POS_PROVIDER, DIRECT_CODEC, true);

    static ContextBlockPosProvider origin() {
        return Origin.INSTANCE;
    }

    BlockPos getBlockPos(LootContext context);

    MapCodec<? extends ContextBlockPosProvider> codec();

    record Origin() implements ContextBlockPosProvider {
        public static final Origin INSTANCE = new Origin();
        public static final MapCodec<Origin> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override
        public BlockPos getBlockPos(LootContext context) {
            return BlockPos.containing(context.getParam(LootContextParams.ORIGIN));
        }

        @Override
        public MapCodec<? extends ContextBlockPosProvider> codec() {
            return MAP_CODEC;
        }

        @Override
        public Set<LootContextParam<?>> getReferencedContextParams() {
            return Set.of(LootContextParams.ORIGIN);
        }
    }
}
