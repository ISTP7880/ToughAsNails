/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;
import toughasnails.api.TANAPI;
import toughasnails.api.crafting.TANRecipeBookCategories;
import toughasnails.api.crafting.TANRecipeSerializers;
import toughasnails.api.crafting.TANRecipeTypes;
import toughasnails.crafting.WaterPurifierRecipe;

import java.util.function.BiConsumer;

public class ModCrafting
{
    public static void registerRecipeSerializers(BiConsumer<ResourceLocation, RecipeSerializer<?>> func)
    {
        TANRecipeSerializers.WATER_PURIFYING = (RecipeSerializer<? extends Recipe<SingleRecipeInput>>) registerSerializer(func, "water_purifying", new WaterPurifierRecipe.Serializer());
    }

    public static void registerRecipeTypes(BiConsumer<ResourceLocation, RecipeType<?>> func)
    {
        TANRecipeTypes.WATER_PURIFYING = (RecipeType<? extends Recipe<SingleRecipeInput>>) registerRecipe(func, "water_purifying", new RecipeType<WaterPurifierRecipe>()
        {
            @Override
            public String toString()
            {
                return "water_purifying";
            }
        });
    }

    public static void registerRecipeBookCategories(BiConsumer<ResourceLocation, RecipeBookCategory> func)
    {
        TANRecipeBookCategories.WATER_PURIFYING = registerRecipeBookCategory(func, "water_purifying", new RecipeBookCategory());
    }

    private static RecipeSerializer<?> registerSerializer(BiConsumer<ResourceLocation, RecipeSerializer<?>> func, String name, RecipeSerializer<?> serializer)
    {
        func.accept(ResourceLocation.fromNamespaceAndPath(TANAPI.MOD_ID, name), serializer);
        return serializer;
    }

    private static <T> RecipeType<?> registerRecipe(BiConsumer<ResourceLocation, RecipeType<?>> func, String name, RecipeType<?> type)
    {
        func.accept(ResourceLocation.fromNamespaceAndPath(TANAPI.MOD_ID, name), type);
        return type;
    }

    private static RecipeBookCategory registerRecipeBookCategory(BiConsumer<ResourceLocation, RecipeBookCategory> func, String name, RecipeBookCategory category)
    {
        func.accept(ResourceLocation.fromNamespaceAndPath(TANAPI.MOD_ID, name), category);
        return category;
    }
}
