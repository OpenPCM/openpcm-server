package org.openpcm.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.openpcm.utils.ObjectUtil;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@EqualsAndHashCode
@Builder
@Entity(name = "patient")
public class Patient {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    /** The first name. */
	 @Column(nullable = false)
    private String firstName;

    /** The last name. */
	 @Column(nullable = false)
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

    /** The address. */
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    /** The alt ids. */
    @OneToMany
    @JoinColumn(name = "altid_id")
    private List<AltId> altIds = new ArrayList<AltId>();
    
    @Override
    public String toString() {
       return ObjectUtil.print(this);
    }

}
