package net.pinto.mythandmetal.mathproject;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class GraphCommands {


    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("plotStandard")
                .then(Commands.argument("a", DoubleArgumentType.doubleArg())
                        .then(Commands.argument("b", DoubleArgumentType.doubleArg())
                                .then(Commands.argument("c", DoubleArgumentType.doubleArg())
                                                        .executes(context -> {
                                                            double a = DoubleArgumentType.getDouble(context, "a");
                                                            double b = DoubleArgumentType.getDouble(context, "b");
                                                            double c = DoubleArgumentType.getDouble(context, "c");

                                                            ServerPlayer player = context.getSource().getPlayerOrException();
                                                            Vec3 origin = player.position();

                                                            GraphingLogic.plotStandardForm(player.level(), origin, a, b, c ,0.05);
                                                            return 1;
                                                        })))));

        dispatcher.register(Commands.literal("plotVertex")
                .then(Commands.argument("a", DoubleArgumentType.doubleArg())
                        .then(Commands.argument("h", DoubleArgumentType.doubleArg())
                                .then(Commands.argument("k", DoubleArgumentType.doubleArg())
                                                        .executes(context -> {
                                                            double a = DoubleArgumentType.getDouble(context, "a");
                                                            double h = DoubleArgumentType.getDouble(context, "h");
                                                            double k = DoubleArgumentType.getDouble(context, "k");

                                                            ServerPlayer player = context.getSource().getPlayerOrException();
                                                            Vec3 origin = player.position();

                                                            GraphingLogic.plotVertexForm(player.level(), origin, a, h, k, 0.05);
                                                            return 1;
                                                        })))));
        dispatcher.register(Commands.literal("plotIntercept")
                .then(Commands.argument("a", DoubleArgumentType.doubleArg())
                        .then(Commands.argument("h", DoubleArgumentType.doubleArg())
                                .then(Commands.argument("k", DoubleArgumentType.doubleArg())
                                        .executes(context -> {
                                            double a = DoubleArgumentType.getDouble(context, "a");
                                            double h = DoubleArgumentType.getDouble(context, "h");
                                            double k = DoubleArgumentType.getDouble(context, "k");

                                            ServerPlayer player = context.getSource().getPlayerOrException();
                                            Vec3 origin = player.position();

                                            GraphingLogic.plotInterceptForm(player.level(), origin, a, h, k, 0.05);
                                            return 1;
                                        })))));
    }

}