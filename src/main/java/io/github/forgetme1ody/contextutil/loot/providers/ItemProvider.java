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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;

import java.util.function.Function;

public interface ItemProvider extends LootContextUser {
    Codec<ItemProvider> DIRECT_CODEC = Codec.lazyInitialized(() -> {
        Codec<ItemProvider> dispatched = BuiltInRegistries.CONTEXT_ITEM_PROVIDER_TYPE.byNameCodec().dispatch(ItemProvider::codec, Function.identity());
        return Codec.either(Constant.INLINE_CODEC, dispatched).xmap(Either::unwrap, ItemProvider::wrap);
    });
    Codec<Holder<ItemProvider>> REFERENCE_CODEC = RegistryFileCodec.create(Registries.CONTEXT_ITEM_PROVIDER, DIRECT_CODEC, true);
    Codec<HolderSet<ItemProvider>> LIST_CODEC = RegistryCodecs.homogeneousList(Registries.CONTEXT_ITEM_PROVIDER, DIRECT_CODEC, true);

    static ItemProvider constant(ItemStack value) {
        return new Constant(value);
    }

    private static Either<Constant, ItemProvider> wrap(ItemProvider provider) {
        return provider instanceof Constant constant ? Either.left(constant) : Either.right(provider);
    }

    ItemStack get(LootContext context);

    MapCodec<? extends ItemProvider> codec();

    record Constant(ItemStack value) implements ItemProvider {
        public static final Codec<Constant> INLINE_CODEC = ItemStack.CODEC.xmap(Constant::new, Constant::value);
        public static final MapCodec<Constant> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemStack.CODEC.fieldOf("value").forGetter(Constant::value)
        ).apply(instance, Constant::new));

        @Override
        public ItemStack get(LootContext context) {
            return this.value;
        }

        @Override
        public MapCodec<? extends ItemProvider> codec() {
            return MAP_CODEC;
        }
    }
}
