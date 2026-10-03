package io.github.forgetme1ody.contextutil.loot.providers;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;

public interface SoundProvider extends LootContextUser {
    Holder<SoundEvent> get(LootContext context);

    MapCodec<? extends SoundProvider> codec();

    record Constant(Holder<SoundEvent> value) implements SoundProvider {

        @Override
        public Holder<SoundEvent> get(LootContext context) {
            return this.value;
        }

        @Override
        public MapCodec<? extends SoundProvider> codec() {
            return null;
        }
    }
}
