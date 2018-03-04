package org.openpcm.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.openpcm.utils.ObjectUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Class Address.
 */
@Getter 
@Setter 
@EqualsAndHashCode
@Builder
@Entity(name = "address")
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    /** The address line one. */
    private String addressLineOne;

    /** The address line two. */
    private String addressLineTwo;

    /** The city. */
    private String city;

    /** The state. */
    private String state;

    /** The zip code. */
    private String zipCode;

    @Override
    public String toString() {
       return ObjectUtil.print(this);
    }
}
