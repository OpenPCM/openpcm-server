package org.openpcm.test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        final Map<String, String> parameterAttributes = new HashMap<>();
        parameterAttributes.put("PATIENT_STATE", "STANDING");
        final List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name("HR").description("Heart Rate").uom("bpm").timestamp(new Date()).utcOffset("-500").value("100")
                        .attributes(parameterAttributes).build());
        final Map<String, String> setAttributes = new HashMap<>();
        setAttributes.put("DOCUMENTATION_TYPE", "MANUAL");
        return ObservationSet.builder().origin("Device1").originType("CLINICIAN").timestamp(new Date()).utcOffset("-0500").parameters(parameters)
                        .attributes(setAttributes).build();
    }

    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean
    @Primary
    Collector collector() {
        final List<Attribute> attributes = new ArrayList<Attribute>();
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
        final List<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(Attribute.builder().key("late").value("yes").build());
        return Encounter.builder().title("First Checkup").description("Checkup on patient")
                        .timestamp(new Date(Instant.parse("2018-12-12T12:00:00Z").getEpochSecond())).attributes(attributes).build();
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
        final List<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(Attribute.builder().key("rich").value("yes").build());
        return User.builder().username("demo").password("demo").email("demo@demo.com").mrn("123456789").ssn("123-45-6789").attributes(attributes).build();
    }

}
