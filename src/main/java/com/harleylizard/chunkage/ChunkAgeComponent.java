package com.harleylizard.chunkage;

import com.harleylizard.chunkage.target.TargetReloadListener;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import org.ladysnake.cca.api.v3.component.ComponentV3;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class ChunkAgeComponent implements ComponentV3, ServerTickingComponent {
    private static final String LAST_PLAY_TIME_NAME = "LastPlayTime";
    private static final String QUEUE_NAME = "Queue";

    private final Queue<PersistentTask> queue = new ConcurrentLinkedQueue<>();

    private final ChunkAccess access;

    private long lastPlayTime;
    private int ticks;

    public ChunkAgeComponent(ChunkAccess access) {
        this.access = access;
    }

    @Override
    public void serverTick() {
        ticks++;
        if (access instanceof LevelChunk levelChunk && ticks % 20 == 0) {
            if (!queue.isEmpty()) {
                queue.poll().apply((ServerLevel) levelChunk.getLevel(), this, access);
            }
        }
    }

    @Override
    public void readFromNbt(CompoundTag compoundTag, HolderLookup.Provider provider) {
        lastPlayTime = compoundTag.getLong(LAST_PLAY_TIME_NAME);

        queue.clear();
        var list = compoundTag.getList(QUEUE_NAME, Tag.TAG_COMPOUND);
        for (var tag : list) {
            queue.offer(PersistentTask.fromTag((CompoundTag) tag));
        }
    }

    @Override
    public void writeToNbt(CompoundTag compoundTag, HolderLookup.Provider provider) {
        compoundTag.putLong(LAST_PLAY_TIME_NAME, lastPlayTime);

        var list = new ListTag();
        for (var i = 0; i < queue.size(); i++) {
            list.add(queue.peek().toTag());
        }
        compoundTag.put(QUEUE_NAME, list);
    }

    public void queueTask() {
        TargetReloadListener.SUPPLIER.get().getMap().keySet().forEach(target -> queue.offer(PersistentTask.fromTarget(target)));
    }

    public void setLastPlayTime(long time) {
        lastPlayTime = time;
    }

    public int getSeconds(long playTime) {
        return (int) (playTime - lastPlayTime) / 20;
    }
}
