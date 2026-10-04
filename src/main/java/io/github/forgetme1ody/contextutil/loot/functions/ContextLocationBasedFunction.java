package io.github.forgetme1ody.contextutil.loot.functions;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;

public interface ContextLocationBasedFunction {
    void apply(LootContext context, Vec3 position);

    MapCodec<? extends ContextLocationBasedFunction> codec();


}
