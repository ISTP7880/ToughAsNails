/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.forge.datagen.model;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.equipment.EquipmentModel;
import toughasnails.init.ModEquipmentModels;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TANEquipmentModelProvider implements DataProvider
{
    private final PackOutput.PathProvider pathProvider;

    public TANEquipmentModelProvider(PackOutput output)
    {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/equipment");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output)
    {
        Map<ResourceLocation, EquipmentModel> map = new HashMap<>();
        ModEquipmentModels.bootstrap((location, model) -> {
            if (map.putIfAbsent(location, model) != null) {
                throw new IllegalStateException("Tried to register equipment model twice for id: " + location);
            }
        });
        return DataProvider.saveAll(output, EquipmentModel.CODEC, this.pathProvider, map);
    }

    @Override
    public String getName() {
        return "Equipment Model Definitions";
    }
}