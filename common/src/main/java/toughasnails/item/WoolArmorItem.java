/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

public class WoolArmorItem extends ArmorItem
{
    public WoolArmorItem(ArmorMaterial $$0, ArmorType $$1, Properties $$2)
    {
        super($$0, $$1, $$2);
    }

    @Override
    public void verifyComponentsAfterLoad(ItemStack stack)
    {
        if (!stack.has(DataComponents.DYED_COLOR))
        {
            stack.set(DataComponents.DYED_COLOR, new DyedItemColor(0xFFFFFF, false));
        }
    }
}
