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

public interface ContextFloatFunction extends LootContextUser {
    Codec<ContextFloatFunction> DIRECT_CODEC = Codec.lazyInitialized(() -> BuiltInRegistries.CONTEXT_FLOAT_MODIFIER_TYPE.byNameCodec().dispatch(ContextFloatFunction::codec, Function.identity()));
    Codec<Holder<ContextFloatFunction>> REFERENCE_CODEC = RegistryFileCodec.create(Registries.CONTEXT_FLOAT_FUNCTION, DIRECT_CODEC, true);
    Codec<HolderSet<ContextFloatFunction>> LIST_CODEC = RegistryCodecs.homogeneousList(Registries.CONTEXT_FLOAT_FUNCTION, DIRECT_CODEC, true);

    static ContextFloatFunction add(NumberProvider value) {
        return new AddValue(value);
    }

    static ContextFloatFunction mul(NumberProvider factor) {
        return new MultiplyValue(factor);
    }

    static ContextFloatFunction set(NumberProvider value) {
        return new SetValue(value);
    }

    float applyFloat(LootContext context, float input);

    MapCodec<? extends ContextFloatFunction> codec();

    record AddValue(NumberProvider value) implements ContextFloatFunction {
        public static final MapCodec<AddValue> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                NumberProviders.CODEC.fieldOf("value").forGetter(AddValue::value)
        ).apply(instance, AddValue::new));

        @Override
        public float applyFloat(LootContext context, float input) {
            return input + this.value.getFloat(context);
        }

        @Override
        public MapCodec<? extends ContextFloatFunction> codec() {
            return MAP_CODEC;
        }
    }

    record MultiplyValue(NumberProvider factor) implements ContextFloatFunction {
        public static final MapCodec<MultiplyValue> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                NumberProviders.CODEC.fieldOf("factor").forGetter(MultiplyValue::factor)
        ).apply(instance, MultiplyValue::new));

        @Override
        public float applyFloat(LootContext context, float input) {
            return input * this.factor.getInt(context);
        }

        @Override
        public MapCodec<? extends ContextFloatFunction> codec() {
            return MAP_CODEC;
        }
    }

    record SetValue(NumberProvider value) implements ContextFloatFunction {
        public static final MapCodec<SetValue> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                NumberProviders.CODEC.fieldOf("value").forGetter(SetValue::value)
        ).apply(instance, SetValue::new));

        @Override
        public float applyFloat(LootContext context, float input) {
            return this.value.getFloat(context);
        }

        @Override
        public MapCodec<? extends ContextFloatFunction> codec() {
            return MAP_CODEC;
        }
    }
}
