package com.harleylizard.chunkage;

import com.harleylizard.chunkage.target.Target;
import com.harleylizard.chunkage.target.TargetReloadListener;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkAccess;

public final class PersistentTask {
    private final Target target;

    private PersistentTask(Target target) {
        this.target = target;
    }

    public void apply(ServerLevel level, ChunkAgeComponent component, ChunkAccess access) {
        var gameTime = level.dayTime();
        var seconds = component.getSeconds(gameTime);
        if (seconds == 0) return;

        var chunkPos = access.getPos();
        var random = level.getRandom();
        var sections = access.getSections();
        for (var i = 0; i < sections.length; i++) {
            var y = access.getSectionYFromSectionIndex(i) << 4;

            for (var x = 0; x < 16; x++) {
                for (var j = 0; j < 16; j++) {
                    for (var z = 0; z < 16; z++) {
                        var chance = 2500000 / seconds;

                        if (random.nextInt(chance + 1) == 0) {
                            var s = chunkPos.getBlockX(x);
                            var l = chunkPos.getBlockZ(z);
                            var blockPos = new BlockPos(s, j + y, l).above();

                            if (target.test(level, blockPos)) {
                                target.getTask().apply(level, blockPos, level.random);
                            }
                        }
                    }
                }
            }
        }
    }

    public CompoundTag toTag() {
        var tag = new CompoundTag();
        tag.putString("target", TargetReloadListener.SUPPLIER.get().getInverse().get(target).toString());
        return tag;
    }

    public static PersistentTask fromTag(CompoundTag tag) {
        return new PersistentTask(TargetReloadListener.SUPPLIER.get().getMap().get(ResourceLocation.parse(tag.getString("target"))));
    }

    public static PersistentTask fromTarget(ResourceLocation location) {
        return new PersistentTask(TargetReloadListener.SUPPLIER.get().getMap().get(location));
    }
}
