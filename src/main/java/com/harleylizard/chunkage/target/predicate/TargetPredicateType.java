package com.harleylizard.chunkage.target.predicate;

import com.harleylizard.chunkage.ChunkAge;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class TargetPredicateType<T extends TargetPredicate> {
    public static final ResourceKey<Registry<TargetPredicateType<? extends TargetPredicate>>> RESOURCE_KEY = ResourceKey.createRegistryKey(ChunkAge.resourceLocation("target_predicate"));

    private final MapCodec<T> codec;

    private TargetPredicateType(MapCodec<T> codec) {
        this.codec = codec;
    }

    public MapCodec<T> getCodec() {
        return codec;
    }
}
