package org.openpcm.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.openpcm.utils.ObjectUtil;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NamedEntityGraph(name = "ObservationSet.attributes", attributeNodes = @NamedAttributeNode("attributes"))
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "observation_set")
public class ObservationSet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "observation_set_id")
    private Long id;

    @NotNull
    private String origin;

    @NotNull
    private String originType;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    private String utcOffset;

    /** The alt ids. */
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "observation_set_parameter", joinColumns = @JoinColumn(name = "observation_set_id"), inverseJoinColumns = @JoinColumn(name = "parameter_id"))
    private Set<Parameter> parameters;

    @ElementCollection
    @CollectionTable(name = "observation_set_attributes", joinColumns = @JoinColumn(name = "observation_set_id"))
    private Set<Attribute> attributes = new HashSet<>();

    @Override
    public String toString() {
        return ObjectUtil.print(this);
    }
}
