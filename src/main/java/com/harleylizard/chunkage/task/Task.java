package com.harleylizard.chunkage.task;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public sealed interface Task permits SetBlockTask, SetSmallFlowerTask {
    Codec<Task> CODEC = TaskType.REGISTRY.byNameCodec().dispatch(Task::getTaskType, TaskType::getCodec);

    void apply(Level level, BlockPos blockPos, RandomSource source);

    TaskType<? extends Task> getTaskType();

    static void setBlockFast(Level level, BlockPos blockPos, BlockState blockState) {
        var x = blockPos.getX() & 0xF;
        var y = blockPos.getY() & 0xF;
        var z = blockPos.getZ() & 0xF;
        var j = SectionPos.blockToSectionCoord(blockPos.getX());
        var k = SectionPos.blockToSectionCoord(blockPos.getZ());
        var chunk = level.getChunkSource().getChunkNow(j, k);
        if (chunk != null && level.getBlockState(blockPos).is(BlockTags.AIR)) {
            level.setBlock(blockPos, blockState, Block.UPDATE_ALL);
            //chunk.getSection(chunk.getSectionIndex(blockPos.getY())).setBlockState(x, y, z, blockState, false);
        }
    }
}
