package io.github.forgetme1ody.contextutil.loot.providers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;

public interface ItemProvider extends LootContextUser {
    ItemStack get(LootContext context);

    MapCodec<? extends ItemProvider> codec();

    record Constant(ItemStack value) implements ItemProvider {
        public static final MapCodec<Constant> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemStack.OPTIONAL_CODEC.fieldOf("value").forGetter(Constant::value)
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
