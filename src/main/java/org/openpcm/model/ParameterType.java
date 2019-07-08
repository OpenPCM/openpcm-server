package org.openpcm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.openpcm.utils.ObjectUtil;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parameter_type")
public class ParameterType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "parameter_type_id")
    private Long id;

    @NotNull
    private String name;

    private String description;

    private String uom;

    @Override
    public String toString() {
        return ObjectUtil.print(this);
    }
}
