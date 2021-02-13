/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import toughasnails.api.block.TANBlocks;
import toughasnails.api.item.TANItems;
import toughasnails.block.RainCollectorBlock;
import toughasnails.config.ThirstConfig;

public class EmptyCanteenItem extends Item
{
    public EmptyCanteenItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        RayTraceResult rayTraceResult = getPlayerPOVHitResult(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);

        if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK)
        {
            BlockPos pos = ((BlockRayTraceResult)rayTraceResult).getBlockPos();

            if (world.mayInteract(player, pos))
            {
                BlockState state = world.getBlockState(pos);

                if (state.getBlock() instanceof RainCollectorBlock)
                {
                    // Fill the canteen from purified water from a rain collector
                    int waterLevel = state.getValue(RainCollectorBlock.LEVEL);

                    if (waterLevel > 0 && !world.isClientSide())
                    {
                        world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                        ((RainCollectorBlock) TANBlocks.RAIN_COLLECTOR).setWaterLevel(world, pos, state, waterLevel - 1);
                        return ActionResult.success(this.replaceCanteen(stack, player, new ItemStack(TANItems.PURIFIED_WATER_CANTEEN)));
                    }
                }
                else if (world.getFluidState(pos).is(FluidTags.WATER))
                {
                    // Fill the canteen with water in the world
                    world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);

                    RegistryKey<Biome> biome = player.level.getBiomeName(player.blockPosition()).orElse(Biomes.PLAINS);
                    Item canteenItem;

                    switch (ThirstConfig.getBiomeWaterType(biome))
                    {
                        case PURIFIED:
                            canteenItem = TANItems.PURIFIED_WATER_CANTEEN;
                            break;

                        case DIRTY:
                            canteenItem = TANItems.DIRTY_WATER_CANTEEN;
                            break;

                        case NORMAL:
                        default:
                            canteenItem = TANItems.WATER_CANTEEN;
                            break;
                    }

                    return ActionResult.sidedSuccess(this.replaceCanteen(stack, player, new ItemStack(canteenItem)), world.isClientSide());
                }
            }
        }

        return ActionResult.pass(stack);
    }

    protected ItemStack replaceCanteen(ItemStack oldStack, PlayerEntity player, ItemStack newStack)
    {
        player.awardStat(Stats.ITEM_USED.get(this));
        return DrinkHelper.createFilledResult(oldStack, player, newStack);
    }
}
