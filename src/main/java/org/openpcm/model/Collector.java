package org.openpcm.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.openpcm.utils.ObjectUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NamedEntityGraph(name = "Collector.attributes", attributeNodes = @NamedAttributeNode("attributes"))
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "collector")
public class Collector {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "collector_id")
    private Long id;

    @NotNull
    private String name;

    @ElementCollection
    @CollectionTable(name = "collector_attributes", joinColumns = @JoinColumn(name = "collector_id"))
    private Set<Attribute> attributes = new HashSet<>();

    @Override
    public String toString() {
        return ObjectUtil.print(this);
    }
}
