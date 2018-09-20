package org.openpcm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
@Table(name = "encounter_type")
public class EncounterType {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "encounter_type_id")
	private Long id;

	@NotNull
	private String name;

	@Override
	public String toString() {
		return ObjectUtil.print(this);
	}
}
