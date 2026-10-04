package io.github.forgetme1ody.contextutil.loot.providers;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.forgetme1ody.contextutil.registries.BuiltInRegistries;
import io.github.forgetme1ody.contextutil.registries.Registries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;

import java.util.function.Function;

public interface SoundProvider extends LootContextUser {
    Codec<SoundProvider> DIRECT_CODEC = Codec.lazyInitialized(() -> {
        Codec<SoundProvider> dispatched = BuiltInRegistries.CONTEXT_SOUND_PROVIDER_TYPE.byNameCodec().dispatch(SoundProvider::codec, Function.identity());
        return Codec.either(Constant.INLINE_CODEC, dispatched).xmap(Either::unwrap, SoundProvider::wrap);
    });
    Codec<Holder<SoundProvider>> REFERENCE_CODEC = RegistryFileCodec.create(Registries.CONTEXT_SOUND_PROVIDER, DIRECT_CODEC, true);
    Codec<HolderSet<SoundProvider>> LIST_CODEC = RegistryCodecs.homogeneousList(Registries.CONTEXT_SOUND_PROVIDER, DIRECT_CODEC, true);

    static SoundProvider constant(Holder<SoundEvent> value) {
        return new Constant(value);
    }

    private static Either<Constant, SoundProvider> wrap(SoundProvider provider) {
        return provider instanceof Constant constant ? Either.left(constant) : Either.right(provider);
    }

    Holder<SoundEvent> get(LootContext context);

    MapCodec<? extends SoundProvider> codec();

    record Constant(Holder<SoundEvent> value) implements SoundProvider {
        public static final Codec<Constant> INLINE_CODEC = SoundEvent.CODEC.xmap(Constant::new, Constant::value);
        public static final MapCodec<Constant> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                SoundEvent.CODEC.fieldOf("value").forGetter(Constant::value)
        ).apply(instance, Constant::new));

        @Override
        public Holder<SoundEvent> get(LootContext context) {
            return this.value;
        }

        @Override
        public MapCodec<? extends SoundProvider> codec() {
            return MAP_CODEC;
        }
    }
}
