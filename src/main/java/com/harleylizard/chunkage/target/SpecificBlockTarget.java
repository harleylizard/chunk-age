package com.harleylizard.chunkage.target;

import com.harleylizard.chunkage.task.Task;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;

public final class SpecificBlockTarget implements Target {
    public static final MapCodec<SpecificBlockTarget> CODEC = RecordCodecBuilder.mapCodec(builder -> {
        return builder
                .group(BlockPredicate.CODEC.fieldOf("block_predicate").forGetter(target -> target.predicate), Task.CODEC.fieldOf("task").forGetter(SpecificBlockTarget::getTask))
                .apply(builder, SpecificBlockTarget::new);
    });

    private final BlockPredicate predicate;
    private final Task task;

    private SpecificBlockTarget(BlockPredicate predicate, Task task) {
        this.predicate = predicate;
        this.task = task;
    }

    @Override
    public boolean test(WorldGenLevel level, BlockPos blockPos) {
        return predicate.test(level, blockPos);
    }

    @Override
    public Task getTask() {
        return task;
    }

    @Override
    public TargetType<?> getTargetType() {
        return TargetType.SPECIFIC_BLOCK;
    }
}
