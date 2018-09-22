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
import org.openpcm.dao.ParameterTypeRepository;
import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.ParameterType;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public class ParameterTypeServiceTest {

    @InjectMocks
    private ParameterTypeService service;

    @Mock
    private ParameterTypeRepository mockRepo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("Test parameter type is saved and id generated")
    @Test
    public void test_create_happy() throws DataViolationException {
        final ParameterType type = ParameterType.builder().description("description").name("name").uom("uom").build();
        final ParameterType result = new ParameterType();
        BeanUtils.copyProperties(type, result);
        result.setId(1L);
        when(mockRepo.save(any(ParameterType.class))).thenReturn(result);

        final ParameterType resultingType = service.create(type);

        assertEquals((Long) 1L, resultingType.getId(), "property is incorrect");
        assertEquals("name", resultingType.getName(), "property is incorrect");
        assertEquals("description", resultingType.getDescription(), "property is incorrect");
        assertEquals("uom", resultingType.getUom(), "property is incorrect");
    }

    @DisplayName("Test parameter type with zero id is saved and id generated")
    @Test
    public void test_create_happyAcceptsZeroId() throws DataViolationException {
        final ParameterType type = ParameterType.builder().description("description").name("name").uom("uom").id(0L).build();
        final ParameterType result = new ParameterType();
        BeanUtils.copyProperties(type, result);
        result.setId(1L);
        when(mockRepo.save(any(ParameterType.class))).thenReturn(result);

        final ParameterType resultingType = service.create(type);

        assertEquals((Long) 1L, resultingType.getId(), "property is incorrect");
        assertEquals("name", resultingType.getName(), "property is incorrect");
        assertEquals("description", resultingType.getDescription(), "property is incorrect");
        assertEquals("uom", resultingType.getUom(), "property is incorrect");
    }

    @DisplayName("Test parameter type is rejected if transient")
    @Test
    public void test_create_handlesTransientException() throws DataViolationException {
        Assertions.assertThrows(DataViolationException.class, () -> {
            final ParameterType type = ParameterType.builder().description("description").name("name").uom("uom").id(3L).build();
            service.create(type);
        });
    }

    @DisplayName("Test parameter type can be read by id")
    @Test
    public void test_read_byId_happy() throws DataViolationException, NotFoundException {
        final Long id = 3L;
        final ParameterType type = ParameterType.builder().description("description").name("name").uom("uom").id(id).build();
        final Optional<ParameterType> optional = Optional.of(type);
        when(mockRepo.findById(3L)).thenReturn(optional);

        final ParameterType resultingType = service.read(id);

        assertEquals(id, resultingType.getId(), "property is incorrect");
        assertEquals("name", resultingType.getName(), "property is incorrect");
        assertEquals("description", resultingType.getDescription(), "property is incorrect");
        assertEquals("uom", resultingType.getUom(), "property is incorrect");
    }

    @DisplayName("Test unknown parameter type read throws error")
    @Test
    public void test_read_byId_handlesNotFound() throws NotFoundException {
        Assertions.assertThrows(NotFoundException.class, () -> {
            final Optional<ParameterType> optional = Optional.empty();
            when(mockRepo.findById(3L)).thenReturn(optional);
            service.read(3L);
        });
    }

    @DisplayName("Test parameter type can be read as page")
    @Test
    public void test_read_pagination_happy() {
        final PageRequest request = PageRequest.of(0, 10);
        final List<ParameterType> typeList = new ArrayList<>();
        typeList.add(ParameterType.builder().description("description").name("name").uom("uom").id(1L).build());
        final Page<ParameterType> typePage = new PageImpl<>(typeList);
        when(mockRepo.findAll(request)).thenReturn(typePage);

        final Page<ParameterType> resultPage = service.read(request);

        assertEquals(1, resultPage.getNumberOfElements(), "property is incorrect");
        assertEquals((Long) 1L, resultPage.getContent().get(0).getId(), "property is incorrect");
    }

    @DisplayName("Test parameter type can be updated")
    @Test
    public void test_update_happy() throws NotFoundException {
        final Long id = 3L;
        final ParameterType type = ParameterType.builder().description("description").name("name").uom("uom").id(id).build();
        final ParameterType dbType = new ParameterType();
        BeanUtils.copyProperties(type, dbType);
        final Optional<ParameterType> dbOptional = Optional.of(dbType);

        when(mockRepo.findById(id)).thenReturn(dbOptional);
        when(mockRepo.save(type)).thenReturn(type);

        final ParameterType resultingType = service.update(id, type);
        assertEquals(id, resultingType.getId(), "property is incorrect");
        assertEquals("name", resultingType.getName(), "property is incorrect");
        assertEquals("description", resultingType.getDescription(), "property is incorrect");
        assertEquals("uom", resultingType.getUom(), "property is incorrect");
    }

    @DisplayName("Test unknown parameter type update request throws error")
    @Test
    public void test_update_handlesNotFound() throws NotFoundException {
        Assertions.assertThrows(NotFoundException.class, () -> {
            final Long id = 3L;
            final Optional<ParameterType> dbOptional = Optional.empty();
            when(mockRepo.findById(anyLong())).thenReturn(dbOptional);

            service.update(id, new ParameterType());
        });
    }

    @DisplayName("Test parameter type can be deleted")
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

    @DisplayName("Test unknown parameter type delete request throws error")
    @Test
    public void test_delete_handlesNotFound() {
        final Long id = 3L;
        when(mockRepo.existsById(anyLong())).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> {
            service.delete(id);
        });
    }
}
