package org.openpcm.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.openpcm.model.Patient.PatientBuilder;
import org.openpcm.utils.ObjectUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Class Address.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    
    @OneToOne
    @JoinColumn(name = "address_patient_id")
    private Patient patient;

    @Override
    public String toString() {
       return ObjectUtil.print(this);
    }
}
