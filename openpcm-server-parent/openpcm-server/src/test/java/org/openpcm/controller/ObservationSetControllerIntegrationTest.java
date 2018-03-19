package org.openpcm.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openpcm.OpenPCMApplication;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = { OpenPCMApplication.class })
public class ObservationSetControllerIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObservationSetControllerIntegrationTest.class);

    @Autowired
    private ObservationSetController controller;

    @Autowired
    private ObservationSetRepository repository;

    @Test
    public void test_create_happy() throws DataViolationException {
        Map<String, String> parameterAttributes = new HashMap<>();
        parameterAttributes.put("PATIENT_STATE", "STANDING");
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name("HR").description("Heart Rate").uom("bpm").timestamp(new Date()).utcOffset("-500").value("100")
                        .attributes(parameterAttributes).build());
        Map<String, String> setAttributes = new HashMap<>();
        setAttributes.put("DOCUMENTATION_TYPE", "MANUAL");
        ObservationSet set = ObservationSet.builder().origin("Device1").originType("CLINICIAN").timestamp(new Date()).utcOffset("-0500").parameters(parameters)
                        .attributes(setAttributes).build();

        ObservationSet result = controller.createObservationSet(set);

        assertNotNull("instance should not be null", result.getId());
        assertNotNull("instance should not be null", result.getParameters().get(0).getId());
        LOGGER.debug("saved result: {}", result);
    }

    @Test
    public void test_read_pagination_happy() {
        Map<String, String> parameterAttributes = new HashMap<>();
        parameterAttributes.put("PATIENT_STATE", "SITTING");
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name("SPO2").description("SpO2").uom("%").timestamp(new Date()).utcOffset("-500").value("100")
                        .attributes(parameterAttributes).build());
        Map<String, String> setAttributes = new HashMap<>();
        setAttributes.put("DOCUMENTATION_TYPE", "MDI");
        ObservationSet set = ObservationSet.builder().origin("Device2").originType("MED-DEVICE").timestamp(new Date()).utcOffset("-0500").parameters(parameters)
                        .attributes(setAttributes).build();

        repository.save(set);

        Page<ObservationSet> page = controller.readObservationSets(PageRequest.of(0, 20));

        assertEquals("incorrect number of elements", 1, page.getNumberOfElements());
        assertEquals("incorrect property value", set.getId(), page.getContent().get(0).getId());
        assertEquals("incorrect property value", set.getParameters().get(0).getId(), page.getContent().get(0).getParameters().get(0).getId());
    }

    @Test
    public void test_read_byId_happy() throws NotFoundException {
        Map<String, String> parameterAttributes = new HashMap<>();
        parameterAttributes.put("PATIENT_STATE", "UNCONSCIOUS");
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name("RESP_IMP").description("Respiration Impedance").uom("bpm").timestamp(new Date()).utcOffset("-500").value("12")
                        .attributes(parameterAttributes).build());
        Map<String, String> setAttributes = new HashMap<>();
        setAttributes.put("DOCUMENTATION_TYPE", "MACH_CALC");
        ObservationSet set = ObservationSet.builder().origin("Device3").originType("MED-DEVICE").timestamp(new Date()).utcOffset("-0500").parameters(parameters)
                        .attributes(setAttributes).build();

        repository.save(set);

        ObservationSet readSet = controller.readObservationSet(set.getId());

        assertEquals("incorrect property value", set.getId(), readSet.getId());
        assertEquals("incorrect property value", set.getParameters().get(0).getId(), readSet.getParameters().get(0).getId());
    }

    @Test
    public void test_update_happy() throws NotFoundException {
        Map<String, String> parameterAttributes = new HashMap<>();
        parameterAttributes.put("PATIENT_STATE", "LYING");
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name("RESP_RATE").description("Respiration Rate").uom("bpm").timestamp(new Date()).utcOffset("-500").value("17")
                        .attributes(parameterAttributes).build());
        Map<String, String> setAttributes = new HashMap<>();
        setAttributes.put("DERIVATION", "STOP_WATCH");
        ObservationSet set = ObservationSet.builder().origin("Device4").originType("MED-DEVICE").timestamp(new Date()).utcOffset("-0500").parameters(parameters)
                        .attributes(setAttributes).build();

        repository.save(set);

        ObservationSet updateSet = new ObservationSet();
        BeanUtils.copyProperties(set, updateSet);
        updateSet.setOrigin("Device5");

        ObservationSet updatedSet = controller.updateObservationSet(set.getId(), updateSet);

        assertEquals("incorrect property value", set.getId(), updatedSet.getId());
        assertEquals("incorrect property value", "Device5", updatedSet.getOrigin());
    }

    @Test
    public void test_delete_happy() throws NotFoundException {
        Map<String, String> parameterAttributes = new HashMap<>();
        parameterAttributes.put("PATIENT_STATE", "BIZARRE");
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name("ICP").description("Intracranial Pressure").uom("mmHg").timestamp(new Date()).utcOffset("-500").value("72")
                        .attributes(parameterAttributes).build());
        Map<String, String> setAttributes = new HashMap<>();
        setAttributes.put("DERIVATION", "MACHINE");
        ObservationSet set = ObservationSet.builder().origin("Device6").originType("MED-DEVICE").timestamp(new Date()).utcOffset("-0500").parameters(parameters)
                        .attributes(setAttributes).build();

        repository.save(set);

        controller.deleteObservationSet(set.getId());

        assertFalse("should be false", repository.existsById(set.getId()));
    }

    @After
    public void tearDown() {
        repository.deleteAll();
    }

}
