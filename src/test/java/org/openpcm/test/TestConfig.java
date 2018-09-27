package org.openpcm.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openpcm.model.Attribute;
import org.openpcm.model.Collector;
import org.openpcm.model.ObservationSet;
import org.openpcm.model.Parameter;
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
        attributes.add(new Attribute("nickName", "Critikon 9710"));
        return Collector.builder().name("GE Dinamap").attributes(attributes).build();
    }
}
