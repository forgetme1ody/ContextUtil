package io.github.forgetme1ody.contextutil.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.github.forgetme1ody.contextutil.loot.functions.ContextEntityFunction;
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
                        .then(Commands.argument("modifier", ResourceArgument.resource(buildContext, Registries.CONTEXT_ENTITY_FUNCTION))
                                .then(Commands.argument("target", EntityArgument.entities())
                                        .executes(context -> modifyEntity(
                                                        context.getSource(),
                                                        ResourceArgument.getResource(context, "modifier", Registries.CONTEXT_ENTITY_FUNCTION),
                                                        EntityArgument.getEntities(context, "target")
                                                )
                                        )
                                )
                        )

                );
    }

    private static int modifyEntity(
            CommandSourceStack source,
            Holder<ContextEntityFunction> modifier,
            Collection<? extends Entity> entities
    ) throws CommandSyntaxException {
        int size = entities.size();

        if (size == 0) {
            throw ERROR_TARGET_NO_CHANGES.create();
        }

        ServerLevel level = source.getLevel();
        for (Entity entity : entities) {
            LootContext context = new LootContext.Builder(
                    new LootParams.Builder(level)
                            .withParameter(LootContextParams.ORIGIN, entity.position())
                            .withParameter(LootContextParams.THIS_ENTITY, entity)
                            .create(LootContextParamSets.COMMAND)
            ).create(modifier.unwrapKey().map(ResourceKey::location));
            modifier.value().apply(context, entity);
        }

        if (size > 1) {
            source.sendSuccess(() -> Component.literal("已对" + size + "个实体应用修饰器"), false);
        } else {
            source.sendSuccess(() -> Component.literal("已对").append(entities.stream().findFirst().orElseThrow().getDisplayName()).append(Component.literal("应用修饰器")), false);
        }


        return size;
    }
}
