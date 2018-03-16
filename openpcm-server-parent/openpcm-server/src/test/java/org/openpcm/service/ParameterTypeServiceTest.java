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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpcm.dao.ParameterTypeRepository;
import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.ParameterType;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public class ParameterTypeServiceTest {

    private ParameterTypeService service;

    @Mock
    private ParameterTypeRepository mockRepo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        service = new ParameterTypeService(mockRepo);
    }

    @Test
    public void test_create_happy() throws DataViolationException {
        ParameterType type = ParameterType.builder().description("description").name("name").uom("uom").build();
        ParameterType result = new ParameterType();
        BeanUtils.copyProperties(type, result);
        result.setId(1L);
        when(mockRepo.save(any(ParameterType.class))).thenReturn(result);

        ParameterType resultingType = service.create(type);

        assertEquals("property is incorrect", (Long) 1L, resultingType.getId());
        assertEquals("property is incorrect", "name", resultingType.getName());
        assertEquals("property is incorrect", "description", resultingType.getDescription());
        assertEquals("property is incorrect", "uom", resultingType.getUom());
    }

    @Test
    public void test_create_happyAcceptsZeroId() throws DataViolationException {
        ParameterType type = ParameterType.builder().description("description").name("name").uom("uom").id(0L).build();
        ParameterType result = new ParameterType();
        BeanUtils.copyProperties(type, result);
        result.setId(1L);
        when(mockRepo.save(any(ParameterType.class))).thenReturn(result);

        ParameterType resultingType = service.create(type);

        assertEquals("property is incorrect", (Long) 1L, resultingType.getId());
        assertEquals("property is incorrect", "name", resultingType.getName());
        assertEquals("property is incorrect", "description", resultingType.getDescription());
        assertEquals("property is incorrect", "uom", resultingType.getUom());
    }

    @Test(expected = DataViolationException.class)
    public void test_create_handlesTransientException() throws DataViolationException {
        ParameterType type = ParameterType.builder().description("description").name("name").uom("uom").id(3L).build();

        service.create(type);
    }

    @Test
    public void test_read_byId_happy() throws DataViolationException, NotFoundException {
        Long id = 3L;
        ParameterType type = ParameterType.builder().description("description").name("name").uom("uom").id(id).build();
        Optional<ParameterType> optional = Optional.of(type);
        when(mockRepo.findById(3L)).thenReturn(optional);

        ParameterType resultingType = service.read(id);

        assertEquals("property is incorrect", id, resultingType.getId());
        assertEquals("property is incorrect", "name", resultingType.getName());
        assertEquals("property is incorrect", "description", resultingType.getDescription());
        assertEquals("property is incorrect", "uom", resultingType.getUom());
    }

    @Test(expected = NotFoundException.class)
    public void test_read_byId_handlesNotFound() throws NotFoundException {
        Optional<ParameterType> optional = Optional.empty();
        when(mockRepo.findById(3L)).thenReturn(optional);
        service.read((Long) 3L);
    }

    @Test
    public void test_read_pagination_happy() {
        PageRequest request = PageRequest.of(0, 10);
        List<ParameterType> typeList = new ArrayList<>();
        typeList.add(ParameterType.builder().description("description").name("name").uom("uom").id(1L).build());
        Page<ParameterType> typePage = new PageImpl<>(typeList);
        when(mockRepo.findAll(request)).thenReturn(typePage);

        Page<ParameterType> resultPage = service.read(request);

        assertEquals("property is incorrect", 1, resultPage.getNumberOfElements());
        assertEquals("property is incorrect", (Long) 1L, resultPage.getContent().get(0).getId());
    }

    @Test
    public void test_update_happy() throws NotFoundException {
        Long id = 3L;
        ParameterType type = ParameterType.builder().description("description").name("name").uom("uom").id(id).build();
        ParameterType dbType = new ParameterType();
        BeanUtils.copyProperties(type, dbType);
        Optional<ParameterType> dbOptional = Optional.of(dbType);

        when(mockRepo.findById(id)).thenReturn(dbOptional);
        when(mockRepo.save(type)).thenReturn(type);

        ParameterType resultingType = service.update(id, type);
        assertEquals("property is incorrect", id, resultingType.getId());
        assertEquals("property is incorrect", "name", resultingType.getName());
        assertEquals("property is incorrect", "description", resultingType.getDescription());
        assertEquals("property is incorrect", "uom", resultingType.getUom());
    }

    @Test(expected = NotFoundException.class)
    public void test_update_handlesNotFound() throws NotFoundException {
        Long id = 3L;
        Optional<ParameterType> dbOptional = Optional.empty();
        when(mockRepo.findById(anyLong())).thenReturn(dbOptional);

        service.update(id, new ParameterType());
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
