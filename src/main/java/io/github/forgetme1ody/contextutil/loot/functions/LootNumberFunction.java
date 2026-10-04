package io.github.forgetme1ody.contextutil.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.forgetme1ody.contextutil.registries.BuiltInRegistries;
import io.github.forgetme1ody.contextutil.registries.Registries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

import java.util.function.Function;

public interface LootNumberFunction extends LootContextUser {
    Codec<LootNumberFunction> DIRECT_CODEC = Codec.lazyInitialized(() -> BuiltInRegistries.NUMBER_MODIFIER_TYPE.byNameCodec().dispatch(LootNumberFunction::codec, Function.identity()));
    Codec<Holder<LootNumberFunction>> REFERENCE_CODEC = RegistryFileCodec.create(Registries.NUMBER_MODIFIER, DIRECT_CODEC, true);
    Codec<HolderSet<LootNumberFunction>> LIST_CODEC = RegistryCodecs.homogeneousList(Registries.NUMBER_MODIFIER, DIRECT_CODEC, true);

    static LootNumberFunction add(NumberProvider value) {
        return new AddValue(value);
    }

    static LootNumberFunction mul(NumberProvider factor) {
        return new MultiplyValue(factor);
    }

    static LootNumberFunction set(NumberProvider value) {
        return new SetValue(value);
    }

    int applyInt(LootContext context, int input);

    float applyFloat(LootContext context, float input);

    MapCodec<? extends LootNumberFunction> codec();

    record AddValue(NumberProvider value) implements LootNumberFunction {
        public static final MapCodec<AddValue> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                NumberProviders.CODEC.fieldOf("value").forGetter(AddValue::value)
        ).apply(instance, AddValue::new));

        @Override
        public int applyInt(LootContext context, int input) {
            return input + this.value.getInt(context);
        }

        @Override
        public float applyFloat(LootContext context, float input) {
            return input + this.value.getFloat(context);
        }

        @Override
        public MapCodec<? extends LootNumberFunction> codec() {
            return MAP_CODEC;
        }
    }

    record MultiplyValue(NumberProvider factor) implements LootNumberFunction {
        public static final MapCodec<MultiplyValue> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                NumberProviders.CODEC.fieldOf("factor").forGetter(MultiplyValue::factor)
        ).apply(instance, MultiplyValue::new));

        @Override
        public int applyInt(LootContext context, int input) {
            return input * this.factor.getInt(context);
        }

        @Override
        public float applyFloat(LootContext context, float input) {
            return input * this.factor.getInt(context);
        }

        @Override
        public MapCodec<? extends LootNumberFunction> codec() {
            return MAP_CODEC;
        }
    }

    record RemoveBinomial(NumberProvider chance) implements LootNumberFunction {
        public static final MapCodec<RemoveBinomial> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                NumberProviders.CODEC.fieldOf("chance").forGetter(RemoveBinomial::chance)
        ).apply(instance, RemoveBinomial::new));

        @Override
        public int applyInt(LootContext context, int input) {
            float chance = this.chance.getFloat(context);
            int value = 0;
            for (int i = 0; i < input; i++) {
                if (context.getRandom().nextFloat() < chance) {
                    value++;
                }
            }

            return input - value;
        }

        @Override
        public float applyFloat(LootContext context, float input) {
            float chance = this.chance.getFloat(context);
            int value = 0;
            for (int i = 0; i < input; i++) {
                if (context.getRandom().nextFloat() < chance) {
                    value++;
                }
            }

            return input - value;
        }

        @Override
        public MapCodec<? extends LootNumberFunction> codec() {
            return null;
        }
    }

    record SetValue(NumberProvider value) implements LootNumberFunction {
        public static final MapCodec<SetValue> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                NumberProviders.CODEC.fieldOf("value").forGetter(SetValue::value)
        ).apply(instance, SetValue::new));

        @Override
        public int applyInt(LootContext context, int input) {
            return this.value.getInt(context);
        }

        @Override
        public float applyFloat(LootContext context, float input) {
            return this.value.getFloat(context);
        }

        @Override
        public MapCodec<? extends LootNumberFunction> codec() {
            return MAP_CODEC;
        }
    }
}
