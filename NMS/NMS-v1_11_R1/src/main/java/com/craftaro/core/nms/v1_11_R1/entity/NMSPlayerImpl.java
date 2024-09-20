package com.craftaro.core.nms.v1_11_R1.entity;

import com.craftaro.core.nms.entity.NMSPlayer;
import com.craftaro.core.nms.entity.player.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_11_R1.Packet;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class NMSPlayerImpl implements NMSPlayer {
    @Override
    public void sendPacket(Player p, Object packet) {
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket((Packet<?>) packet);
    }

    @Override
    public GameProfile getProfile(Player p) {
        com.mojang.authlib.GameProfile profile = ((CraftPlayer) p).getHandle().getProfile();
        return wrapProfile(profile);
    }

    @Override
    public GameProfile createProfile(UUID id, String name, @Nullable String textureValue) {
        com.mojang.authlib.GameProfile profile = new com.mojang.authlib.GameProfile(id, name);
        if (textureValue != null) {
            profile.getProperties().put("textures", new Property("textures", textureValue));
        }

        return wrapProfile(profile);
    }

    private GameProfile wrapProfile(com.mojang.authlib.GameProfile profile) {
        String textureValue = null;
        String textureSignature = null;
        for (Property property : profile.getProperties().get("textures")) {
            if (property.getName().equals("SKIN")) {
                textureValue = property.getValue();
                textureSignature = property.getSignature();
            }
        }

        return new GameProfile(
                profile,
                null,

                profile.getId(),
                profile.getName(),
                textureValue,
                textureSignature
        );
    }
}
