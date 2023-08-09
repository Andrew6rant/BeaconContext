package io.github.andrew6rant.beaconcontext.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {
    @Unique private static int diamondBlocks;

    @Inject(method = "updateLevel(Lnet/minecraft/world/World;III)I",
            at = @At(value = "HEAD"))
    private static void beaconcontext$countblocks(World world, int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        diamondBlocks = 0;
        // loop through beacon blocks in the same way that Vanilla does. I'll get the other more performant mixin working later
        for (int j = 1; j <= 4; j++) {
            int k = y - j;
            if (k < world.getBottomY()) break;
            for (int l = x - j; l <= x + j; ++l) {
                for (int m = z - j; m <= z + j; ++m) {
                    if (world.getBlockState(new BlockPos(l, k, m)).equals(Blocks.DIAMOND_BLOCK.getDefaultState())) {
                        //System.out.println("Diamond Block Found: "+l+", "+k+", "+m);
                        diamondBlocks++;
                    }
                }
            }
        }
    }

    /* This doesn't work :(
    @Inject(method = "updateLevel(Lnet/minecraft/world/World;III)I",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;",
            shift = At.Shift.BEFORE),
        locals = LocalCapture.CAPTURE_FAILHARD)
    private static void beaconcontext$countblocks(World world, int x, int y, int z, CallbackInfoReturnable<Integer> cir, int l, int k, int m) {
        System.out.println("l: " + l + ", k: " + k + ", m: ");
        if (world.getBlockState(new BlockPos(l, k, m)).equals(Blocks.DIAMOND_BLOCK.getDefaultState())) {
            System.out.println("Diamond Block Found!");
            diamondBlocks++;
        }
    }*/

    @ModifyConstant(method = "applyPlayerEffects(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;ILnet/minecraft/entity/effect/StatusEffect;Lnet/minecraft/entity/effect/StatusEffect;)V",
            constant = {
                @Constant(intValue = 0, ordinal = 0),
                @Constant(intValue = 1, ordinal = 0)
            })
    private static int beaconcontext$modifyEffects(int i) {
        System.out.println("diamondBlocks: "+diamondBlocks);
        return i + (diamondBlocks * 75);
    }
}
