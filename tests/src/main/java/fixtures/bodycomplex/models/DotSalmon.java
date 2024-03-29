package fixtures.bodycomplex.models;

import com.azure.core.annotation.Fluent;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The DotSalmon model.
 */
@Fluent
public final class DotSalmon extends DotFish {
    /*
     * The location property.
     */
    @JsonProperty(value = "location")
    private String location;

    /*
     * The iswild property.
     */
    @JsonProperty(value = "iswild")
    private boolean iswild;

    /**
     * Get the location property: The location property.
     * 
     * @return the location value.
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * Set the location property: The location property.
     * 
     * @param location the location value to set.
     * @return the DotSalmon object itself.
     */
    public DotSalmon setLocation(String location) {
        this.location = location;
        return this;
    }

    /**
     * Get the iswild property: The iswild property.
     * 
     * @return the iswild value.
     */
    public boolean iswild() {
        return this.iswild;
    }

    /**
     * Set the iswild property: The iswild property.
     * 
     * @param iswild the iswild value to set.
     * @return the DotSalmon object itself.
     */
    public DotSalmon setIswild(boolean iswild) {
        this.iswild = iswild;
        return this;
    }
}
