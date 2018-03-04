package org.openpcm.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.openpcm.utils.ObjectUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "patient")
public class Patient {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    /** The first name. */
    private String firstName;

    /** The last name. */
    private String lastName;

    /** The middle name. */
    private String middleName;

    /** The maiden name. */
    private String maidenName;

    /** The gender. */
    private String gender;

    /** The date of birth. */
    private Date dateOfBirth;

    /** The email. */
    private String email;

    /** The phone number. */
    private String phoneNumber;
    
    /** the social security number */
    @Column(unique=true)
    private String ssn;

    /** The address. */
    @OneToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="patient")
    private Address address = new Address();

    /** The alt ids. */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "altid_id")
    private List<AltId> altIds = new ArrayList<AltId>();
    
    @Override
    public String toString() {
       return ObjectUtil.print(this);
    }

}
