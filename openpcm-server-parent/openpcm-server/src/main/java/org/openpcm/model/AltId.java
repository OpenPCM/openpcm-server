package org.openpcm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.openpcm.utils.ObjectUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Column(nullable = false, name = "altid_id")
    private String name;

    /** The value. */
    @Column(nullable = false)
    private String value;

    @Override
    public String toString() {
        return ObjectUtil.print(this);
    }
}
