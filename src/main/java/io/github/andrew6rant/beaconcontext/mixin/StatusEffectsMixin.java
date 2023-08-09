package io.github.andrew6rant.beaconcontext.mixin;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.InstantStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

@Mixin(StatusEffects.class)
public class StatusEffectsMixin {

    @ModifyVariable(method = "register(ILjava/lang/String;Lnet/minecraft/entity/effect/StatusEffect;)Lnet/minecraft/entity/effect/StatusEffect;", at = @At(value = "HEAD"), argsOnly = true)
    private static StatusEffect beaconcontext$modifyEffect(StatusEffect effect) {
        if (effect instanceof InstantStatusEffect) return effect;
        if (effect.getAttributeModifiers().isEmpty()) return effect;

        StatusEffect newStatus = StatusEffectInvoker.init(effect.getCategory(), effect.getColor());

        for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : effect.getAttributeModifiers().entrySet()) {
            EntityAttributeModifier value = entry.getValue();
            double amount = value.getValue(); // I know it looks weird, but the first getValue() is to get the EntityAttributeModifier, and the second is to get the amount.
            if (amount != 0.0) {
                amount = amount / 100.0;
                newStatus.addAttributeModifier(entry.getKey(), value.getId().toString(), amount, value.getOperation());
            }
        }

        return newStatus;
    }
}