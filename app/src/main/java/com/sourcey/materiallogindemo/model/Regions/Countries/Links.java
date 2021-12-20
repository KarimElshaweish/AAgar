package com.sourcey.materiallogindemo.model.Regions.Countries;

import com.google.gson.annotations.SerializedName;

public class Links {
    @SerializedName("rel")
    private String rel;
    @SerializedName("href")
    private String href;

    public String getRel ()
    {
        return rel;
    }

    public void setRel (String rel)
    {
        this.rel = rel;
    }

    public String getHref ()
    {
        return href;
    }

    public void setHref (String href)
    {
        this.href = href;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [rel = "+rel+", href = "+href+"]";
    }
}
