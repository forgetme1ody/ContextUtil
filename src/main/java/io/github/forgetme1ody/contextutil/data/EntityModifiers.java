package io.github.forgetme1ody.contextutil.data;

import io.github.forgetme1ody.contextutil.ContextUtilMod;
import io.github.forgetme1ody.contextutil.loot.functions.ContextEntityFunction;
import io.github.forgetme1ody.contextutil.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class EntityModifiers {
    public static final ResourceKey<ContextEntityFunction> EQUIP_IRON_HELMET = key("equip_iron_helmet");

    private EntityModifiers() {
    }

    public static void bootstrap(BootstrapContext<ContextEntityFunction> context) {
        context.register(EQUIP_IRON_HELMET, new ContextEntityFunction.SetItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET)));
    }

    private static ResourceKey<ContextEntityFunction> key(String name) {
        return ResourceKey.create(Registries.CONTEXT_ENTITY_FUNCTION, ContextUtilMod.id(name));
    }
}
