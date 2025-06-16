package org.agent.hexvoid.forge.mixin;

import org.agent.hexvoid.Hexvoid;
import org.spongepowered.asm.mixin.Mixin;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

// scuffed workaround for https://github.com/architectury/architectury-loom/issues/189
@Mixin(net.minecraft.data.Main.class)
public class MixinDatagenMain {
    @WrapMethod(method = "main", remap = false)
    private static void hexvoid$systemExitAfterDatagenFinishes(String[] strings, Operation<Void> original) {
        try {
            original.call((Object) strings);
        } catch (Throwable throwable) {
            Hexvoid.LOGGER.error("Datagen failed!", throwable);
            System.exit(1);
        }
        Hexvoid.LOGGER.info("Datagen succeeded, terminating.");
        System.exit(0);
    }
}
