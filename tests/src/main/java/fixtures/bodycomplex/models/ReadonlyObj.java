package fixtures.bodycomplex.models;

import com.azure.core.annotation.Fluent;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The ReadonlyObj model.
 */
@Fluent
public final class ReadonlyObj {
    /*
     * The id property.
     */
    @JsonProperty(value = "id", access = JsonProperty.Access.WRITE_ONLY)
    private String id;

    /*
     * The size property.
     */
    @JsonProperty(value = "size")
    private int size;

    /**
     * Get the id property: The id property.
     * 
     * @return the id value.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Get the size property: The size property.
     * 
     * @return the size value.
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Set the size property: The size property.
     * 
     * @param size the size value to set.
     * @return the ReadonlyObj object itself.
     */
    public ReadonlyObj setSize(int size) {
        this.size = size;
        return this;
    }
}
