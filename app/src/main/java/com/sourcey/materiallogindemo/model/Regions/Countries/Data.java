package com.sourcey.materiallogindemo.model.Regions.Countries;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("wikiDataId")
    private String wikiDataId;
    @SerializedName("isoCode")
    private String isoCode;
    @SerializedName("countryCode")
    private String countryCode;
    @SerializedName("name")
    private String name;
    @SerializedName("fipsCode")
    private String fipsCode;

    public String getWikiDataId ()
    {
        return wikiDataId;
    }

    public void setWikiDataId (String wikiDataId)
    {
        this.wikiDataId = wikiDataId;
    }

    public String getIsoCode ()
    {
        return isoCode;
    }

    public void setIsoCode (String isoCode)
    {
        this.isoCode = isoCode;
    }

    public String getCountryCode ()
    {
        return countryCode;
    }

    public void setCountryCode (String countryCode)
    {
        this.countryCode = countryCode;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getFipsCode ()
    {
        return fipsCode;
    }

    public void setFipsCode (String fipsCode)
    {
        this.fipsCode = fipsCode;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [wikiDataId = "+wikiDataId+", isoCode = "+isoCode+", countryCode = "+countryCode+", name = "+name+", fipsCode = "+fipsCode+"]";
    }

}
