package io.github.forgetme1ody.contextutil;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.logging.LogUtils;
import io.github.forgetme1ody.contextutil.commands.ModifyCommand;
import io.github.forgetme1ody.contextutil.data.EntityModifiers;
import io.github.forgetme1ody.contextutil.loot.functions.LootEntityFunction;
import io.github.forgetme1ody.contextutil.loot.functions.LootNumberFunction;
import io.github.forgetme1ody.contextutil.loot.providers.EntityProvider;
import io.github.forgetme1ody.contextutil.loot.providers.ItemProvider;
import io.github.forgetme1ody.contextutil.loot.providers.LocationProvider;
import io.github.forgetme1ody.contextutil.loot.providers.SoundProvider;
import io.github.forgetme1ody.contextutil.registries.BuiltInRegistries;
import io.github.forgetme1ody.contextutil.registries.Registries;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;

@Mod(ContextUtilMod.MOD_ID)
public class ContextUtilMod {
    public static final String MOD_ID = "context_util";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ContextUtilMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerRegistries);
        modEventBus.addListener(this::registerDataPackRegistries);
        modEventBus.addListener(this::registerContents);
        modEventBus.addListener(this::registerDataProviders);
        NeoForge.EVENT_BUS.addListener(this::registerCommands);
    }

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    private void registerRegistries(NewRegistryEvent event) {
        event.register(BuiltInRegistries.ENTITY_MODIFIER_TYPE);
        event.register(BuiltInRegistries.NUMBER_MODIFIER_TYPE);
        event.register(BuiltInRegistries.CONTEXT_ENTITY_PROVIDER_TYPE);
        event.register(BuiltInRegistries.CONTEXT_ITEM_PROVIDER_TYPE);
        event.register(BuiltInRegistries.CONTEXT_LOCATION_PROVIDER_TYPE);
        event.register(BuiltInRegistries.CONTEXT_SOUND_PROVIDER_TYPE);
    }

    private void registerDataPackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(Registries.ENTITY_MODIFIER, LootEntityFunction.DIRECT_CODEC, LootEntityFunction.DIRECT_CODEC);
        event.dataPackRegistry(Registries.NUMBER_MODIFIER, LootNumberFunction.DIRECT_CODEC, LootNumberFunction.DIRECT_CODEC);
        event.dataPackRegistry(Registries.CONTEXT_ENTITY_PROVIDER, EntityProvider.DIRECT_CODEC, EntityProvider.DIRECT_CODEC);
        event.dataPackRegistry(Registries.CONTEXT_ITEM_PROVIDER, ItemProvider.DIRECT_CODEC, ItemProvider.DIRECT_CODEC);
        event.dataPackRegistry(Registries.CONTEXT_LOCATION_PROVIDER, LocationProvider.DIRECT_CODEC, LocationProvider.DIRECT_CODEC);
        event.dataPackRegistry(Registries.CONTEXT_SOUND_PROVIDER, SoundProvider.DIRECT_CODEC, SoundProvider.DIRECT_CODEC);
    }

    private void registerContents(RegisterEvent event) {
        event.register(Registries.ENTITY_MODIFIER_TYPE, context -> {
            context.register(id("all_of"), LootEntityFunction.AllOf.MAP_CODEC);
            context.register(id("damage"), LootEntityFunction.Damage.MAP_CODEC);
            context.register(id("apply_mob_effect"), LootEntityFunction.ApplyMobEffect.MAP_CODEC);
            context.register(id("set_item_slot"), LootEntityFunction.SetItemSlot.MAP_CODEC);
            context.register(id("ignite"), LootEntityFunction.Ignite.MAP_CODEC);
            context.register(id("mount"), LootEntityFunction.Mount.MAP_CODEC);
            context.register(id("dismount"), LootEntityFunction.Dismount.MAP_CODEC);
        });
        event.register(Registries.NUMBER_MODIFIER_TYPE, context -> {
            context.register(id("add"), LootNumberFunction.AddValue.MAP_CODEC);
            context.register(id("mul"), LootNumberFunction.MultiplyValue.MAP_CODEC);
            context.register(id("set"), LootNumberFunction.SetValue.MAP_CODEC);
        });
        event.register(Registries.CONTEXT_ENTITY_PROVIDER_TYPE, context -> {
            context.register(id("this_entity"), EntityProvider.ThisEntity.MAP_CODEC);
            context.register(id("attacking_entity"), EntityProvider.AttackingEntity.MAP_CODEC);
            context.register(id("direct_attacking_entity"), EntityProvider.DirectAttackingEntity.MAP_CODEC);
        });
        event.register(Registries.CONTEXT_ITEM_PROVIDER_TYPE, context -> {
            context.register(id("constant"), ItemProvider.Constant.MAP_CODEC);
        });
        event.register(Registries.CONTEXT_LOCATION_PROVIDER_TYPE, context -> {
            context.register(id("origin"), LocationProvider.Origin.MAP_CODEC);
        });
        event.register(Registries.CONTEXT_SOUND_PROVIDER_TYPE, context -> {
            context.register(id("constant"), SoundProvider.Constant.MAP_CODEC);
        });
    }

    private void registerCommands(RegisterCommandsEvent event) {
        CommandBuildContext buildContext = event.getBuildContext();
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        LiteralArgumentBuilder<CommandSourceStack> commandBuilder = Commands.literal(MOD_ID).requires(stack -> stack.hasPermission(2));
        dispatcher.register(
                commandBuilder
                        .then(ModifyCommand.register(buildContext))
        );
    }

    private void registerDataProviders(GatherDataEvent event) {
        ExistingFileHelper helper = event.getExistingFileHelper();
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        if (event.includeServer()) {
            RegistrySetBuilder builder = new RegistrySetBuilder()
                    .add(Registries.ENTITY_MODIFIER, EntityModifiers::bootstrap);
            event.createDatapackRegistryObjects(builder);
        }
    }

}
