package io.github.andrew6rant.beaconcontext.mixin;

import net.minecraft.server.command.EffectCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EffectCommand.class)
public class EffectCommandMixin {
    @ModifyConstant(method = "register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V",
            constant = @Constant(intValue = 255))
    private static int beaconcontext$maxLevel(int constant) {
        return 25500;
    }
}
