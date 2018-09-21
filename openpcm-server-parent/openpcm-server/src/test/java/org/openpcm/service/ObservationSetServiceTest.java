package org.openpcm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpcm.dao.ObservationSetRepository;
import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.ObservationSet;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public class ObservationSetServiceTest {

    @InjectMocks
    private ObservationSetService service;

    @Mock
    private ObservationSetRepository mockRepo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("Test observation set is saved and id generated")
    @Test
    public void test_create_happy() throws DataViolationException {
        final ObservationSet type = ObservationSet.builder().origin("Device1").originType("MED-DEVICE").build();
        final ObservationSet result = new ObservationSet();
        BeanUtils.copyProperties(type, result);
        result.setId(1L);
        when(mockRepo.save(any(ObservationSet.class))).thenReturn(result);

        final ObservationSet resultingType = service.create(type);

        assertEquals((Long) 1L, resultingType.getId(), "property is incorrect");
        assertEquals("Device1", resultingType.getOrigin(), "property is incorrect");
        assertEquals("MED-DEVICE", resultingType.getOriginType(), "property is incorrect");
    }

    @DisplayName("Test observation with zero id set is saved and id generated")
    @Test
    public void test_create_happyWithZeroId() throws DataViolationException {
        final ObservationSet type = ObservationSet.builder().origin("Device1").originType("MED-DEVICE").id(0L).build();
        final ObservationSet result = new ObservationSet();
        BeanUtils.copyProperties(type, result);
        result.setId(1L);
        when(mockRepo.save(any(ObservationSet.class))).thenReturn(result);

        final ObservationSet resultingType = service.create(type);

        assertEquals((Long) 1L, resultingType.getId(), "property is incorrect");
        assertEquals("Device1", resultingType.getOrigin(), "property is incorrect");
        assertEquals("MED-DEVICE", resultingType.getOriginType(), "property is incorrect");
    }

    @DisplayName("Test observation is reject if its transient object")
    @Test
    public void test_create_handlesTransientException() throws DataViolationException {
        Assertions.assertThrows(DataViolationException.class, () -> {
            final ObservationSet type = ObservationSet.builder().origin("Device1").originType("MED-DEVICE").id(3L).build();
            service.create(type);
        });
    }

    @DisplayName("Test observation can be read by id")
    @Test
    public void test_read_byId_happy() throws DataViolationException, NotFoundException {
        final Long id = 3L;
        final ObservationSet type = ObservationSet.builder().origin("Device1").originType("MED-DEVICE").id(id).build();
        final Optional<ObservationSet> optional = Optional.of(type);
        when(mockRepo.findById(3L)).thenReturn(optional);

        final ObservationSet resultingType = service.read(id);

        assertEquals(id, resultingType.getId(), "property is incorrect");
        assertEquals("Device1", resultingType.getOrigin(), "property is incorrect");
        assertEquals("MED-DEVICE", resultingType.getOriginType(), "property is incorrect");
    }

    @DisplayName("Test read for unknown observation set throws error")
    @Test
    public void test_read_byId_handlesNotFound() throws NotFoundException {
        Assertions.assertThrows(NotFoundException.class, () -> {
            final Optional<ObservationSet> optional = Optional.empty();
            when(mockRepo.findById(3L)).thenReturn(optional);
            service.read(3L);
        });
    }

    @DisplayName("Test observation set can be read as page")
    @Test
    public void test_read_pagination_happy() {
        final PageRequest request = PageRequest.of(0, 10);
        final List<ObservationSet> typeList = new ArrayList<>();
        typeList.add(ObservationSet.builder().origin("Device1").originType("MED-DEVICE").id(1L).build());
        final Page<ObservationSet> typePage = new PageImpl<>(typeList);
        when(mockRepo.findAll(request)).thenReturn(typePage);

        final Page<ObservationSet> resultPage = service.read(request);

        assertEquals(1, resultPage.getNumberOfElements(), "property is incorrect");
        assertEquals((Long) 1L, resultPage.getContent().get(0).getId(), "property is incorrect");
    }

    @DisplayName("Test observation set can be updated")
    @Test
    public void test_update_happy() throws NotFoundException {
        final Long id = 3L;
        final ObservationSet type = ObservationSet.builder().origin("Device1").originType("MED-DEVICE").id(id).build();
        final ObservationSet dbType = new ObservationSet();
        BeanUtils.copyProperties(type, dbType);
        final Optional<ObservationSet> dbOptional = Optional.of(dbType);

        when(mockRepo.findById(id)).thenReturn(dbOptional);
        when(mockRepo.save(type)).thenReturn(type);

        final ObservationSet resultingType = service.update(id, type);
        assertEquals(id, resultingType.getId(), "property is incorrect");
        assertEquals("Device1", resultingType.getOrigin(), "property is incorrect");
        assertEquals("MED-DEVICE", resultingType.getOriginType(), "property is incorrect");
    }

    @DisplayName("Test unknown observation set can not be updated")
    public void test_update_handlesNotFound() throws NotFoundException {
        Assertions.assertThrows(NotFoundException.class, () -> {
            final Long id = 3L;
            final Optional<ObservationSet> dbOptional = Optional.empty();
            when(mockRepo.findById(anyLong())).thenReturn(dbOptional);

            service.update(id, new ObservationSet());
        });
    }

    @DisplayName("Test observation set can be deleted")
    @Test
    public void test_delete_happy() {
        final Long id = 3L;
        when(mockRepo.existsById(anyLong())).thenReturn(true);

        try {
            service.delete(id);
        } catch (final Exception e) {
            fail(e.getMessage());
        }

        verify(mockRepo, times(1)).deleteById(anyLong());
    }

    @DisplayName("Test deletion attempt of unknown observation throws error")
    @Test
    public void test_delete_handlesNotFound() {
        final Long id = 3L;
        when(mockRepo.existsById(anyLong())).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> {
            service.delete(id);
        });
    }
}
