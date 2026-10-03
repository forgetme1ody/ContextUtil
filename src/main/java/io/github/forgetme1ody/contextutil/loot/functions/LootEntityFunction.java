package io.github.forgetme1ody.contextutil.loot.functions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.forgetme1ody.contextutil.loot.providers.EntityProvider;
import io.github.forgetme1ody.contextutil.loot.providers.LocationProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

import java.util.Optional;

public interface LootEntityFunction extends LootContextUser {

    void apply(LootContext context, Entity entity);

    MapCodec<? extends LootEntityFunction> codec();

    record DamageEntity(
            Holder<DamageType> damageType,
            NumberProvider damageAmount,
            Optional<Holder<EntityProvider>> directEntity,
            Optional<Holder<EntityProvider>> causingEntity,
            Optional<Holder<LocationProvider>> damageSourcePosition
    ) implements LootEntityFunction {
        public static final MapCodec<DamageEntity> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                DamageType.CODEC.fieldOf("damage_type").forGetter(DamageEntity::damageType),
                NumberProviders.CODEC.fieldOf("damage_amount").forGetter(DamageEntity::damageAmount),
                EntityProvider.REFERENCE_CODEC.optionalFieldOf("direct_entity").forGetter(DamageEntity::directEntity),
                EntityProvider.REFERENCE_CODEC.optionalFieldOf("causing_entity").forGetter(DamageEntity::directEntity),
                LocationProvider.REFERENCE_CODEC.optionalFieldOf("damage_source_position").forGetter(DamageEntity::damageSourcePosition)
        ).apply(instance, DamageEntity::new));

        @Override
        public void apply(LootContext context, Entity entity) {
            entity.hurt(
                    new DamageSource(
                            this.damageType,
                            this.directEntity.map(provider -> provider.value().get(context)).orElse(null),
                            this.causingEntity.map(provider -> provider.value().get(context)).orElse(null),
                            damageSourcePosition.map(provider -> provider.value().getPosition(context)).orElse(null)
                    ),
                    this.damageAmount.getFloat(context)
            );
        }

        @Override
        public MapCodec<? extends LootEntityFunction> codec() {
            return null;
        }
    }

    record ApplyMobEffect(
            Holder<MobEffect> effect,
            NumberProvider duration,
            NumberProvider amplifier
    ) implements LootEntityFunction {
        public static final MapCodec<ApplyMobEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("effect").forGetter(ApplyMobEffect::effect),
                NumberProviders.CODEC.fieldOf("duration").forGetter(ApplyMobEffect::duration),
                NumberProviders.CODEC.fieldOf("amplifier").forGetter(ApplyMobEffect::amplifier)
        ).apply(instance, ApplyMobEffect::new));

        @Override
        public void apply(LootContext context, Entity entity) {
            if (entity instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(this.effect, this.duration.getInt(context), this.amplifier.getInt(context)));
            }
        }

        @Override
        public MapCodec<? extends LootEntityFunction> codec() {
            return MAP_CODEC;
        }
    }

    record SetItemSlot(EquipmentSlot slot, ItemStack item) implements LootEntityFunction {
        public static final MapCodec<SetItemSlot> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                EquipmentSlot.CODEC.fieldOf("slot").forGetter(SetItemSlot::slot),
                ItemStack.OPTIONAL_CODEC.fieldOf("item").forGetter(SetItemSlot::item)
        ).apply(instance, SetItemSlot::new));

        @Override
        public void apply(LootContext context, Entity entity) {
            if (entity instanceof Mob mob) {
                mob.setItemSlot(this.slot, this.item.isEmpty() ? this.item : this.item.copy());
            }
        }

        @Override
        public MapCodec<? extends LootEntityFunction> codec() {
            return MAP_CODEC;
        }
    }


}
