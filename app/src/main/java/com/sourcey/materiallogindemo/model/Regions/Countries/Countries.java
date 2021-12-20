package com.sourcey.materiallogindemo.model.Regions.Countries;

import com.google.gson.annotations.SerializedName;

public class Countries {
    @SerializedName("metadata")
    private Metadata metadata;

    @SerializedName("data")
    private Data[] data;

    @SerializedName("links")
    private Links[] links;

    public Metadata getMetadata ()
    {
        return metadata;
    }

    public void setMetadata (Metadata metadata)
    {
        this.metadata = metadata;
    }

    public Data[] getData ()
    {
        return data;
    }

    public void setData (Data[] data)
    {
        this.data = data;
    }

    public Links[] getLinks ()
    {
        return links;
    }

    public void setLinks (Links[] links)
    {
        this.links = links;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [metadata = "+metadata+", data = "+data+", links = "+links+"]";
    }

}
