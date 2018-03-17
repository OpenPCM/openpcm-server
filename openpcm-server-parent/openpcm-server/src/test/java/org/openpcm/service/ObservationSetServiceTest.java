package org.openpcm.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_create_happy() throws DataViolationException {
        ObservationSet type = ObservationSet.builder().origin("Device1").originType("MED-DEVICE").build();
        ObservationSet result = new ObservationSet();
        BeanUtils.copyProperties(type, result);
        result.setId(1L);
        when(mockRepo.save(any(ObservationSet.class))).thenReturn(result);

        ObservationSet resultingType = service.create(type);

        assertEquals("property is incorrect", (Long) 1L, resultingType.getId());
        assertEquals("property is incorrect", "Device1", resultingType.getOrigin());
        assertEquals("property is incorrect", "MED-DEVICE", resultingType.getOriginType());
    }

    @Test
    public void test_create_happyWithZeroId() throws DataViolationException {
        ObservationSet type = ObservationSet.builder().origin("Device1").originType("MED-DEVICE").id(0L).build();
        ObservationSet result = new ObservationSet();
        BeanUtils.copyProperties(type, result);
        result.setId(1L);
        when(mockRepo.save(any(ObservationSet.class))).thenReturn(result);

        ObservationSet resultingType = service.create(type);

        assertEquals("property is incorrect", (Long) 1L, resultingType.getId());
        assertEquals("property is incorrect", "Device1", resultingType.getOrigin());
        assertEquals("property is incorrect", "MED-DEVICE", resultingType.getOriginType());
    }

    @Test(expected = DataViolationException.class)
    public void test_create_handlesTransientException() throws DataViolationException {
        ObservationSet type = ObservationSet.builder().origin("Device1").originType("MED-DEVICE").id(3L).build();

        service.create(type);
    }

    @Test
    public void test_read_byId_happy() throws DataViolationException, NotFoundException {
        Long id = 3L;
        ObservationSet type = ObservationSet.builder().origin("Device1").originType("MED-DEVICE").id(id).build();
        Optional<ObservationSet> optional = Optional.of(type);
        when(mockRepo.findById(3L)).thenReturn(optional);

        ObservationSet resultingType = service.read(id);

        assertEquals("property is incorrect", id, resultingType.getId());
        assertEquals("property is incorrect", "Device1", resultingType.getOrigin());
        assertEquals("property is incorrect", "MED-DEVICE", resultingType.getOriginType());
    }

    @Test(expected = NotFoundException.class)
    public void test_read_byId_handlesNotFound() throws NotFoundException {
        Optional<ObservationSet> optional = Optional.empty();
        when(mockRepo.findById(3L)).thenReturn(optional);
        service.read((Long) 3L);
    }

    @Test
    public void test_read_pagination_happy() {
        PageRequest request = PageRequest.of(0, 10);
        List<ObservationSet> typeList = new ArrayList<>();
        typeList.add(ObservationSet.builder().origin("Device1").originType("MED-DEVICE").id(1L).build());
        Page<ObservationSet> typePage = new PageImpl<>(typeList);
        when(mockRepo.findAll(request)).thenReturn(typePage);

        Page<ObservationSet> resultPage = service.read(request);

        assertEquals("property is incorrect", 1, resultPage.getNumberOfElements());
        assertEquals("property is incorrect", (Long) 1L, resultPage.getContent().get(0).getId());
    }

    @Test
    public void test_update_happy() throws NotFoundException {
        Long id = 3L;
        ObservationSet type = ObservationSet.builder().origin("Device1").originType("MED-DEVICE").id(id).build();
        ObservationSet dbType = new ObservationSet();
        BeanUtils.copyProperties(type, dbType);
        Optional<ObservationSet> dbOptional = Optional.of(dbType);

        when(mockRepo.findById(id)).thenReturn(dbOptional);
        when(mockRepo.save(type)).thenReturn(type);

        ObservationSet resultingType = service.update(id, type);
        assertEquals("property is incorrect", id, resultingType.getId());
        assertEquals("property is incorrect", "Device1", resultingType.getOrigin());
        assertEquals("property is incorrect", "MED-DEVICE", resultingType.getOriginType());
    }

    @Test(expected = NotFoundException.class)
    public void test_update_handlesNotFound() throws NotFoundException {
        Long id = 3L;
        Optional<ObservationSet> dbOptional = Optional.empty();
        when(mockRepo.findById(anyLong())).thenReturn(dbOptional);

        service.update(id, new ObservationSet());
    }

    @Test
    public void test_delete_happy() {
        Long id = 3L;
        when(mockRepo.existsById(anyLong())).thenReturn(false);

        try {
            service.delete(id);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        verify(mockRepo, times(0)).deleteById(anyLong());
    }

    @Test
    public void test_delete_handlesNotFound() {
        Long id = 3L;
        when(mockRepo.existsById(anyLong())).thenReturn(true);

        try {
            service.delete(id);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        verify(mockRepo, times(1)).deleteById(anyLong());
    }
}
