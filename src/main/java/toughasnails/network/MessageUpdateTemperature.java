/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import toughasnails.api.temperature.ITemperature;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;

import java.util.function.Supplier;

public class MessageUpdateTemperature
{
    public TemperatureLevel temperatureLevel;

    public MessageUpdateTemperature(TemperatureLevel temperatureLevel)
    {
        this.temperatureLevel = temperatureLevel;
    }

    public static void encode(MessageUpdateTemperature packet, FriendlyByteBuf buf)
    {
        buf.writeEnum(packet.temperatureLevel);
    }

    public static MessageUpdateTemperature decode(FriendlyByteBuf buf)
    {
        return new MessageUpdateTemperature(buf.readEnum(TemperatureLevel.class));
    }

    public static class Handler
    {
        public static void handle(final MessageUpdateTemperature packet, Supplier<NetworkEvent.Context> context)
        {
            context.get().enqueueWork(() ->
            {
                if (FMLEnvironment.dist != Dist.CLIENT)
                    return;

                updateTemperature(packet.temperatureLevel);
            });
            context.get().setPacketHandled(true);
        }

        @OnlyIn(Dist.CLIENT)
        private static void updateTemperature(TemperatureLevel temperature)
        {
            Player player = Minecraft.getInstance().player;
            ITemperature data = TemperatureHelper.getTemperatureData(player);
            data.setLevel(temperature);
        }
    }
}
