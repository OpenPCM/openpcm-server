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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        service = new RoleService(mockRepo);
    }

    @Test
    public void test_create_happy() throws DataViolationException {
        Role type = Role.builder().name("name").build();
        Role result = new Role();
        BeanUtils.copyProperties(type, result);
        result.setId(1L);
        when(mockRepo.save(any(Role.class))).thenReturn(result);

        Role resultingType = service.create(type);

        assertEquals("property is incorrect", (Long) 1L, resultingType.getId());
        assertEquals("property is incorrect", "name", resultingType.getName());
    }

    @Test
    public void test_create_happyAcceptsZeroId() throws DataViolationException {
        Role type = Role.builder().name("name").id(0L).build();
        Role result = new Role();
        BeanUtils.copyProperties(type, result);
        result.setId(1L);
        when(mockRepo.save(any(Role.class))).thenReturn(result);

        Role resultingType = service.create(type);

        assertEquals("property is incorrect", (Long) 1L, resultingType.getId());
        assertEquals("property is incorrect", "name", resultingType.getName());
    }

    @Test(expected = DataViolationException.class)
    public void test_create_handlesTransientException() throws DataViolationException {
        Role type = Role.builder().name("name").id(3L).build();

        service.create(type);
    }

    @Test
    public void test_read_byId_happy() throws DataViolationException, NotFoundException {
        Long id = 3L;
        Role type = Role.builder().name("name").id(id).build();
        Optional<Role> optional = Optional.of(type);
        when(mockRepo.findById(3L)).thenReturn(optional);

        Role resultingType = service.read(id);

        assertEquals("property is incorrect", id, resultingType.getId());
        assertEquals("property is incorrect", "name", resultingType.getName());
    }

    @Test(expected = NotFoundException.class)
    public void test_read_handlesNotFound() throws NotFoundException {
        Optional<Role> optional = Optional.empty();
        when(mockRepo.findById(3L)).thenReturn(optional);
        service.read((Long) 3L);
    }

    @Test
    public void test_read_pagination_happy() {
        PageRequest request = PageRequest.of(0, 10);
        List<Role> typeList = new ArrayList<>();
        typeList.add(Role.builder().name("name").id(1L).build());
        Page<Role> typePage = new PageImpl<>(typeList);
        when(mockRepo.findAll(request)).thenReturn(typePage);

        Page<Role> resultPage = service.read(request);

        assertEquals("property is incorrect", 1, resultPage.getNumberOfElements());
        assertEquals("property is incorrect", (Long) 1L, resultPage.getContent().get(0).getId());
    }

    @Test
    public void test_update_happy() throws NotFoundException {
        Long id = 3L;
        Role role = Role.builder().name("name").id(id).build();
        Role dbRole = new Role();
        BeanUtils.copyProperties(role, dbRole);
        Optional<Role> dbOptional = Optional.of(dbRole);

        when(mockRepo.findById(id)).thenReturn(dbOptional);
        when(mockRepo.save(role)).thenReturn(role);

        Role resultingType = service.update(id, role);
        assertEquals("property is incorrect", id, resultingType.getId());
        assertEquals("property is incorrect", "name", resultingType.getName());
    }

    @Test(expected = NotFoundException.class)
    public void test_update_handlesNotFound() throws NotFoundException {
        Long id = 3L;
        Optional<Role> dbOptional = Optional.empty();
        when(mockRepo.findById(anyLong())).thenReturn(dbOptional);

        service.update(id, new Role());
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
