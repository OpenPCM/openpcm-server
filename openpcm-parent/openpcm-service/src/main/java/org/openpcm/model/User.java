package org.openpcm.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.openpcm.utils.ObjectUtil;
import org.springframework.beans.BeanUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

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
    @Column(unique = true)
    private String ssn;

    private boolean active = true;

    public User(User user) {
        BeanUtils.copyProperties(user, this);
    }

    /** The address. */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private Address address = new Address();

    /** The alt ids. */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_altid", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "altid_id"))
    private List<AltId> altIds = new ArrayList<AltId>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<Role>();

    @Override
    public String toString() {
        return ObjectUtil.print(this);
    }
}
