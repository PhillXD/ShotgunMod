package philxd.shotgunmod.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ReloadC2SPacket() implements CustomPacketPayload {

    public static final Type<ReloadC2SPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("shotgunmod", "reload"));

    // No data to encode/decode, so the codec just constructs the record
    public static final StreamCodec<FriendlyByteBuf, ReloadC2SPacket> STREAM_CODEC =
            StreamCodec.unit(new ReloadC2SPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
