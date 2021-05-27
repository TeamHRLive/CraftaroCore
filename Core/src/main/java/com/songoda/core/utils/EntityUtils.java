package com.songoda.core.utils;

import com.songoda.core.compatibility.CompatibleMaterial;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EntityUtils {

    private static Class<?> clazzEntityInsentient, clazzEntity, clazzCraftEntity;

    private static Field aware, fromMobSpawner;

    private static Method methodGetHandle;

    static {
        try {
            String ver = Bukkit.getServer().getClass().getPackage().getName().substring(23);
            clazzEntityInsentient = Class.forName("net.minecraft.server." + ver + ".EntityInsentient");
            clazzEntity = Class.forName("net.minecraft.server." + ver + ".Entity");
            clazzCraftEntity = Class.forName("org.bukkit.craftbukkit." + ver + ".entity.CraftEntity");
            methodGetHandle = clazzCraftEntity.getDeclaredMethod("getHandle");
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            aware = clazzEntityInsentient.getField("aware");
        } catch (NoSuchFieldException e) {
            try {
                fromMobSpawner = clazzEntity.getField("fromMobSpawner");
            } catch (NoSuchFieldException ee) {
                ee.printStackTrace();
            }
        }
    }

    public static void setUnaware(LivingEntity entity) {
        try {
            setUnaware(methodGetHandle.invoke(clazzCraftEntity.cast(entity)));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void setUnaware(Object entity) {
        try {
            if (aware != null)
                aware.setBoolean(entity, false);
            else
                fromMobSpawner.setBoolean(entity, true);
        } catch (IllegalAccessException ee) {
            ee.printStackTrace();
        }
    }

    public static boolean isAware(LivingEntity entity) {
        try {
            return isAware(methodGetHandle.invoke(clazzCraftEntity.cast(entity)));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isAware(Object entity) {
        try {
            if (aware != null)
                return aware.getBoolean(entity);
            else
                return fromMobSpawner.getBoolean(entity);
        } catch (IllegalAccessException ee) {
            ee.printStackTrace();
        }
        return false;
    }

    public static List<CompatibleMaterial> getSpawnBlocks(EntityType type) {
        switch (type.name()) {
            case "PIG":
            case "SHEEP":
            case "CHICKEN":
            case "COW":
            case "RABBIT":
            case "LLAMA":
            case "HORSE":
            case "CAT":
                return new ArrayList<>(Collections.singletonList(CompatibleMaterial.GRASS_BLOCK));
            case "MUSHROOM_COW":
                return new ArrayList<>(Collections.singletonList(CompatibleMaterial.MYCELIUM));
            case "SQUID":
            case "ELDER_GUARDIAN":
            case "COD":
            case "SALMON":
            case "PUFFERFISH":
            case "TROPICAL_FISH":
                return new ArrayList<>(Collections.singletonList(CompatibleMaterial.WATER));
            case "OCELOT":
                return new ArrayList<>(Arrays.asList(CompatibleMaterial.GRASS_BLOCK,
                        CompatibleMaterial.JUNGLE_LEAVES, CompatibleMaterial.ACACIA_LEAVES,
                        CompatibleMaterial.BIRCH_LEAVES, CompatibleMaterial.DARK_OAK_LEAVES,
                        CompatibleMaterial.OAK_LEAVES, CompatibleMaterial.SPRUCE_LEAVES));
            default:
                return new ArrayList<>(Collections.singletonList(CompatibleMaterial.AIR));
        }
    }
}
