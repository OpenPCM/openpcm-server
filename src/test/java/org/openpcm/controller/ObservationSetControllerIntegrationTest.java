package org.openpcm.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openpcm.annotation.IntegrationTest;
import org.openpcm.dao.ObservationSetRepository;
import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.ObservationSet;
import org.openpcm.model.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Category(IntegrationTest.class)
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ObservationSetControllerIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObservationSetControllerIntegrationTest.class);

    @Autowired
    private ObservationSetController controller;

    @Autowired
    private ObservationSetRepository repository;

    @Test
    public void test_create_happy() throws DataViolationException {
        final Map<String, String> parameterAttributes = new HashMap<>();
        parameterAttributes.put("PATIENT_STATE", "STANDING");
        final List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name("HR").description("Heart Rate").uom("bpm").timestamp(new Date()).utcOffset("-500").value("100")
                        .attributes(parameterAttributes).build());
        final Map<String, String> setAttributes = new HashMap<>();
        setAttributes.put("DOCUMENTATION_TYPE", "MANUAL");
        final ObservationSet set = ObservationSet.builder().origin("Device1").originType("CLINICIAN").timestamp(new Date()).utcOffset("-0500")
                        .parameters(parameters).attributes(setAttributes).build();

        final ObservationSet result = controller.createObservationSet(set);

        assertNotNull(result.getId(), "instance should not be null");
        assertNotNull(result.getParameters().get(0).getId(), "instance should not be null");
    }

    @Test
    public void test_read_pagination_happy() {
        final Map<String, String> parameterAttributes = new HashMap<>();
        parameterAttributes.put("PATIENT_STATE", "SITTING");
        final List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name("SPO2").description("SpO2").uom("%").timestamp(new Date()).utcOffset("-500").value("100")
                        .attributes(parameterAttributes).build());
        final Map<String, String> setAttributes = new HashMap<>();
        setAttributes.put("DOCUMENTATION_TYPE", "MDI");
        final ObservationSet set = ObservationSet.builder().origin("Device2").originType("MED-DEVICE").timestamp(new Date()).utcOffset("-0500")
                        .parameters(parameters).attributes(setAttributes).build();

        repository.save(set);

        final Page<ObservationSet> page = controller.readObservationSets(PageRequest.of(0, 20));

        assertEquals(1, page.getNumberOfElements(), "incorrect number of elements");
        assertEquals(set.getId(), page.getContent().get(0).getId(), "incorrect property value");
        assertEquals(set.getParameters().get(0).getId(), page.getContent().get(0).getParameters().get(0).getId(), "incorrect property value");
    }

    @Test
    public void test_read_byId_happy() throws NotFoundException {
        final Map<String, String> parameterAttributes = new HashMap<>();
        parameterAttributes.put("PATIENT_STATE", "UNCONSCIOUS");
        final List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name("RESP_IMP").description("Respiration Impedance").uom("bpm").timestamp(new Date()).utcOffset("-500").value("12")
                        .attributes(parameterAttributes).build());
        final Map<String, String> setAttributes = new HashMap<>();
        setAttributes.put("DOCUMENTATION_TYPE", "MACH_CALC");
        final ObservationSet set = ObservationSet.builder().origin("Device3").originType("MED-DEVICE").timestamp(new Date()).utcOffset("-0500")
                        .parameters(parameters).attributes(setAttributes).build();

        repository.save(set);

        final ObservationSet readSet = controller.readObservationSet(set.getId());

        assertEquals(set.getId(), readSet.getId(), "incorrect property value");
        assertEquals(set.getParameters().get(0).getId(), readSet.getParameters().get(0).getId(), "incorrect property value");
    }

    @Test
    public void test_update_happy() throws NotFoundException {
        final Map<String, String> parameterAttributes = new HashMap<>();
        parameterAttributes.put("PATIENT_STATE", "LYING");
        final List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name("RESP_RATE").description("Respiration Rate").uom("bpm").timestamp(new Date()).utcOffset("-500").value("17")
                        .attributes(parameterAttributes).build());
        final Map<String, String> setAttributes = new HashMap<>();
        setAttributes.put("DERIVATION", "STOP_WATCH");
        final ObservationSet set = ObservationSet.builder().origin("Device4").originType("MED-DEVICE").timestamp(new Date()).utcOffset("-0500")
                        .parameters(parameters).attributes(setAttributes).build();

        repository.save(set);

        final ObservationSet updateSet = new ObservationSet();
        BeanUtils.copyProperties(set, updateSet);
        updateSet.setOrigin("Device5");

        final ObservationSet updatedSet = controller.updateObservationSet(set.getId(), updateSet);

        assertEquals(set.getId(), updatedSet.getId(), "incorrect property value");
        assertEquals("Device5", updatedSet.getOrigin(), "incorrect property value");
    }

    @Test
    public void test_delete_happy() throws NotFoundException {
        final Map<String, String> parameterAttributes = new HashMap<>();
        parameterAttributes.put("PATIENT_STATE", "BIZARRE");
        final List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name("ICP").description("Intracranial Pressure").uom("mmHg").timestamp(new Date()).utcOffset("-500").value("72")
                        .attributes(parameterAttributes).build());
        final Map<String, String> setAttributes = new HashMap<>();
        setAttributes.put("DERIVATION", "MACHINE");
        final ObservationSet set = ObservationSet.builder().origin("Device6").originType("MED-DEVICE").timestamp(new Date()).utcOffset("-0500")
                        .parameters(parameters).attributes(setAttributes).build();

        repository.save(set);

        controller.deleteObservationSet(set.getId());

        assertFalse(repository.existsById(set.getId()), "should be false");
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
    }

}
