package org.openpcm.test;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.openpcm.model.Attribute;
import org.openpcm.model.Collector;
import org.openpcm.model.Encounter;
import org.openpcm.model.EncounterType;
import org.openpcm.model.ObservationSet;
import org.openpcm.model.Parameter;
import org.openpcm.model.ParameterType;
import org.openpcm.model.Role;
import org.openpcm.model.User;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

@Configuration
public class TestConfig {

    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean
    @Primary
    ObservationSet observationSet() {
        final Set<Attribute> parameterAttributes = new HashSet<>();
        parameterAttributes.add(Attribute.builder().key("PATIENT_STATE").value("STANDING").build());
        final Set<Parameter> parameters = new HashSet<>();
        parameters.add(Parameter.builder().name("HR").description("Heart Rate").uom("bpm").timestamp(new Date()).value("100").attributes(parameterAttributes)
                        .build());
        final Set<Attribute> observationSetAttributes = new HashSet<>();
        observationSetAttributes.add(Attribute.builder().key("DOCUMENTATION_TYPE").value("MANUAL").build());
        return ObservationSet.builder().origin("Device1").originType("CLINICIAN").timestamp(new Date()).parameters(parameters)
                        .attributes(observationSetAttributes).build();
    }

    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean
    @Primary
    Collector collector() {
        final Set<Attribute> attributes = new HashSet<>();
        attributes.add(Attribute.builder().key("alias").value("Critikon 9710").build());
        return Collector.builder().name("GE Dinamap").attributes(attributes).build();
    }

    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean
    @Primary
    EncounterType encounterType() {
        return EncounterType.builder().name("DOCTOR-VISIT").build();
    }

    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean
    @Primary
    Encounter encounter() {
        final Set<Attribute> attributes = new HashSet<>();
        attributes.add(Attribute.builder().key("late").value("yes").build());
        final Encounter.EncounterBuilder builder = Encounter.builder().title("First Checkup").description("Checkup on patient")
                        .timestamp(new Date(Instant.parse("2018-12-12T12:00:00Z").getEpochSecond())).attributes(attributes);
        builder.types(new HashSet<>(Arrays.asList(EncounterType.builder().id(14512L).build())));
        builder.patient(User.builder().id(14511L).build());
        return builder.build();
    }

    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean
    @Primary
    ParameterType parameterType() {
        return ParameterType.builder().name("HR").description("Heart Rate").uom("bpm").build();
    }

    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean
    @Primary
    Role role() {
        return Role.builder().name("TESTER").build();
    }

    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean
    @Primary
    User user() {
        return User.builder().username("test-demo").password("test-demo").email("test-demo@demo.com").mrn("12-3079304-567").ssn("111-22-3333").build();
    }

}
