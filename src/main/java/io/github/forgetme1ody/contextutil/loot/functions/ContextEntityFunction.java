package io.github.forgetme1ody.contextutil.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.forgetme1ody.contextutil.loot.providers.ContextEntityProvider;
import io.github.forgetme1ody.contextutil.loot.providers.ContextPositionProvider;
import io.github.forgetme1ody.contextutil.registries.Registries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface ContextEntityFunction extends LootContextUser {
    Codec<ContextEntityFunction> DIRECT_CODEC = Codec.lazyInitialized(() -> io.github.forgetme1ody.contextutil.registries.BuiltInRegistries.CONTEXT_ENTITY_FUNCTION_TYPE.byNameCodec().dispatch(ContextEntityFunction::codec, Function.identity()));
    Codec<Holder<ContextEntityFunction>> REFERENCE_CODEC = RegistryFileCodec.create(Registries.CONTEXT_ENTITY_FUNCTION, DIRECT_CODEC, true);
    Codec<HolderSet<ContextEntityFunction>> LIST_CODEC = RegistryCodecs.homogeneousList(Registries.CONTEXT_ENTITY_FUNCTION, DIRECT_CODEC, true);

    static ContextEntityFunction damageEntity(
            Holder<DamageType> damageType,
            NumberProvider damageAmount
    ) {
        return damageEntity(
                damageType,
                damageAmount,
                null,
                null,
                null
        );
    }

    static ContextEntityFunction damageEntity(
            Holder<DamageType> damageType,
            NumberProvider damageAmount,
            @Nullable Holder<ContextEntityProvider> directEntity
    ) {
        return damageEntity(
                damageType,
                damageAmount,
                directEntity,
                null,
                null
        );
    }

    static ContextEntityFunction damageEntity(
            Holder<DamageType> damageType,
            NumberProvider damageAmount,
            @Nullable Holder<ContextEntityProvider> directEntity,
            @Nullable Holder<ContextEntityProvider> causingEntity
    ) {
        return damageEntity(
                damageType,
                damageAmount,
                directEntity,
                causingEntity,
                null
        );
    }

    static ContextEntityFunction damageEntity(
            Holder<DamageType> damageType,
            NumberProvider damageAmount,
            @Nullable Holder<ContextEntityProvider> directEntity,
            @Nullable Holder<ContextEntityProvider> causingEntity,
            @Nullable Holder<ContextPositionProvider> damageSourcePosition
    ) {
        return new Damage(
                damageType,
                damageAmount,
                Optional.ofNullable(directEntity),
                Optional.ofNullable(causingEntity),
                Optional.ofNullable(damageSourcePosition)
        );
    }

    static ContextEntityFunction applyMobEffect(
            Holder<MobEffect> effect
    ) {
        return new ApplyMobEffect(effect, null, null);
    }

    static ContextEntityFunction applyMobEffect(
            Holder<MobEffect> effect,
            @Nullable NumberProvider duration
    ) {
        return new ApplyMobEffect(effect, Optional.ofNullable(duration), null);
    }

    static ContextEntityFunction applyMobEffect(
            Holder<MobEffect> effect,
            @Nullable NumberProvider duration,
            @Nullable NumberProvider amplifier
    ) {
        return new ApplyMobEffect(effect, Optional.ofNullable(duration), Optional.ofNullable(amplifier));
    }

    static ContextEntityFunction ignite(NumberProvider duration) {
        return new Ignite(duration);
    }

    static ContextEntityFunction setItemSlot(EquipmentSlot slot, Item item) {
        return new SetItemSlot(slot, new ItemStack(item));
    }

    static ContextEntityFunction setItemSlot(EquipmentSlot slot, ItemStack item) {
        return new SetItemSlot(slot, item);
    }

    static ContextEntityFunction mount(ContextEntityProvider mount) {
        return new Mount(Holder.direct(mount));
    }

    static ContextEntityFunction mount(Holder<ContextEntityProvider> mount) {
        return new Mount(mount);
    }

    static ContextEntityFunction dismount() {
        return Dismount.INSTANCE;
    }

    void apply(LootContext context, Entity entity);

    MapCodec<? extends ContextEntityFunction> codec();

    record AllOf(List<ContextEntityFunction> modifiers) implements ContextEntityFunction {
        public static final MapCodec<AllOf> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ContextEntityFunction.DIRECT_CODEC.listOf().fieldOf("modifiers").forGetter(AllOf::modifiers)
        ).apply(instance, AllOf::new));

        @Override
        public void apply(LootContext context, Entity entity) {
            for (ContextEntityFunction modifier : this.modifiers) {
                modifier.apply(context, entity);
            }
        }

        @Override
        public MapCodec<? extends ContextEntityFunction> codec() {
            return MAP_CODEC;
        }
    }

    record Damage(
            Holder<DamageType> damageType,
            NumberProvider amount,
            Optional<Holder<ContextEntityProvider>> directEntity,
            Optional<Holder<ContextEntityProvider>> causingEntity,
            Optional<Holder<ContextPositionProvider>> damageSourcePosition
    ) implements ContextEntityFunction {
        public static final MapCodec<Damage> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                DamageType.CODEC.fieldOf("damage_type").forGetter(Damage::damageType),
                NumberProviders.CODEC.fieldOf("amount").forGetter(Damage::amount),
                ContextEntityProvider.REFERENCE_CODEC.optionalFieldOf("direct_entity").forGetter(Damage::directEntity),
                ContextEntityProvider.REFERENCE_CODEC.optionalFieldOf("causing_entity").forGetter(Damage::directEntity),
                ContextPositionProvider.REFERENCE_CODEC.optionalFieldOf("damage_source_position").forGetter(Damage::damageSourcePosition)
        ).apply(instance, Damage::new));

        @Override
        public void apply(LootContext context, Entity entity) {
            entity.hurt(
                    new DamageSource(
                            this.damageType,
                            this.directEntity.map(provider -> provider.value().get(context)).orElse(null),
                            this.causingEntity.map(provider -> provider.value().get(context)).orElse(null),
                            damageSourcePosition.map(provider -> provider.value().getPosition(context)).orElse(null)
                    ),
                    this.amount.getFloat(context)
            );
        }

        @Override
        public MapCodec<? extends ContextEntityFunction> codec() {
            return null;
        }
    }

    record ApplyMobEffect(
            Holder<MobEffect> effect,
            Optional<NumberProvider> duration,
            Optional<NumberProvider> amplifier
    ) implements ContextEntityFunction {
        public static final MapCodec<ApplyMobEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("effect").forGetter(ApplyMobEffect::effect),
                NumberProviders.CODEC.optionalFieldOf("duration").forGetter(ApplyMobEffect::duration),
                NumberProviders.CODEC.optionalFieldOf("amplifier").forGetter(ApplyMobEffect::amplifier)
        ).apply(instance, ApplyMobEffect::new));

        @Override
        public void apply(LootContext context, Entity entity) {
            if (entity instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(
                                this.effect,
                                this.duration.map(provider -> Math.min(0, provider.getInt(context)) * 20).orElse(-1),
                                this.amplifier.map(provider -> provider.getInt(context)).orElse(0)
                        )
                );
            }
        }

        @Override
        public MapCodec<? extends ContextEntityFunction> codec() {
            return MAP_CODEC;
        }
    }

    record SetItemSlot(EquipmentSlot slot, ItemStack item) implements ContextEntityFunction {
        public static final MapCodec<SetItemSlot> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                EquipmentSlot.CODEC.fieldOf("slot").forGetter(SetItemSlot::slot),
                ItemStack.OPTIONAL_CODEC.fieldOf("item").forGetter(SetItemSlot::item)
        ).apply(instance, SetItemSlot::new));

        @Override
        public void apply(LootContext context, Entity entity) {
            if (entity instanceof Mob mob) {
                mob.setItemSlot(this.slot, this.item.copy());
            }
        }

        @Override
        public MapCodec<? extends ContextEntityFunction> codec() {
            return MAP_CODEC;
        }
    }

    record Ignite(NumberProvider duration) implements ContextEntityFunction {
        public static final MapCodec<Ignite> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                NumberProviders.CODEC.fieldOf("duration").forGetter(Ignite::duration)
        ).apply(instance, Ignite::new));

        @Override
        public void apply(LootContext context, Entity entity) {
            entity.igniteForSeconds(this.duration.getInt(context));
        }

        @Override
        public MapCodec<? extends ContextEntityFunction> codec() {
            return MAP_CODEC;
        }
    }

    record Mount(
            Holder<ContextEntityProvider> vehicle
    ) implements ContextEntityFunction {
        public static final MapCodec<Mount> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ContextEntityProvider.REFERENCE_CODEC.fieldOf("vehicle").forGetter(Mount::vehicle)
        ).apply(instance, Mount::new));

        @Override
        public void apply(LootContext context, Entity entity) {
            Entity vehicle = this.vehicle.value().get(context);
            entity.startRiding(vehicle, true);
        }

        @Override
        public MapCodec<? extends ContextEntityFunction> codec() {
            return MAP_CODEC;
        }
    }

    record Dismount() implements ContextEntityFunction {
        public static final Dismount INSTANCE = new Dismount();
        public static final MapCodec<Dismount> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override
        public void apply(LootContext context, Entity entity) {
            entity.stopRiding();
        }

        @Override
        public MapCodec<? extends ContextEntityFunction> codec() {
            return MAP_CODEC;
        }
    }
}
