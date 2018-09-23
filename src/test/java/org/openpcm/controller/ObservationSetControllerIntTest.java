package org.openpcm.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URISyntaxException;

import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openpcm.annotation.IntegrationTest;
import org.openpcm.dao.ObservationSetRepository;
import org.openpcm.model.AuthSuccess;
import org.openpcm.model.ObservationSet;
import org.openpcm.util.TestAuthenticationUtils;
import org.openpcm.utils.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Category(IntegrationTest.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class ObservationSetControllerIntTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObservationSetControllerIntTest.class);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestAuthenticationUtils authentication;

    private AuthSuccess authSuccess;

    private HttpEntity authHeaders;

    private String base;

    @Autowired
    private ObservationSetController controller;

    @Autowired
    private ObservationSetRepository repository;

    @BeforeEach
    public void beforeEach() {
        base = "http://localhost:" + port;
        authSuccess = authentication.retrieveCreds(base);
    }

    @Test
    @DisplayName("Ensure observation set can be created")
    public void test_createSucceeds(@Autowired ObservationSet set) throws URISyntaxException {
        // final URI myUri = new URI();
        // LOGGER.warn("URI: {}", myUri.toString());
        authHeaders = authentication.convert(ObjectUtil.print(set), authSuccess);
        final ResponseEntity<ObservationSet> result = restTemplate.exchange(base + "/api/v1/observationset", HttpMethod.POST, authHeaders,
                        ObservationSet.class);
        assertNotNull(result.getBody().getId(), "instance should not be null");
        assertNotNull(result.getBody().getParameters().get(0).getId(), "instance should not be null");
    }

    // @Test
    // public void test_create_happy() throws DataViolationException {
    //
    // final ObservationSet result = controller.createObservationSet(set);
    //
    // }
    //
    // @Test
    // public void test_read_pagination_happy() {
    // final Map<String, String> parameterAttributes = new HashMap<>();
    // parameterAttributes.put("PATIENT_STATE", "SITTING");
    // final List<Parameter> parameters = new ArrayList<>();
    // parameters.add(Parameter.builder().name("SPO2").description("SpO2").uom("%").timestamp(new Date()).utcOffset("-500").value("100")
    // .attributes(parameterAttributes).build());
    // final Map<String, String> setAttributes = new HashMap<>();
    // setAttributes.put("DOCUMENTATION_TYPE", "MDI");
    // final ObservationSet set = ObservationSet.builder().origin("Device2").originType("MED-DEVICE").timestamp(new Date()).utcOffset("-0500")
    // .parameters(parameters).attributes(setAttributes).build();
    //
    // repository.save(set);
    //
    // final Page<ObservationSet> page = controller.readObservationSets(PageRequest.of(0, 20));
    //
    // assertEquals(1, page.getNumberOfElements(), "incorrect number of elements");
    // assertEquals(set.getId(), page.getContent().get(0).getId(), "incorrect property value");
    // assertEquals(set.getParameters().get(0).getId(), page.getContent().get(0).getParameters().get(0).getId(), "incorrect property value");
    // }
    //
    // @Test
    // public void test_read_byId_happy() throws NotFoundException {
    // final Map<String, String> parameterAttributes = new HashMap<>();
    // parameterAttributes.put("PATIENT_STATE", "UNCONSCIOUS");
    // final List<Parameter> parameters = new ArrayList<>();
    // parameters.add(Parameter.builder().name("RESP_IMP").description("Respiration Impedance").uom("bpm").timestamp(new Date()).utcOffset("-500").value("12")
    // .attributes(parameterAttributes).build());
    // final Map<String, String> setAttributes = new HashMap<>();
    // setAttributes.put("DOCUMENTATION_TYPE", "MACH_CALC");
    // final ObservationSet set = ObservationSet.builder().origin("Device3").originType("MED-DEVICE").timestamp(new Date()).utcOffset("-0500")
    // .parameters(parameters).attributes(setAttributes).build();
    //
    // repository.save(set);
    //
    // final ObservationSet readSet = controller.readObservationSet(set.getId());
    //
    // assertEquals(set.getId(), readSet.getId(), "incorrect property value");
    // assertEquals(set.getParameters().get(0).getId(), readSet.getParameters().get(0).getId(), "incorrect property value");
    // }
    //
    // @Test
    // public void test_update_happy() throws NotFoundException {
    // final Map<String, String> parameterAttributes = new HashMap<>();
    // parameterAttributes.put("PATIENT_STATE", "LYING");
    // final List<Parameter> parameters = new ArrayList<>();
    // parameters.add(Parameter.builder().name("RESP_RATE").description("Respiration Rate").uom("bpm").timestamp(new Date()).utcOffset("-500").value("17")
    // .attributes(parameterAttributes).build());
    // final Map<String, String> setAttributes = new HashMap<>();
    // setAttributes.put("DERIVATION", "STOP_WATCH");
    // final ObservationSet set = ObservationSet.builder().origin("Device4").originType("MED-DEVICE").timestamp(new Date()).utcOffset("-0500")
    // .parameters(parameters).attributes(setAttributes).build();
    //
    // repository.save(set);
    //
    // final ObservationSet updateSet = new ObservationSet();
    // BeanUtils.copyProperties(set, updateSet);
    // updateSet.setOrigin("Device5");
    //
    // final ObservationSet updatedSet = controller.updateObservationSet(set.getId(), updateSet);
    //
    // assertEquals(set.getId(), updatedSet.getId(), "incorrect property value");
    // assertEquals("Device5", updatedSet.getOrigin(), "incorrect property value");
    // }
    //
    // @Test
    // public void test_delete_happy() throws NotFoundException {
    // final Map<String, String> parameterAttributes = new HashMap<>();
    // parameterAttributes.put("PATIENT_STATE", "BIZARRE");
    // final List<Parameter> parameters = new ArrayList<>();
    // parameters.add(Parameter.builder().name("ICP").description("Intracranial Pressure").uom("mmHg").timestamp(new Date()).utcOffset("-500").value("72")
    // .attributes(parameterAttributes).build());
    // final Map<String, String> setAttributes = new HashMap<>();
    // setAttributes.put("DERIVATION", "MACHINE");
    // final ObservationSet set = ObservationSet.builder().origin("Device6").originType("MED-DEVICE").timestamp(new Date()).utcOffset("-0500")
    // .parameters(parameters).attributes(setAttributes).build();
    //
    // repository.save(set);
    //
    // controller.deleteObservationSet(set.getId());
    //
    // assertFalse(repository.existsById(set.getId()), "should be false");
    // }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
    }

}
