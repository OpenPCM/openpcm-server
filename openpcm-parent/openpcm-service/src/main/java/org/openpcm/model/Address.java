package org.openpcm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.openpcm.utils.ObjectUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Column(name = "address_id")
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
    @JoinColumn(name = "address_user_id")
    private User user;

    @Override
    public String toString() {
        return ObjectUtil.print(this);
    }
}
