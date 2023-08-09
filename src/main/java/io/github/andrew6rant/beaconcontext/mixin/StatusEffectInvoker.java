package io.github.andrew6rant.beaconcontext.mixin;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StatusEffect.class)
public interface StatusEffectInvoker {
    @Invoker("<init>")
    static StatusEffect init(StatusEffectCategory category, int color) {
        throw new AssertionError();
    }
}
