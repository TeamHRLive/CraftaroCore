package com.craftaro.core.nms.v1_13_R1.nbt;

import com.craftaro.core.nms.nbt.NBTCompound;
import com.craftaro.core.nms.nbt.NBTObject;
import net.minecraft.server.v1_13_R1.NBTTagCompound;

import java.util.Set;

public class NBTObjectImpl implements NBTObject {
    private final NBTTagCompound compound;
    private final String tag;

    public NBTObjectImpl(NBTTagCompound compound, String tag) {
        this.compound = compound;
        this.tag = tag;
    }

    @Override
    public String asString() {
        return compound.getString(tag);
    }

    @Override
    public boolean asBoolean() {
        return compound.getBoolean(tag);
    }

    @Override
    public int asInt() {
        return compound.getInt(tag);
    }

    @Override
    public double asDouble() {
        return compound.getDouble(tag);
    }

    @Override
    public long asLong() {
        return compound.getLong(tag);
    }

    @Override
    public short asShort() {
        return compound.getShort(tag);
    }

    @Override
    public byte asByte() {
        return compound.getByte(tag);
    }

    @Override
    public int[] asIntArray() {
        return compound.getIntArray(tag);
    }

    @Override
    public byte[] asByteArray() {
        return compound.getByteArray(tag);
    }

    @Override
    public Set<String> getKeys() {
        return compound.getKeys();
    }

    @Override
    public NBTCompound asCompound() {
        return new NBTCompoundImpl(compound.getCompound(tag));
    }
}
