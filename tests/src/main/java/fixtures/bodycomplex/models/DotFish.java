package fixtures.bodycomplex.models;

import com.azure.core.annotation.Fluent;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The DotFish model.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "null", defaultImpl = DotFish.class)
@JsonTypeName("DotFish")
@JsonSubTypes({
    @JsonSubTypes.Type(name = "DotSalmon", value = DotSalmon.class)
})
@Fluent
public class DotFish {
    /*
     * The fish.type property.
     */
    @JsonProperty(value = "fish.type", required = true)
    private String fishtype;

    /*
     * The species property.
     */
    @JsonProperty(value = "species")
    private String species;

    /**
     * Get the fishtype property: The fish.type property.
     * 
     * @return the fishtype value.
     */
    public String getFishtype() {
        return this.fishtype;
    }

    /**
     * Set the fishtype property: The fish.type property.
     * 
     * @param fishtype the fishtype value to set.
     * @return the DotFish object itself.
     */
    public DotFish setFishtype(String fishtype) {
        this.fishtype = fishtype;
        return this;
    }

    /**
     * Get the species property: The species property.
     * 
     * @return the species value.
     */
    public String getSpecies() {
        return this.species;
    }

    /**
     * Set the species property: The species property.
     * 
     * @param species the species value to set.
     * @return the DotFish object itself.
     */
    public DotFish setSpecies(String species) {
        this.species = species;
        return this;
    }
}
