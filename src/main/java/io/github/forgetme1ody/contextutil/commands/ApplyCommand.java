package io.github.forgetme1ody.contextutil.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.github.forgetme1ody.contextutil.loot.functions.ContextEntityFunction;
import io.github.forgetme1ody.contextutil.loot.functions.ContextIntFunction;
import io.github.forgetme1ody.contextutil.registries.Registries;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;

public final class ApplyCommand {
    private static final SimpleCommandExceptionType ERROR_TARGET_NO_CHANGES = new SimpleCommandExceptionType(
            Component.translatable("commands.contextutil.modify.entity.target.no_changes")
    );

    private ApplyCommand() {
    }

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandBuildContext buildContext) {
        return Commands.literal("apply")
                .then(Commands.literal("entity")
                        .then(Commands.argument("function", ResourceArgument.resource(buildContext, Registries.CONTEXT_ENTITY_FUNCTION))
                                .then(Commands.argument("targets", EntityArgument.entities())
                                        .executes(context -> applyEntity(
                                                        context.getSource(),
                                                        ResourceArgument.getResource(context, "function", Registries.CONTEXT_ENTITY_FUNCTION),
                                                        EntityArgument.getEntities(context, "targets")
                                                )
                                        )
                                )
                        )
                )
                .then(Commands.literal("int")
                        .then(Commands.argument("function", ResourceArgument.resource(buildContext, Registries.CONTEXT_INT_FUNCTION))
                                .then(Commands.argument("input", IntegerArgumentType.integer())
                                        .executes(context -> applyInt(
                                                        context.getSource(),
                                                        ResourceArgument.getResource(context, "function", Registries.CONTEXT_INT_FUNCTION),
                                                        IntegerArgumentType.getInteger(context, "input")
                                                )
                                        )
                                )
                        )
                )
                .then(Commands.literal("float")

                )
                .then(Commands.literal("location_based")

                )
                ;
    }

    private static int applyEntity(
            CommandSourceStack source,
            Holder<ContextEntityFunction> modifier,
            Collection<? extends Entity> entities
    ) throws CommandSyntaxException {
        int size = entities.size();

        if (size == 0) {
            throw ERROR_TARGET_NO_CHANGES.create();
        }

        ServerLevel level = source.getLevel();
        Vec3 position = source.getPosition();
        for (Entity entity : entities) {
            LootContext context = new LootContext.Builder(
                    new LootParams.Builder(level)
                            .withParameter(LootContextParams.ORIGIN, position)
                            .withParameter(LootContextParams.THIS_ENTITY, entity)
                            .create(LootContextParamSets.COMMAND)
            ).create(modifier.unwrapKey().map(ResourceKey::location));
            modifier.value().apply(context, entity);
        }

        if (size > 1) {
            source.sendSuccess(() -> Component.literal("已对" + size + "个实体应用函数"), false);
        } else {
            source.sendSuccess(() -> Component.literal("已对").append(entities.stream().findFirst().orElseThrow().getDisplayName()).append(Component.literal("应用函数")), false);
        }


        return size;
    }

    private static int applyInt(
            CommandSourceStack source,
            Holder<ContextIntFunction> modifier,
            int input
    ) {
        ServerLevel level = source.getLevel();
        Vec3 position = source.getPosition();
        Entity entity = source.getEntity();
        LootContext context = new LootContext.Builder(
                new LootParams.Builder(level)
                        .withParameter(LootContextParams.ORIGIN, position)
                        .withParameter(LootContextParams.THIS_ENTITY, entity)
                        .create(LootContextParamSets.COMMAND)
        ).create(modifier.unwrapKey().map(ResourceKey::location));
        int value = modifier.value().applyInt(context, input);
        source.sendSuccess(() -> Component.literal("已对整数值" + input + "应用函数，结果：" + value), false);
        return value;
    }
}
