package com.harleylizard.chunkage.target;

import com.harleylizard.chunkage.task.Task;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;

public sealed interface Target permits SpecificBlockTarget {
    Codec<Target> CODEC = TargetType.REGISTRY.byNameCodec().dispatch(Target::getTargetType, TargetType::getCodec);

    boolean test(WorldGenLevel level, BlockPos blockPos);

    Task getTask();

    TargetType<?> getTargetType();
}
