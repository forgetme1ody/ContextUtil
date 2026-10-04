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

public interface ContextIntFunction extends LootContextUser {
    Codec<ContextIntFunction> DIRECT_CODEC = Codec.lazyInitialized(() -> BuiltInRegistries.CONTEXT_INT_MODIFIER_TYPE.byNameCodec().dispatch(ContextIntFunction::codec, Function.identity()));
    Codec<Holder<ContextIntFunction>> REFERENCE_CODEC = RegistryFileCodec.create(Registries.CONTEXT_INT_FUNCTION, DIRECT_CODEC, true);
    Codec<HolderSet<ContextIntFunction>> LIST_CODEC = RegistryCodecs.homogeneousList(Registries.CONTEXT_INT_FUNCTION, DIRECT_CODEC, true);

    static ContextIntFunction add(NumberProvider value) {
        return new AddValue(value);
    }

    static ContextIntFunction mul(NumberProvider value) {
        return new MultiplyValue(value);
    }

    static ContextIntFunction set(NumberProvider value) {
        return new SetValue(value);
    }

    int applyInt(LootContext context, int input);

    MapCodec<? extends ContextIntFunction> codec();

    record AddValue(NumberProvider value) implements ContextIntFunction {
        public static final MapCodec<AddValue> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                NumberProviders.CODEC.fieldOf("value").forGetter(AddValue::value)
        ).apply(instance, AddValue::new));

        @Override
        public int applyInt(LootContext context, int input) {
            return input + this.value.getInt(context);
        }

        @Override
        public MapCodec<? extends ContextIntFunction> codec() {
            return MAP_CODEC;
        }
    }

    record MultiplyValue(NumberProvider factor) implements ContextIntFunction {
        public static final MapCodec<MultiplyValue> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                NumberProviders.CODEC.fieldOf("factor").forGetter(MultiplyValue::factor)
        ).apply(instance, MultiplyValue::new));

        @Override
        public int applyInt(LootContext context, int input) {
            return input * this.factor.getInt(context);
        }

        @Override
        public MapCodec<? extends ContextIntFunction> codec() {
            return MAP_CODEC;
        }
    }

    record SetValue(NumberProvider value) implements ContextIntFunction {
        public static final MapCodec<SetValue> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                NumberProviders.CODEC.fieldOf("value").forGetter(SetValue::value)
        ).apply(instance, SetValue::new));

        @Override
        public int applyInt(LootContext context, int input) {
            return this.value.getInt(context);
        }

        @Override
        public MapCodec<? extends ContextIntFunction> codec() {
            return MAP_CODEC;
        }
    }
}
