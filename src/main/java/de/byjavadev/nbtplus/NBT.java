package de.byjavadev.nbtplus;

import de.byjavadev.nbtplus.tag.NBTTag;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode
@ToString
public class NBT implements Serializable
{
    /** The magic numbers for the start and the end of the NBT data section */
    public static final int START_MAGIC = /* dec 170 */ 0xAA, END_MAGIC = /* dec 188 */ 0xBC;

    /** A set containing all NBT Tags */
    private Set<NBTTag<?>> nbtTags = new HashSet<>();

    /**
     * @param key the key to search for
     * @param <T> the parameter type
     * @return an NBT Tag of T
     */

    @SuppressWarnings("unchecked")
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

    public NBT add(final NBTTag<?> nbtTag)
    {
        this.nbtTags.add(nbtTag);
        return this;
    }

    /**
     * Creates an NBT tag and adds it
     * @param key the key
     * @param value the value
     * @param <T> the type of value
     */

    public <T> NBT add(final String key, final T value)
    {
        return add(new NBTTag<>(key, value));
    }

    /**
     * Writes the NBT tags into a byte-array
     * @return a byte-array with the NBT Tags inside
     * @throws IOException if something went wrong while writing into the ObjectOutputStream
     */

    public byte[] save() throws IOException
    {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.useProtocolVersion(ObjectStreamConstants.PROTOCOL_VERSION_2);
        objectOutputStream.writeObject(this.nbtTags);

        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Saves the nbt to an outputstream, this nbt instance must the parent!
     * @param outputStream the output stream
     */

    public void saveAll(final OutputStream outputStream) throws IOException
    {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byteArrayOutputStream.write(START_MAGIC);
        byteArrayOutputStream.write(save());
        byteArrayOutputStream.write(END_MAGIC);

        byte[] data;
        System.out.println(Arrays.toString(data = byteArrayOutputStream.toByteArray()));
        System.out.println("Length: " + data.length);
        outputStream.write(data);
    }

    /**
     * Deserializes the NBT Tags from a byte array
     * @param bytes the input byte array
     * @return a new NBT object
     * @throws IOException if the stream could not be opened
     * @throws ClassNotFoundException if the data is of an invalid type
     */

    @SuppressWarnings("unchecked")
    public static NBT from(byte[] bytes) throws IOException, ClassNotFoundException
    {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        System.out.println(Arrays.toString(bytes));

        final NBT nbt = new NBT();
        nbt.nbtTags = (Set<NBTTag<?>>) objectInputStream.readObject();

        return nbt;
    }

    /**
     * Reads an NBT from the input stream
     * @param inputStream the input stream
     * @return the
     */

    public static NBT readAll(final InputStream inputStream) throws IOException, ClassNotFoundException
    {
        final ByteArrayOutputStream read = new ByteArrayOutputStream();

        boolean first = true;
        int current;
        while ((current = inputStream.read()) != -1)
        {
            //System.out.println("Handling " + current + " (byte " + ((byte) current) + ")..");

            if(current == START_MAGIC)
            {
                if(!first)
                {
                    throw new StreamCorruptedException("Start number inside the data");
                } else
                {
                    System.out.println("Found section where nbt starts!");
                    first = false;
                }
                continue;
            }

            if(current == END_MAGIC)
            {
                final byte[] data = read.toByteArray();
                System.out.println("Found end (" + data.length + "), creating nbt!");
                return from(data);
            }

            if(!first)
            {
                //System.out.println("Wrote data: " + current);
                read.write(current);
            }
        }

        System.out.println("Could not find start byte, no nbt has been read...");
        return null;
    }
}
