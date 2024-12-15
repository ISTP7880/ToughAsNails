/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.neoforge.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.tags.EnchantmentTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import toughasnails.api.enchantment.TANEnchantments;

import java.util.concurrent.CompletableFuture;

public class TANEnchantmentTagsProvider extends EnchantmentTagsProvider
{
    public TANEnchantmentTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        // TAN tags
        this.tag(EnchantmentTags.TREASURE).add(TANEnchantments.THERMAL_TUNING).add(TANEnchantments.WATER_CLEANSING);
        this.tag(EnchantmentTags.TRADEABLE).add(TANEnchantments.THERMAL_TUNING).add(TANEnchantments.WATER_CLEANSING);
        this.tag(EnchantmentTags.ON_RANDOM_LOOT).add(TANEnchantments.THERMAL_TUNING).add(TANEnchantments.WATER_CLEANSING);
    }
}
