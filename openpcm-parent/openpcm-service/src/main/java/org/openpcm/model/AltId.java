package org.openpcm.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
 * The Class AltId.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "altid")
public class AltId {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
    /** The name. */
    private String name;

    /** The value. */
    private String value;

    @Override
    public String toString() {
       return ObjectUtil.print(this);
    }
}
