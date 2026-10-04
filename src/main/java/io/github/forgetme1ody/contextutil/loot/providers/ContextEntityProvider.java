package io.github.forgetme1ody.contextutil.loot.providers;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.forgetme1ody.contextutil.loot.functions.ContextEntityFunction;
import io.github.forgetme1ody.contextutil.registries.BuiltInRegistries;
import io.github.forgetme1ody.contextutil.registries.Registries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public interface ContextEntityProvider extends LootContextUser {
    Codec<ContextEntityProvider> DIRECT_CODEC = Codec.lazyInitialized(() -> BuiltInRegistries.CONTEXT_ENTITY_PROVIDER_TYPE.byNameCodec().dispatch(ContextEntityProvider::codec, Function.identity()));
    Codec<Holder<ContextEntityProvider>> REFERENCE_CODEC = RegistryFileCodec.create(Registries.CONTEXT_ENTITY_PROVIDER, DIRECT_CODEC, true);
    Codec<HolderSet<ContextEntityProvider>> LIST_CODEC = RegistryCodecs.homogeneousList(Registries.CONTEXT_ENTITY_PROVIDER, DIRECT_CODEC, true);

    static ContextEntityProvider summon(
            EntityType<?> entityType
    ) {
        return new SummonEntity(entityType, Optional.empty(), List.of());
    }

    static ContextEntityProvider summon(
            EntityType<?> entityType,
            ContextEntityFunction... modifiers
    ) {
        return new SummonEntity(entityType, Optional.empty(), List.of(modifiers));
    }

    static ContextEntityProvider summon(
            EntityType<?> entityType,
            @Nullable Holder<ContextLocationProvider> blockPos,
            ContextEntityFunction... modifiers
    ) {
        return new SummonEntity(entityType, Optional.ofNullable(blockPos), List.of(modifiers));
    }

    static ContextEntityProvider thisEntity() {
        return ThisEntity.INSTANCE;
    }

    static ContextEntityProvider attackingEntity() {
        return ThisEntity.INSTANCE;
    }

    static ContextEntityProvider directAttackingEntity() {
        return ThisEntity.INSTANCE;
    }

    private static Either<SummonEntity, ContextEntityProvider> wrap(ContextEntityProvider provider) {
        return provider instanceof SummonEntity summonEntity ? Either.left(summonEntity) : Either.right(provider);
    }

    Entity get(LootContext context);

    MapCodec<? extends ContextEntityProvider> codec();

    record SummonEntity(
            EntityType<?> entityType,
            Optional<Holder<ContextLocationProvider>> position,
            List<ContextEntityFunction> modifiers
    ) implements ContextEntityProvider {
        public static final MapCodec<SummonEntity> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(SummonEntity::entityType),
                ContextLocationProvider.REFERENCE_CODEC.optionalFieldOf("block_pos").forGetter(SummonEntity::position),
                ContextEntityFunction.DIRECT_CODEC.listOf().optionalFieldOf("modifiers", List.of()).forGetter(SummonEntity::modifiers)
        ).apply(instance, SummonEntity::new));

        @Override
        public Entity get(LootContext context) {
            ServerLevel level = context.getLevel();
            BlockPos blockPos = this.position
                    .map(provider -> provider.value().getBlockPos(context))
                    .orElseGet(() -> ContextLocationProvider.origin().getBlockPos(context));
            return this.entityType.spawn(level, blockPos, MobSpawnType.TRIGGERED);
        }

        @Override
        public MapCodec<? extends ContextEntityProvider> codec() {
            return MAP_CODEC;
        }
    }

    record ThisEntity() implements ContextEntityProvider {
        public static final ThisEntity INSTANCE = new ThisEntity();
        public static final MapCodec<ThisEntity> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override
        public Entity get(LootContext context) {
            return context.getParam(LootContextParams.THIS_ENTITY);
        }

        @Override
        public MapCodec<? extends ContextEntityProvider> codec() {
            return MAP_CODEC;
        }

        @Override
        public Set<LootContextParam<?>> getReferencedContextParams() {
            return Set.of(LootContextParams.THIS_ENTITY);
        }
    }

    record AttackingEntity() implements ContextEntityProvider {
        public static final AttackingEntity INSTANCE = new AttackingEntity();
        public static final MapCodec<AttackingEntity> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override
        public Entity get(LootContext context) {
            return context.getParam(LootContextParams.THIS_ENTITY);
        }

        @Override
        public MapCodec<? extends ContextEntityProvider> codec() {
            return MAP_CODEC;
        }

        @Override
        public Set<LootContextParam<?>> getReferencedContextParams() {
            return Set.of(LootContextParams.THIS_ENTITY);
        }
    }

    record DirectAttackingEntity() implements ContextEntityProvider {
        public static final DirectAttackingEntity INSTANCE = new DirectAttackingEntity();
        public static final MapCodec<DirectAttackingEntity> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override
        public Entity get(LootContext context) {
            return context.getParam(LootContextParams.THIS_ENTITY);
        }

        @Override
        public MapCodec<? extends ContextEntityProvider> codec() {
            return MAP_CODEC;
        }

        @Override
        public Set<LootContextParam<?>> getReferencedContextParams() {
            return Set.of(LootContextParams.THIS_ENTITY);
        }
    }

}
