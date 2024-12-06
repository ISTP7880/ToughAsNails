/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.equipment.EquipmentModel;
import toughasnails.core.ToughAsNails;

import java.util.function.BiConsumer;

public class ModEquipmentModels
{
    public static final ResourceLocation LEAF = ResourceLocation.fromNamespaceAndPath(ToughAsNails.MOD_ID, "leaf");
    public static final ResourceLocation WOOL = ResourceLocation.fromNamespaceAndPath(ToughAsNails.MOD_ID, "wool");

    public static void bootstrap(BiConsumer<ResourceLocation, EquipmentModel> output)
    {
        output.accept(
                LEAF,
                EquipmentModel.builder()
                        .addHumanoidLayers(ResourceLocation.fromNamespaceAndPath(ToughAsNails.MOD_ID, "leaf"), true)
                        .addHumanoidLayers(ResourceLocation.fromNamespaceAndPath(ToughAsNails.MOD_ID, "leaf_overlay"), true)
                        .build()
        );
        output.accept(
                WOOL,
                EquipmentModel.builder()
                        .addHumanoidLayers(ResourceLocation.fromNamespaceAndPath(ToughAsNails.MOD_ID, "wool"), true)
                        .addHumanoidLayers(ResourceLocation.fromNamespaceAndPath(ToughAsNails.MOD_ID, "wool_overlay"), false)
                        .build()
        );
    }
}
