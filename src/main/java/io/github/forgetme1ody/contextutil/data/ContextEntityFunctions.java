package io.github.forgetme1ody.contextutil.data;

import io.github.forgetme1ody.contextutil.ContextUtilMod;
import io.github.forgetme1ody.contextutil.loot.functions.ContextEntityFunction;
import io.github.forgetme1ody.contextutil.loot.providers.ContextEntityProvider;
import io.github.forgetme1ody.contextutil.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class ContextEntityFunctions {
    public static final ResourceKey<ContextEntityFunction> EQUIP_IRON_HELMET = key("equip_iron_helmet");
    public static final ResourceKey<ContextEntityFunction> MOUNT_ZOMBIE_HORSE = key("mount_zombie_horse");

    private ContextEntityFunctions() {
    }

    public static void bootstrap(BootstrapContext<ContextEntityFunction> context) {
        context.register(EQUIP_IRON_HELMET, ContextEntityFunction.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET)));
        context.register(MOUNT_ZOMBIE_HORSE, ContextEntityFunction.mount(ContextEntityProvider.summon(EntityType.ZOMBIE_HORSE)));
    }

    private static ResourceKey<ContextEntityFunction> key(String name) {
        return ResourceKey.create(Registries.CONTEXT_ENTITY_FUNCTION, ContextUtilMod.id(name));
    }
}
