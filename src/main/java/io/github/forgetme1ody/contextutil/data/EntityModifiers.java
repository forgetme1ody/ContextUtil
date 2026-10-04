package io.github.forgetme1ody.contextutil.data;

import io.github.forgetme1ody.contextutil.ContextUtilMod;
import io.github.forgetme1ody.contextutil.loot.functions.LootEntityFunction;
import io.github.forgetme1ody.contextutil.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class EntityModifiers {
    public static final ResourceKey<LootEntityFunction> EQUIP_IRON_HELMET = key("equip_iron_helmet");

    private EntityModifiers() {
    }

    public static void bootstrap(BootstrapContext<LootEntityFunction> context) {
        context.register(EQUIP_IRON_HELMET, new LootEntityFunction.SetItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET)));
    }

    private static ResourceKey<LootEntityFunction> key(String name) {
        return ResourceKey.create(Registries.ENTITY_MODIFIER, ContextUtilMod.id(name));
    }
}
