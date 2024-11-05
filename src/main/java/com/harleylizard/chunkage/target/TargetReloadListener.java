package com.harleylizard.chunkage.target;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.harleylizard.chunkage.ChunkAge;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

public final class TargetReloadListener implements SimpleSynchronousResourceReloadListener {
    public static final Supplier<TargetReloadListener> SUPPLIER = Suppliers.memoize(TargetReloadListener::new);

    private final BiMap<ResourceLocation, Target> biMap = HashBiMap.create();

    private TargetReloadListener() {}

    @Override
    public ResourceLocation getFabricId() {
        return ChunkAge.resourceLocation("target");
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        biMap.clear();
        var resources = resourceManager.listResources("target", location -> location.getPath().endsWith(".json")).entrySet();
        for (var entry : resources) {
            try {
                var result = parseWithCodec(Target.CODEC, entry.getValue());
                if (result.isError()) {
                    ChunkAge.LOGGER.error("Failed to read Target {}", result.error().get().message());
                    return;
                }
                biMap.put(toResourceLocation(entry), result.getOrThrow());
            } catch (IOException e) {
                ChunkAge.LOGGER.error("Failed to read data.", e);
            }
        }
    }

    public Map<Target, ResourceLocation> getInverse() {
        return Collections.unmodifiableMap(biMap.inverse());
    }

    public Map<ResourceLocation, Target> getMap() {
        return Collections.unmodifiableMap(biMap);
    }

    public boolean has(ResourceLocation location) {
        return biMap.containsKey(location);
    }

    private static <T> DataResult<T> parseWithCodec(Codec<T> codec, Resource resource) throws IOException {
        try (var reader = resource.openAsReader()) {
            return codec.parse(JsonOps.INSTANCE, new GsonBuilder().create().fromJson(reader, JsonElement.class));
        }
    }

    private static ResourceLocation toResourceLocation(Map.Entry<ResourceLocation, Resource> entry) {
        var key = entry.getKey();
        var path = key.getPath();
        path = path.substring(0, path.indexOf(".json")).substring(path.lastIndexOf("/") + 1);
        return ResourceLocation.fromNamespaceAndPath(key.getNamespace(), path);
    }
}
