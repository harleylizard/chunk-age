package com.harleylizard.chunkage;

import com.harleylizard.chunkage.target.TargetReloadListener;
import com.harleylizard.chunkage.target.TargetType;
import com.harleylizard.chunkage.task.TaskType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import org.ladysnake.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.chunk.ChunkComponentInitializer;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ChunkAge implements ModInitializer, ChunkComponentInitializer {
    private static final String MOD_ID = "chunk_age";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final ComponentKey<ChunkAgeComponent> CHUNK_AGE = ComponentRegistry.getOrCreate(resourceLocation("chunk_age"), ChunkAgeComponent.class);

    @Override
    public void onInitialize() {
        TargetType.register();
        TaskType.register();

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(TargetReloadListener.SUPPLIER.get());

        ServerChunkEvents.CHUNK_UNLOAD.register((level, chunk) -> {
            chunk.getComponent(CHUNK_AGE).setLastPlayTime(level.dayTime());
        });

        ServerChunkEvents.CHUNK_LOAD.register((level, chunk) -> {
            chunk.getComponent(CHUNK_AGE).queueTask();
        });
    }

    @Override
    public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
        registry.register(CHUNK_AGE, ChunkAgeComponent::new);
    }

    public static ResourceLocation resourceLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }


}
