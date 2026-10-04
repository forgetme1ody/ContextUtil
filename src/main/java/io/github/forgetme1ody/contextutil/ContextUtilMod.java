package io.github.forgetme1ody.contextutil;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.logging.LogUtils;
import io.github.forgetme1ody.contextutil.commands.ModifyCommand;
import io.github.forgetme1ody.contextutil.data.EntityModifiers;
import io.github.forgetme1ody.contextutil.loot.functions.ContextEntityFunction;
import io.github.forgetme1ody.contextutil.loot.functions.ContextFloatFunction;
import io.github.forgetme1ody.contextutil.loot.functions.ContextIntFunction;
import io.github.forgetme1ody.contextutil.loot.providers.*;
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
        event.register(BuiltInRegistries.CONTEXT_INT_MODIFIER_TYPE);
        event.register(BuiltInRegistries.CONTEXT_FLOAT_MODIFIER_TYPE);
        event.register(BuiltInRegistries.CONTEXT_ENTITY_FUNCTION_TYPE);
        event.register(BuiltInRegistries.CONTEXT_ENTITY_PROVIDER_TYPE);
        event.register(BuiltInRegistries.CONTEXT_ITEM_PROVIDER_TYPE);
        event.register(BuiltInRegistries.CONTEXT_POSITION_PROVIDER_TYPE);
        event.register(BuiltInRegistries.CONTEXT_BLOCK_POS_PROVIDER_TYPE);
        event.register(BuiltInRegistries.CONTEXT_SOUND_PROVIDER_TYPE);
    }

    private void registerDataPackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(Registries.CONTEXT_ENTITY_PROVIDER, ContextEntityProvider.DIRECT_CODEC, ContextEntityProvider.DIRECT_CODEC);
        event.dataPackRegistry(Registries.CONTEXT_ITEM_PROVIDER, ContextItemProvider.DIRECT_CODEC, ContextItemProvider.DIRECT_CODEC);
        event.dataPackRegistry(Registries.CONTEXT_POSITION_PROVIDER, ContextPositionProvider.DIRECT_CODEC, ContextPositionProvider.DIRECT_CODEC);
        event.dataPackRegistry(Registries.CONTEXT_BLOCK_POS_PROVIDER, ContextBlockPosProvider.DIRECT_CODEC, ContextBlockPosProvider.DIRECT_CODEC);
        event.dataPackRegistry(Registries.CONTEXT_SOUND_PROVIDER, ContextSoundProvider.DIRECT_CODEC, ContextSoundProvider.DIRECT_CODEC);
        event.dataPackRegistry(Registries.CONTEXT_ENTITY_FUNCTION, ContextEntityFunction.DIRECT_CODEC, ContextEntityFunction.DIRECT_CODEC);
        event.dataPackRegistry(Registries.CONTEXT_INT_FUNCTION, ContextIntFunction.DIRECT_CODEC, ContextIntFunction.DIRECT_CODEC);
        event.dataPackRegistry(Registries.CONTEXT_FLOAT_FUNCTION, ContextFloatFunction.DIRECT_CODEC, ContextFloatFunction.DIRECT_CODEC);
    }

    private void registerContents(RegisterEvent event) {
        event.register(Registries.CONTEXT_ENTITY_PROVIDER_TYPE, context -> {
            context.register(id("this_entity"), ContextEntityProvider.ThisEntity.MAP_CODEC);
            context.register(id("attacking_entity"), ContextEntityProvider.AttackingEntity.MAP_CODEC);
            context.register(id("direct_attacking_entity"), ContextEntityProvider.DirectAttackingEntity.MAP_CODEC);
        });
        event.register(Registries.CONTEXT_ITEM_PROVIDER_TYPE, context -> {
            context.register(id("constant"), ContextItemProvider.Constant.MAP_CODEC);
        });
        event.register(Registries.CONTEXT_POSITION_PROVIDER_TYPE, context -> {
            context.register(id("origin"), ContextPositionProvider.Origin.MAP_CODEC);
        });
        event.register(Registries.CONTEXT_BLOCK_POS_PROVIDER_TYPE, context -> {
            context.register(id("origin"), ContextBlockPosProvider.Origin.MAP_CODEC);
        });
        event.register(Registries.CONTEXT_SOUND_PROVIDER_TYPE, context -> {
            context.register(id("constant"), ContextSoundProvider.Constant.MAP_CODEC);
        });
        event.register(Registries.CONTEXT_ENTITY_FUNCTION_TYPE, context -> {
            context.register(id("all_of"), ContextEntityFunction.AllOf.MAP_CODEC);
            context.register(id("damage"), ContextEntityFunction.Damage.MAP_CODEC);
            context.register(id("apply_mob_effect"), ContextEntityFunction.ApplyMobEffect.MAP_CODEC);
            context.register(id("set_item_slot"), ContextEntityFunction.SetItemSlot.MAP_CODEC);
            context.register(id("ignite"), ContextEntityFunction.Ignite.MAP_CODEC);
            context.register(id("mount"), ContextEntityFunction.Mount.MAP_CODEC);
            context.register(id("dismount"), ContextEntityFunction.Dismount.MAP_CODEC);
        });
        event.register(Registries.CONTEXT_INT_FUNCTION_TYPE, context -> {
            context.register(id("add"), ContextIntFunction.AddValue.MAP_CODEC);
            context.register(id("mul"), ContextIntFunction.MultiplyValue.MAP_CODEC);
            context.register(id("set"), ContextIntFunction.SetValue.MAP_CODEC);
        });
        event.register(Registries.CONTEXT_FLOAT_FUNCTION_TYPE, context -> {
            context.register(id("add"), ContextFloatFunction.AddValue.MAP_CODEC);
            context.register(id("mul"), ContextFloatFunction.MultiplyValue.MAP_CODEC);
            context.register(id("set"), ContextFloatFunction.SetValue.MAP_CODEC);
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
                    .add(Registries.CONTEXT_ENTITY_FUNCTION, EntityModifiers::bootstrap);
            event.createDatapackRegistryObjects(builder);
        }
    }

}
