package io.github.andrew6rant.beaconcontext.mixin;

import net.minecraft.entity.effect.InstantStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(Potions.class)
public class PotionsMixin {

    @Unique private static String savedName = "";

    @Inject(method = "register(Ljava/lang/String;Lnet/minecraft/potion/Potion;)Lnet/minecraft/potion/Potion;", at = @At(value = "HEAD"))
    private static void beaconcontext$savePotionName(String name, Potion potion, CallbackInfoReturnable<Potion> cir) {
        savedName = name;
    }

    @ModifyVariable(method = "register(Ljava/lang/String;Lnet/minecraft/potion/Potion;)Lnet/minecraft/potion/Potion;", at = @At(value = "HEAD", shift = At.Shift.AFTER), argsOnly = true)
    private static Potion beaconcontext$modifyPotion(Potion potion) {
        List<StatusEffectInstance> effects = potion.getEffects();
        List<StatusEffectInstance> newEffects = new ArrayList<>();

        for (StatusEffectInstance effectInstance : effects) {
            StatusEffect effect = effectInstance.getEffectType();
            if (!(effect instanceof InstantStatusEffect) && !effect.getAttributeModifiers().isEmpty()) {
                int amplifier = effectInstance.getAmplifier();
                //if (amplifier != 0) {
                    amplifier = (amplifier + 1) * 100;
                    newEffects.add(new StatusEffectInstance(effectInstance.getEffectType(), effectInstance.getDuration(), amplifier));
                //} else {
                //    newEffects.add(effectInstance);
                //}
            } else {
                newEffects.add(effectInstance);
            }
        }
        return new Potion(savedName, newEffects.toArray(new StatusEffectInstance[0]));
    }
}
