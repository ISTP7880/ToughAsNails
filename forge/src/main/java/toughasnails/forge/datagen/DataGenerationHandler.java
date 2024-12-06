/************************************************************************even*******
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.forge.datagen;

import net.minecraft.core.Cloner;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.RegistriesDatapackGenerator;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import toughasnails.api.TANAPI;
import toughasnails.core.ToughAsNails;
import toughasnails.forge.datagen.loot.TANLootTableProvider;
import toughasnails.forge.datagen.model.TANEquipmentModelProvider;
import toughasnails.forge.datagen.model.TANModelProvider;
import toughasnails.forge.datagen.provider.*;
import toughasnails.init.ModEnchantments;
import toughasnails.init.ModEquipmentModels;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ToughAsNails.MOD_ID)
public class DataGenerationHandler
{
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
        .add(Registries.DAMAGE_TYPE, ModDamageTypes::bootstrap)
        .add(Registries.ENCHANTMENT, ModEnchantments::bootstrap);

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        var datapackProvider = generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(output, lookupProvider, BUILDER, Set.of(ToughAsNails.MOD_ID)));

        // Recipes
        generator.addProvider(event.includeServer(), new TANRecipeProvider.Runner(output, event.getLookupProvider()));

        // Loot
        generator.addProvider(event.includeServer(), TANLootTableProvider.create(output, event.getLookupProvider()));

        // Tags
        var blocksTagProvider = generator.addProvider(event.includeServer(), new TANBlockTagsProvider(output, datapackProvider.getRegistryProvider(), existingFileHelper));
        generator.addProvider(event.includeServer(), new TANItemTagsProvider(output, datapackProvider.getRegistryProvider(), blocksTagProvider.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeServer(), new TANBiomeTagsProvider(output, datapackProvider.getRegistryProvider(), existingFileHelper));
        generator.addProvider(event.includeServer(), new TANDamageTypeTagsProvider(output, datapackProvider.getRegistryProvider(), existingFileHelper));
        generator.addProvider(event.includeServer(), new TANTrimMaterialTagsProvider(output, datapackProvider.getRegistryProvider(), existingFileHelper));
        generator.addProvider(event.includeServer(), new TANPoiTypesTagsProvider(output, datapackProvider.getRegistryProvider(), existingFileHelper));
        generator.addProvider(event.includeServer(), new TANEnchantmentTagsProvider(output, datapackProvider.getRegistryProvider(), existingFileHelper));

        // Client
        generator.addProvider(event.includeClient(), new TANItemModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new TANEquipmentModelProvider(output));
        generator.addProvider(event.includeClient(), new TANModelProvider(output));
    }
}
