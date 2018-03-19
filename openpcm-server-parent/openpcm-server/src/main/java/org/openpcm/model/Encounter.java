package org.openpcm.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.openpcm.utils.ObjectUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "encounter")
public class Encounter {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "encounter_id")
	private Long id;

	@Column(unique = true)
	private String title;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "encounter_encounter_type", joinColumns = @JoinColumn(name = "encounter_id"), inverseJoinColumns = @JoinColumn(name = "encounter_type_id"))
	private Set<EncounterType> types;

	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;

	private String utcOffset;

	@OneToMany
	@JoinTable(name = "encounter_observationsets", joinColumns = @JoinColumn(name = "encounter_id"), inverseJoinColumns = @JoinColumn(name = "observation_set_id"))
	private Set<ObservationSet> observationSets;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User patient;

	@ElementCollection
	@MapKeyColumn(name = "name")
	@Column(name = "value")
	@CollectionTable(name = "encounter_attributes", joinColumns = @JoinColumn(name = "encounter_id"))
	private Map<String, String> attributes = new HashMap<String, String>();

	@Override
	public String toString() {
		return ObjectUtil.print(this);
	}

}
