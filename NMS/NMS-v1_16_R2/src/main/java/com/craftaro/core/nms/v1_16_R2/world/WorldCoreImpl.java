package com.craftaro.core.nms.v1_16_R2.world;

import com.craftaro.core.nms.ReflectionUtils;
import com.craftaro.core.nms.v1_16_R2.world.spawner.BBaseSpawnerImpl;
import com.craftaro.core.nms.world.BBaseSpawner;
import com.craftaro.core.nms.world.SItemStack;
import com.craftaro.core.nms.world.SSpawner;
import com.craftaro.core.nms.world.SWorld;
import com.craftaro.core.nms.world.WorldCore;
import net.minecraft.server.v1_16_R2.BlockPosition;
import net.minecraft.server.v1_16_R2.Chunk;
import net.minecraft.server.v1_16_R2.ChunkCoordIntPair;
import net.minecraft.server.v1_16_R2.ChunkSection;
import net.minecraft.server.v1_16_R2.Fluid;
import net.minecraft.server.v1_16_R2.GameProfilerFiller;
import net.minecraft.server.v1_16_R2.IBlockData;
import net.minecraft.server.v1_16_R2.MobSpawnerAbstract;
import net.minecraft.server.v1_16_R2.WorldServer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_16_R2.CraftChunk;
import org.bukkit.craftbukkit.v1_16_R2.block.CraftBlock;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class WorldCoreImpl implements WorldCore {
    @Override
    public SSpawner getSpawner(CreatureSpawner spawner) {
        return new SSpawnerImpl(spawner.getLocation());
    }

    @Override
    public SSpawner getSpawner(Location location) {
        return new SSpawnerImpl(location);
    }

    @Override
    public SItemStack getItemStack(ItemStack item) {
        return new SItemStackImpl(item);
    }

    @Override
    public SWorld getWorld(World world) {
        return new SWorldImpl(world);
    }

    @Override
    public BBaseSpawner getBaseSpawner(CreatureSpawner spawner) throws NoSuchFieldException, IllegalAccessException {
        Object cTileEntity = ReflectionUtils.getFieldValue(spawner, "tileEntity");

        return new BBaseSpawnerImpl((MobSpawnerAbstract) ReflectionUtils.getFieldValue(cTileEntity, "a"));
    }

    /**
     * Method is based on {@link WorldServer#a(Chunk, int)}.
     */
    @Override
    public void randomTickChunk(org.bukkit.Chunk bukkitChunk, int tickAmount) {
        Chunk chunk = ((CraftChunk) bukkitChunk).getHandle();

        if (tickAmount <= 0) {
            return;
        }

        ChunkCoordIntPair chunkcoordintpair = chunk.getPos();
        int j = chunkcoordintpair.d();
        int k = chunkcoordintpair.e();

        GameProfilerFiller profiler = chunk.world.getMethodProfiler();

        profiler.enter("tickBlocks");
        for (ChunkSection chunksection : chunk.getSections()) {
            if (chunksection != Chunk.a && chunksection.d()) {
                int j1 = chunksection.getYPosition();

                for (int i = 0; i < tickAmount; ++i) {
                    BlockPosition blockposition2 = chunk.world.a(j, j1, k, 15);
                    profiler.enter("randomTick");

                    IBlockData iblockdata = chunksection.getType(
                            blockposition2.getX() - j,
                            blockposition2.getY() - j1,
                            blockposition2.getZ() - k);

                    if (iblockdata.isTicking()) {
                        iblockdata.b(chunk.world, blockposition2, chunk.world.random);
                    }

                    Fluid fluid = iblockdata.getFluid();
                    if (fluid.f()) {
                        fluid.b(chunk.world, blockposition2, chunk.world.random);
                    }

                    profiler.exit();
                }
            }
        }

        profiler.exit();
    }

    @Override
    public void updateAdjacentComparators(@NotNull Block bukkitBlock) {
        CraftBlock craftBlock = (CraftBlock) bukkitBlock;
        WorldServer serverLevel = craftBlock.getCraftWorld().getHandle();

        serverLevel.updateAdjacentComparators(craftBlock.getPosition(), craftBlock.getNMS().getBlock());
    }
}
