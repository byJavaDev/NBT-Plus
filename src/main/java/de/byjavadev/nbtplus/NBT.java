package de.byjavadev.nbtplus;

import de.byjavadev.nbtplus.tag.NBTTag;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class NBT implements Serializable
{
    /** A set containing all NBT Tags */
    private Set<NBTTag<?>> nbtTags = new HashSet<>();

    /**
     * @param key the key to search for
     * @param <T> the parameter type
     * @return an NBT Tag of T
     */

    @SuppressWarnings({"unchecked", "unused"})
    public <T> NBTTag<T> get(final String key)
    {
        return (NBTTag<T>) this.nbtTags.stream().filter(nbtTag -> nbtTag.getKey().equals(key)).findFirst().orElse(null);
    }

    /**
     * @param key the search key
     * @return {@code true} if the NBT contains the specific search key
     */

    public boolean has(final String key)
    {
        return this.get(key) != null;
    }

    /**
     * Adds a NBT tag
     * @param nbtTag the NBT tag to add
     */

    @SuppressWarnings("unused")
    public void add(final NBTTag<?> nbtTag)
    {
        this.nbtTags.add(nbtTag);
    }

    /**
     * Writes the NBT tags into a byte-array
     * @return a byte-array with the NBT Tags inside
     * @throws IOException if something went wrong whilst writing into the ObjectOutputStream
     */

    @SuppressWarnings("unused")
    public byte[] save() throws IOException
    {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.useProtocolVersion(ObjectStreamConstants.PROTOCOL_VERSION_2);
        objectOutputStream.writeObject(this.nbtTags);

        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Deserializes the NBT Tags from a byte array
     * @param bytes the input byte array
     * @return a new NBT object
     * @throws IOException if the stream could not be opened
     * @throws ClassNotFoundException if the data is of an invalid type
     */

    @SuppressWarnings({"unchecked", "unused"})
    public static NBT from(byte[] bytes) throws IOException, ClassNotFoundException
    {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        final NBT nbt = new NBT();
        nbt.nbtTags = (Set<NBTTag<?>>) objectInputStream.readObject();

        return nbt;
    }
}
