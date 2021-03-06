package org.openpcm.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.openpcm.utils.ObjectUtil;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class Address.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {

    /** The address line one. */
    @NotNull
    @Size(max = 100)
    private String addressLineOne;

    /** The address line two. */
    @Size(max = 100)
    private String addressLineTwo;

    /** The city. */
    @NotNull
    @Size(max = 100)
    private String city;

    /** The state. */
    @NotNull
    @Size(max = 100)
    private String state;

    /** The zip code. */
    @Size(max = 10)
    private String zipCode;

    @Override
    public String toString() {
        return ObjectUtil.print(this);
    }
}
