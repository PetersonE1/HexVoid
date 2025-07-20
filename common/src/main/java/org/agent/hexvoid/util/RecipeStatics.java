package org.agent.hexvoid.util;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class RecipeStatics {
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static BlockState copyProperties(BlockState original, BlockState copyTo) {
        for (Property prop : original.getProperties()) {
            if (copyTo.hasProperty(prop)) {
                copyTo = copyTo.setValue(prop, original.getValue(prop));
            }
        }

        return copyTo;
    }
}
