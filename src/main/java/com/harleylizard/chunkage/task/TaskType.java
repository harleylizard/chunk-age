package com.harleylizard.chunkage.task;

import com.harleylizard.chunkage.ChunkAge;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class TaskType<T extends Task> {
    private static final ResourceKey<Registry<TaskType<? extends Task>>> RESOURCE_KEY = ResourceKey.createRegistryKey(ChunkAge.resourceLocation("task"));

    public static final Registry<TaskType<? extends Task>> REGISTRY = FabricRegistryBuilder.createSimple(RESOURCE_KEY).buildAndRegister();

    public static final TaskType<SetBlockTask> SET_BLOCK = of(SetBlockTask.CODEC);
    public static final TaskType<SetSmallFlowerTask> SET_SMALL_FLOWER = of(SetSmallFlowerTask.CODEC);

    private final MapCodec<T> codec;

    private TaskType(MapCodec<T> codec) {
        this.codec = codec;
    }

    public MapCodec<T> getCodec() {
        return codec;
    }

    public static void register() {
        register("set_block", SET_BLOCK);
        register("set_small_flower", SET_SMALL_FLOWER);
    }

    private static void register(String name, TaskType<? extends Task> type) {
        Registry.register(REGISTRY, ChunkAge.resourceLocation(name), type);
    }

    public static <T extends Task> TaskType<T> of(MapCodec<T> codec) {
        return new TaskType<>(codec);
    }
}
