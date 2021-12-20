package com.sourcey.materiallogindemo.model.Regions.Countries;

import com.google.gson.annotations.SerializedName;

public class Metadata {
    @SerializedName("currentOffset")
    private String currentOffset;

    @SerializedName("totalCount")
    private String totalCount;

    public String getCurrentOffset ()
    {
        return currentOffset;
    }

    public void setCurrentOffset (String currentOffset)
    {
        this.currentOffset = currentOffset;
    }

    public String getTotalCount ()
    {
        return totalCount;
    }

    public void setTotalCount (String totalCount)
    {
        this.totalCount = totalCount;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [currentOffset = "+currentOffset+", totalCount = "+totalCount+"]";
    }
}
