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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpcm.dao.RoleRepository;
import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.Role;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public class RoleServiceTest {

    private RoleService service;

    @Mock
    private RoleRepository mockRepo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        service = new RoleService(mockRepo);
    }

    @DisplayName("Test role is saved and id generated")
    @Test
    public void test_create_happy() throws DataViolationException {
        final Role type = Role.builder().name("name").build();
        final Role result = new Role();
        BeanUtils.copyProperties(type, result);
        result.setId(1L);
        when(mockRepo.save(any(Role.class))).thenReturn(result);

        final Role resultingType = service.create(type);

        assertEquals((Long) 1L, resultingType.getId());
        assertEquals("name", resultingType.getName());
    }

    @DisplayName("Test role with zero id is saved and id generated")
    @Test
    public void test_create_happyAcceptsZeroId() throws DataViolationException {
        final Role type = Role.builder().name("name").id(0L).build();
        final Role result = new Role();
        BeanUtils.copyProperties(type, result);
        result.setId(1L);
        when(mockRepo.save(any(Role.class))).thenReturn(result);

        final Role resultingType = service.create(type);

        assertEquals((Long) 1L, resultingType.getId());
        assertEquals("name", resultingType.getName());
    }

    @DisplayName("Test transient role throws error")
    @Test
    public void test_create_handlesTransientException() throws DataViolationException {
        Assertions.assertThrows(DataViolationException.class, () -> {
            final Role type = Role.builder().name("name").id(3L).build();
            service.create(type);
        });
    }

    @DisplayName("Test role can be read by id")
    @Test
    public void test_read_byId_happy() throws DataViolationException, NotFoundException {
        final Long id = 3L;
        final Role type = Role.builder().name("name").id(id).build();
        final Optional<Role> optional = Optional.of(type);
        when(mockRepo.findById(3L)).thenReturn(optional);

        final Role resultingType = service.read(id);

        assertEquals(id, resultingType.getId());
        assertEquals("name", resultingType.getName());
    }

    @DisplayName("Test unknown role read throws error")
    @Test
    public void test_read_handlesNotFound() throws NotFoundException {
        Assertions.assertThrows(NotFoundException.class, () -> {
            final Optional<Role> optional = Optional.empty();
            when(mockRepo.findById(3L)).thenReturn(optional);
            service.read(3L);
        });
    }

    @DisplayName("Test role can be read as page")
    @Test
    public void test_read_pagination_happy() {
        final PageRequest request = PageRequest.of(0, 10);
        final List<Role> typeList = new ArrayList<>();
        typeList.add(Role.builder().name("name").id(1L).build());
        final Page<Role> typePage = new PageImpl<>(typeList);
        when(mockRepo.findAll(request)).thenReturn(typePage);

        final Page<Role> resultPage = service.read(request);

        assertEquals(1, resultPage.getNumberOfElements());
        assertEquals((Long) 1L, resultPage.getContent().get(0).getId());
    }

    @DisplayName("Test role can be updated")
    @Test
    public void test_update_happy() throws NotFoundException {
        final Long id = 3L;
        final Role role = Role.builder().name("name").id(id).build();
        final Role dbRole = new Role();
        BeanUtils.copyProperties(role, dbRole);
        final Optional<Role> dbOptional = Optional.of(dbRole);

        when(mockRepo.findById(id)).thenReturn(dbOptional);
        when(mockRepo.save(role)).thenReturn(role);

        final Role resultingType = service.update(id, role);
        assertEquals(id, resultingType.getId());
        assertEquals("name", resultingType.getName());
    }

    @DisplayName("Test unknown role update throws error")
    @Test
    public void test_update_handlesNotFound() throws NotFoundException {
        Assertions.assertThrows(NotFoundException.class, () -> {
            final Long id = 3L;
            final Optional<Role> dbOptional = Optional.empty();
            when(mockRepo.findById(anyLong())).thenReturn(dbOptional);

            service.update(id, new Role());
        });
    }

    @DisplayName("Test role can be deleted")
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

    @DisplayName("Test unknown role delete throws error")
    @Test
    public void test_delete_handlesNotFound() {
        final Long id = 3L;
        when(mockRepo.existsById(anyLong())).thenReturn(false);

        try {
            service.delete(id);
        } catch (final Exception e) {
            fail(e.getMessage());
        }

        verify(mockRepo, times(0)).deleteById(anyLong());
    }
}
