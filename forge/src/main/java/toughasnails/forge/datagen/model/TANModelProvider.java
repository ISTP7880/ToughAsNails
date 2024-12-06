/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.forge.datagen.model;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class TANModelProvider implements DataProvider
{
    private final PackOutput.PathProvider modelPathProvider;

    public TANModelProvider(PackOutput output)
    {
        this.modelPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output)
    {
        Map<ResourceLocation, Supplier<JsonElement>> modelMap = Maps.newHashMap();
        BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput = (p_325908_, p_325909_) -> {
            Supplier<JsonElement> $$3x = (Supplier)modelMap.put(p_325908_, p_325909_);
            if ($$3x != null) {
                throw new IllegalStateException("Duplicate model definition for " + String.valueOf(p_325908_));
            }
        };
        (new TANItemModelGenerators(modelOutput)).run();
        return CompletableFuture.allOf(this.saveCollection(output, modelMap, this.modelPathProvider::json));
    }

    private <T> CompletableFuture<?> saveCollection(CachedOutput p_254549_, Map<T, ? extends Supplier<JsonElement>> p_253779_, Function<T, Path> p_254013_) {
        return CompletableFuture.allOf(p_253779_.entrySet().stream().map(p_253408_ -> {
            Path path = p_254013_.apply(p_253408_.getKey());
            JsonElement jsonelement = p_253408_.getValue().get();
            return DataProvider.saveStable(p_254549_, jsonelement, path);
        }).toArray(CompletableFuture[]::new));
    }

    public final String getName() {
        return "Model Definitions";
    }
}
