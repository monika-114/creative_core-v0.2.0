package dev.creationcore.mixin;

import dev.creationcore.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Makes Light Blocks targetable while Mine Craft is held, mirroring the vanilla Light item. */
@Mixin(LightBlock.class)
public abstract class LightBlockMixin {
    @Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
    private void creationcore$mineCraftCanTargetLight(BlockState state, BlockGetter level, BlockPos pos,
                                                       CollisionContext context,
                                                       CallbackInfoReturnable<VoxelShape> cir) {
        // getShape is also queried while vanilla Blocks are being statically bootstrapped.
        // At that point our DeferredItem has not been bound yet, so calling get() would crash
        // the game before mod registration starts. Only resolve the item after the holder is bound.
        if (ModItems.MINE_CRAFT.isBound() && context.isHoldingItem(ModItems.MINE_CRAFT.get())) {
            cir.setReturnValue(Shapes.block());
        }
    }
}
