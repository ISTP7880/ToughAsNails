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

    public static void bootstrap(BiConsumer<ResourceLocation, EquipmentModel> p_371586_)
    {
        p_371586_.accept(LEAF, onlyHumanoid("leaf"));
        p_371586_.accept(WOOL, onlyHumanoid("wool"));
    }

    private static EquipmentModel onlyHumanoid(String name)
    {
        return EquipmentModel.builder().addHumanoidLayers(ResourceLocation.fromNamespaceAndPath(ToughAsNails.MOD_ID, name)).build();
    }
}
