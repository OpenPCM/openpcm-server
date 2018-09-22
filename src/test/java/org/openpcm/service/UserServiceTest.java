package org.openpcm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.openpcm.dao.UserRepository;
import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.Address;
import org.openpcm.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository mockRepo;

    @Mock
    private PasswordEncoder mockEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(mockEncoder.encode(anyString())).thenAnswer(invocation -> (String) invocation.getArguments()[0]);
    }

    @DisplayName("Test user is saved and id generated")
    @Test
    public void test_create_happy() throws DataViolationException {
        final User user = User.builder().username("username").build();
        final User result = new User();
        BeanUtils.copyProperties(user, result);
        result.setId(1L);
        when(mockRepo.save(any(User.class))).thenReturn(result);

        final User resultingType = userService.create(user);

        assertEquals((Long) 1L, resultingType.getId(), "property is incorrect");
        assertEquals("username", resultingType.getUsername(), "property is incorrect");
    }

    @DisplayName("Test user is saved with address and id generated")
    @Test
    public void test_create_happyWithAddress() throws DataViolationException {
        final Address address = Address.builder().addressLineOne("123 Main Street").city("City").state("CA").zipCode("32802").build();
        final User user = User.builder().username("username").password("password").address(address).build();
        final User result = new User();
        BeanUtils.copyProperties(user, result);
        result.setId(1L);
        when(mockRepo.save(any(User.class))).thenReturn(result);

        final User resultingType = userService.create(user);

        assertEquals((Long) 1L, resultingType.getId(), "property is incorrect");
        assertEquals("username", resultingType.getUsername(), "property is incorrect");
        assertEquals("password", resultingType.getPassword(), "property is incorrect");
    }

    @DisplayName("Test user is saved with address with zero id and id generated")
    @Test
    public void test_create_happyWithAddressWithZeroId() throws DataViolationException {
        final Address address = Address.builder().addressLineOne("123 Main Street").city("City").state("CA").zipCode("32802").build();
        final User user = User.builder().username("username").password("password").address(address).build();
        final User result = new User();
        BeanUtils.copyProperties(user, result);
        result.setId(1L);
        when(mockRepo.save(any(User.class))).thenReturn(result);

        final User resultingType = userService.create(user);

        assertEquals((Long) 1L, resultingType.getId(), "property is incorrect");
        assertEquals("username", resultingType.getUsername(), "property is incorrect");
        assertEquals("password", resultingType.getPassword(), "property is incorrect");
    }

    @DisplayName("Test user with zero id is saved and id generated")
    @Test
    public void test_create_happyAcceptsZeroId() throws DataViolationException {
        final User user = User.builder().username("username").password("password").id(0L).build();
        final User result = new User();
        BeanUtils.copyProperties(user, result);
        result.setId(1L);
        when(mockRepo.save(any(User.class))).thenReturn(result);

        final User resultingType = userService.create(user);

        assertEquals((Long) 1L, resultingType.getId(), "property is incorrect");
        assertEquals("username", resultingType.getUsername(), "property is incorrect");
        assertEquals("password", resultingType.getPassword(), "property is incorrect");
    }

    @DisplayName("Test user not saved through repo rejected with id")
    @Test
    public void test_create_handlesUserTransientException() throws DataViolationException {
        Assertions.assertThrows(DataViolationException.class, () -> {
            final User user = User.builder().username("username").password("password").id(3L).build();
            userService.create(user);
        });
    }

    @DisplayName("Test user can be retrieved by id")
    @Test
    public void test_read_byId_happy() throws DataViolationException, NotFoundException {
        final Long id = 3L;
        final User user = User.builder().username("username").password("password").id(id).build();
        final Optional<User> optional = Optional.of(user);
        when(mockRepo.findById(3L)).thenReturn(optional);

        final User resultingType = userService.read(id);

        assertEquals(id, resultingType.getId(), "property is incorrect");
        assertEquals("username", resultingType.getUsername(), "property is incorrect");
        assertEquals("password", resultingType.getPassword(), "property is incorrect");
    }

    @DisplayName("Test user can be retrieved by id")
    @Test
    public void test_read_byId_handlesNotFound() throws NotFoundException {
        Assertions.assertThrows(NotFoundException.class, () -> {
            final Optional<User> optional = Optional.empty();
            when(mockRepo.findById(3L)).thenReturn(optional);
            userService.read(3L);
        });
    }

    @DisplayName("Test user can be retrieved page")
    @Test
    public void test_read_pagination_happy() {
        final PageRequest request = PageRequest.of(0, 10);
        final List<User> typeList = new ArrayList<>();
        typeList.add(User.builder().username("username").password("password").id(1L).build());
        final Page<User> typePage = new PageImpl<>(typeList);
        when(mockRepo.findAll(request)).thenReturn(typePage);

        final Page<User> resultPage = userService.read(request);

        assertEquals(1, resultPage.getNumberOfElements(), "property is incorrect");
        assertEquals((Long) 1L, resultPage.getContent().get(0).getId(), "property is incorrect");
    }

    @DisplayName("Test user can be updated")
    @Test
    public void test_update_happy() throws NotFoundException {
        final Long id = 3L;
        final User user = User.builder().username("username").password("password").id(id).build();
        final User dbUser = new User();
        BeanUtils.copyProperties(user, dbUser);
        final Optional<User> dbOptional = Optional.of(dbUser);

        when(mockRepo.findById(id)).thenReturn(dbOptional);
        when(mockRepo.save(user)).thenReturn(user);

        final User resultingType = userService.update(id, user);
        assertEquals(id, resultingType.getId(), "property is incorrect");
        assertEquals("username", resultingType.getUsername(), "property is incorrect");
        assertEquals("password", resultingType.getPassword(), "property is incorrect");
    }

    @DisplayName("Test user can have password changed")
    @Test
    public void test_update_happyWithPasswordChange() throws NotFoundException {
        final Long id = 3L;
        final User user = User.builder().username("username").password("password").id(id).build();
        final User dbUser = new User();
        BeanUtils.copyProperties(user, dbUser);
        final Optional<User> dbOptional = Optional.of(dbUser);
        user.setPassword("password2");

        when(mockRepo.findById(id)).thenReturn(dbOptional);
        when(mockRepo.save(user)).thenReturn(user);

        final User resultingType = userService.update(id, user);
        assertEquals(id, resultingType.getId(), "property is incorrect");
        assertEquals("username", resultingType.getUsername(), "property is incorrect");
        assertEquals("password2", resultingType.getPassword(), "property is incorrect");
    }

    @DisplayName("Test user password can be reset to empty")
    @Test
    public void test_update_happyWithPasswordChangeToEmpty() throws NotFoundException {
        // need to figure something out with password reset for this
        final Long id = 3L;
        final User user = User.builder().username("username").password("password").id(id).build();
        final User dbUser = new User();
        BeanUtils.copyProperties(user, dbUser);
        final Optional<User> dbOptional = Optional.of(dbUser);
        user.setPassword("");

        when(mockRepo.findById(id)).thenReturn(dbOptional);
        when(mockRepo.save(user)).thenReturn(user);

        final User resultingType = userService.update(id, user);
        assertEquals(id, resultingType.getId(), "property is incorrect");
        assertEquals("username", resultingType.getUsername(), "property is incorrect");
        assertEquals("", resultingType.getPassword(), "property is incorrect");
    }

    @DisplayName("Test user update handles exception")
    @Test
    public void test_update_handlesNotFound() throws NotFoundException {
        Assertions.assertThrows(NotFoundException.class, () -> {
            final Long id = 3L;
            final Optional<User> dbOptional = Optional.empty();
            when(mockRepo.findById(anyLong())).thenReturn(dbOptional);

            userService.update(id, new User());
        });
    }

    @DisplayName("Test user can be deleted")
    @Test
    public void test_delete_happy() {
        when(mockRepo.existsById(anyLong())).thenReturn(true);

        try {
            userService.delete(1L);
        } catch (final Exception e) {
            fail(e.getMessage());
        }
        verify(mockRepo, times(1)).deleteById(anyLong());
    }

    @DisplayName("Test unknown user id can be deleted")
    @Test
    public void test_delete_handlesIdNotFound() {
        when(mockRepo.existsById(anyLong())).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.delete(1L);
        });
    }

}
