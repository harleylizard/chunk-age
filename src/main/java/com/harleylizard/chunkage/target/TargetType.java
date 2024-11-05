package com.harleylizard.chunkage.target;

import com.harleylizard.chunkage.ChunkAge;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class TargetType<T extends Target> {
    private static final ResourceKey<Registry<TargetType<? extends Target>>> RESOURCE_KEY = ResourceKey.createRegistryKey(ChunkAge.resourceLocation("target_type"));

    public static final Registry<TargetType<? extends Target>> REGISTRY = FabricRegistryBuilder.createSimple(RESOURCE_KEY).buildAndRegister();

    public static final TargetType<SpecificBlockTarget> SPECIFIC_BLOCK = of(SpecificBlockTarget.CODEC);

    private final MapCodec<T> codec;

    private TargetType(MapCodec<T> codec) {
        this.codec = codec;
    }

    public MapCodec<T> getCodec() {
        return codec;
    }

    public static void register() {
        register("specific_block", SPECIFIC_BLOCK);
    }

    private static void register(String name, TargetType<? extends Target> type) {
        Registry.register(REGISTRY, ChunkAge.resourceLocation(name), type);
    }

    public static <T extends Target> TargetType<T> of(MapCodec<T> codec) {
        return new TargetType<>(codec);
    }
}
