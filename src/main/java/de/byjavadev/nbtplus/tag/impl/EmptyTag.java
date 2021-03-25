package de.byjavadev.nbtplus.tag.impl;

import de.byjavadev.nbtplus.tag.NBTTag;

public class EmptyTag extends NBTTag<Void>
{
    public EmptyTag(String key)
    {
        super(key, null);
    }
}
