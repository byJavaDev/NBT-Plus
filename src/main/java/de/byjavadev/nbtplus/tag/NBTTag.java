package de.byjavadev.nbtplus.tag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@ToString
@AllArgsConstructor
@Data
public class NBTTag<T> implements Serializable
{
    private static final long serialVersionUID = 5098346L;

    private final String key;
    private T value;

    public Boolean getAsBoolean()
    {
        return (Boolean) this.getValue();
    }

     public Short getAsShort()
     {
         return (Short) this.getValue();
     }

     public Float getAsFloat()
     {
         return (Float) this.getValue();
     }

     public Long getAsLong()
     {
         return (Long) this.getValue();
     }

     public Double getAsDouble()
     {
         return (Double) this.getValue();
     }

     public Integer getAsInteger()
     {
         return (Integer) this.getValue();
     }

     public String getAsString()
     {
         return (String) this.getValue();
     }
}
