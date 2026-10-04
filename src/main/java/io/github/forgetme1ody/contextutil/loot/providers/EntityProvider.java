package io.github.forgetme1ody.contextutil.loot.providers;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.forgetme1ody.contextutil.loot.functions.LootEntityFunction;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public interface EntityProvider extends LootContextUser {
    Codec<EntityProvider> DIRECT_CODEC = Codec.lazyInitialized(() -> {
        Codec<EntityProvider> dispatched = BuiltInRegistries.CONTEXT_ENTITY_PROVIDER_TYPE.byNameCodec().dispatch(EntityProvider::codec, Function.identity());
        return Codec.either(SummonEntity.INLINE_CODEC, dispatched).xmap(Either::unwrap, EntityProvider::wrap);
    });
    Codec<Holder<EntityProvider>> REFERENCE_CODEC = RegistryFileCodec.create(Registries.CONTEXT_ENTITY_PROVIDER, DIRECT_CODEC, true);
    Codec<HolderSet<EntityProvider>> LIST_CODEC = RegistryCodecs.homogeneousList(Registries.CONTEXT_ENTITY_PROVIDER, DIRECT_CODEC, true);

    static EntityProvider thisEntity() {
        return ThisEntity.INSTANCE;
    }

    static EntityProvider attackingEntity() {
        return ThisEntity.INSTANCE;
    }

    static EntityProvider directAttackingEntity() {
        return ThisEntity.INSTANCE;
    }

    private static Either<SummonEntity, EntityProvider> wrap(EntityProvider provider) {
        return provider instanceof SummonEntity summonEntity ? Either.left(summonEntity) : Either.right(provider);
    }

    Entity get(LootContext context);

    MapCodec<? extends EntityProvider> codec();

    record SummonEntity(
            EntityType<?> entityType,
            Optional<Holder<LocationProvider>> position,
            List<LootEntityFunction> modifiers
    ) implements EntityProvider {
        public static final Codec<SummonEntity> INLINE_CODEC = net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.byNameCodec().xmap(SummonEntity::new, SummonEntity::entityType);
        public static final MapCodec<SummonEntity> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(SummonEntity::entityType),
                LocationProvider.REFERENCE_CODEC.optionalFieldOf("position").forGetter(SummonEntity::position),
                LootEntityFunction.DIRECT_CODEC.listOf().optionalFieldOf("modifiers", List.of()).forGetter(SummonEntity::modifiers)
        ).apply(instance, SummonEntity::new));

        public SummonEntity(EntityType<?> entityType) {
            this(entityType, Optional.empty(), List.of());
        }

        @Override
        public Entity get(LootContext context) {
            ServerLevel level = context.getLevel();
            BlockPos blockPos = this.position
                    .map(provider -> provider.value().getBlockPos(context))
                    .orElseGet(() -> LocationProvider.origin().getBlockPos(context));
            return this.entityType.spawn(level, blockPos, MobSpawnType.TRIGGERED);
        }

        @Override
        public MapCodec<? extends EntityProvider> codec() {
            return MAP_CODEC;
        }
    }

    record ThisEntity() implements EntityProvider {
        public static final ThisEntity INSTANCE = new ThisEntity();
        public static final MapCodec<ThisEntity> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override
        public Entity get(LootContext context) {
            return context.getParam(LootContextParams.THIS_ENTITY);
        }

        @Override
        public MapCodec<? extends EntityProvider> codec() {
            return MAP_CODEC;
        }

        @Override
        public Set<LootContextParam<?>> getReferencedContextParams() {
            return Set.of(LootContextParams.THIS_ENTITY);
        }
    }

    record AttackingEntity() implements EntityProvider {
        public static final AttackingEntity INSTANCE = new AttackingEntity();
        public static final MapCodec<AttackingEntity> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override
        public Entity get(LootContext context) {
            return context.getParam(LootContextParams.THIS_ENTITY);
        }

        @Override
        public MapCodec<? extends EntityProvider> codec() {
            return MAP_CODEC;
        }

        @Override
        public Set<LootContextParam<?>> getReferencedContextParams() {
            return Set.of(LootContextParams.THIS_ENTITY);
        }
    }

    record DirectAttackingEntity() implements EntityProvider {
        public static final DirectAttackingEntity INSTANCE = new DirectAttackingEntity();
        public static final MapCodec<DirectAttackingEntity> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override
        public Entity get(LootContext context) {
            return context.getParam(LootContextParams.THIS_ENTITY);
        }

        @Override
        public MapCodec<? extends EntityProvider> codec() {
            return MAP_CODEC;
        }

        @Override
        public Set<LootContextParam<?>> getReferencedContextParams() {
            return Set.of(LootContextParams.THIS_ENTITY);
        }
    }

}
