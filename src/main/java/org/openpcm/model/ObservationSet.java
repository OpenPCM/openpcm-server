package org.openpcm.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
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
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.openpcm.utils.ObjectUtil;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "observation_set")
@JsonIgnoreProperties(ignoreUnknown = true)
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
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "observation_set_parameter", joinColumns = @JoinColumn(name = "observation_set_id"), inverseJoinColumns = @JoinColumn(name = "parameter_id"))
	private List<Parameter> parameters;

	@ElementCollection
	@MapKeyColumn(name = "name")
	@Column(name = "value")
	@CollectionTable(name = "observation_set_attributes", joinColumns = @JoinColumn(name = "observation_set_id"))
	private Map<String, String> attributes;

	@Override
	public String toString() {
		return ObjectUtil.print(this);
	}
}
