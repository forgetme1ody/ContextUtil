package io.github.forgetme1ody.contextutil.loot.functions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

public interface LootNumberFunction extends LootContextUser {

    static LootNumberFunction addValue(NumberProvider value) {
        return new AddValue(value);
    }

    static LootNumberFunction multiplyValue(NumberProvider factor) {
        return new MultiplyValue(factor);
    }

    static LootNumberFunction removeBinomial(NumberProvider chance) {
        return new RemoveBinomial(chance);
    }

    static LootNumberFunction setValue(NumberProvider value) {
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
