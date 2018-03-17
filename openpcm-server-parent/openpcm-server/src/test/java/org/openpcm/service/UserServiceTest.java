package org.openpcm.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(mockEncoder.encode(anyString())).thenAnswer(new Answer<String>() {

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return (String) invocation.getArguments()[0];
            }
        });
    }

    @Test
    public void test_create_happy() throws DataViolationException {
        User user = User.builder().username("username").build();
        User result = new User();
        BeanUtils.copyProperties(user, result);
        result.setId(1L);
        when(mockRepo.save(any(User.class))).thenReturn(result);

        User resultingType = userService.create(user);

        assertEquals("property is incorrect", (Long) 1L, resultingType.getId());
        assertEquals("property is incorrect", "username", resultingType.getUsername());
    }

    @Test
    public void test_create_happyWithAddress() throws DataViolationException {
        Address address = Address.builder().addressLineOne("123 Main Street").city("City").state("CA").zipCode("32802").build();
        User user = User.builder().username("username").password("password").address(address).build();
        User result = new User();
        BeanUtils.copyProperties(user, result);
        result.setId(1L);
        when(mockRepo.save(any(User.class))).thenReturn(result);

        User resultingType = userService.create(user);

        assertEquals("property is incorrect", (Long) 1L, resultingType.getId());
        assertEquals("property is incorrect", "username", resultingType.getUsername());
        assertEquals("property is incorrect", "password", resultingType.getPassword());
    }

    @Test
    public void test_create_happyWithAddressWithZeroId() throws DataViolationException {
        Address address = Address.builder().addressLineOne("123 Main Street").city("City").state("CA").zipCode("32802").id(0L).build();
        User user = User.builder().username("username").password("password").address(address).build();
        User result = new User();
        BeanUtils.copyProperties(user, result);
        result.setId(1L);
        when(mockRepo.save(any(User.class))).thenReturn(result);

        User resultingType = userService.create(user);

        assertEquals("property is incorrect", (Long) 1L, resultingType.getId());
        assertEquals("property is incorrect", "username", resultingType.getUsername());
        assertEquals("property is incorrect", "password", resultingType.getPassword());
    }

    @Test
    public void test_create_happyAcceptsZeroId() throws DataViolationException {
        User user = User.builder().username("username").password("password").id(0L).build();
        User result = new User();
        BeanUtils.copyProperties(user, result);
        result.setId(1L);
        when(mockRepo.save(any(User.class))).thenReturn(result);

        User resultingType = userService.create(user);

        assertEquals("property is incorrect", (Long) 1L, resultingType.getId());
        assertEquals("property is incorrect", "username", resultingType.getUsername());
        assertEquals("property is incorrect", "password", resultingType.getPassword());
    }

    @Test(expected = DataViolationException.class)
    public void test_create_handlesUserTransientException() throws DataViolationException {
        User user = User.builder().username("username").password("password").id(3L).build();

        userService.create(user);
    }

    @Test(expected = DataViolationException.class)
    public void test_create_handlesAddressTransientException() throws DataViolationException {
        Address address = Address.builder().addressLineOne("123 Main Street").city("City").state("CA").zipCode("32802").id(1L).build();
        User user = User.builder().username("username").password("password").address(address).build();

        userService.create(user);
    }

    @Test
    public void test_read_byId_happy() throws DataViolationException, NotFoundException {
        Long id = 3L;
        User user = User.builder().username("username").password("password").id(id).build();
        Optional<User> optional = Optional.of(user);
        when(mockRepo.findById(3L)).thenReturn(optional);

        User resultingType = userService.read(id);

        assertEquals("property is incorrect", id, resultingType.getId());
        assertEquals("property is incorrect", "username", resultingType.getUsername());
        assertEquals("property is incorrect", "password", resultingType.getPassword());
    }

    @Test(expected = NotFoundException.class)
    public void test_read_byId_handlesNotFound() throws NotFoundException {
        Optional<User> optional = Optional.empty();
        when(mockRepo.findById(3L)).thenReturn(optional);
        userService.read((Long) 3L);
    }

    @Test
    public void test_read_pagination_happy() {
        PageRequest request = PageRequest.of(0, 10);
        List<User> typeList = new ArrayList<>();
        typeList.add(User.builder().username("username").password("password").id(1L).build());
        Page<User> typePage = new PageImpl<>(typeList);
        when(mockRepo.findAll(request)).thenReturn(typePage);

        Page<User> resultPage = userService.read(request);

        assertEquals("property is incorrect", 1, resultPage.getNumberOfElements());
        assertEquals("property is incorrect", (Long) 1L, resultPage.getContent().get(0).getId());
    }

    @Test
    public void test_update_happy() throws NotFoundException {
        Long id = 3L;
        User user = User.builder().username("username").password("password").id(id).build();
        User dbUser = new User();
        BeanUtils.copyProperties(user, dbUser);
        Optional<User> dbOptional = Optional.of(dbUser);

        when(mockRepo.findById(id)).thenReturn(dbOptional);
        when(mockRepo.save(user)).thenReturn(user);

        User resultingType = userService.update(id, user);
        assertEquals("property is incorrect", id, resultingType.getId());
        assertEquals("property is incorrect", "username", resultingType.getUsername());
        assertEquals("property is incorrect", "password", resultingType.getPassword());
    }

    @Test
    public void test_update_happyWithPasswordChange() throws NotFoundException {
        Long id = 3L;
        User user = User.builder().username("username").password("password").id(id).build();
        User dbUser = new User();
        BeanUtils.copyProperties(user, dbUser);
        Optional<User> dbOptional = Optional.of(dbUser);
        user.setPassword("password2");

        when(mockRepo.findById(id)).thenReturn(dbOptional);
        when(mockRepo.save(user)).thenReturn(user);

        User resultingType = userService.update(id, user);
        assertEquals("property is incorrect", id, resultingType.getId());
        assertEquals("property is incorrect", "username", resultingType.getUsername());
        assertEquals("property is incorrect", "password2", resultingType.getPassword());
    }

    @Test
    public void test_update_happyWithPasswordChangeToEmpty() throws NotFoundException {
        // need to figure something out with password reset for this
        Long id = 3L;
        User user = User.builder().username("username").password("password").id(id).build();
        User dbUser = new User();
        BeanUtils.copyProperties(user, dbUser);
        Optional<User> dbOptional = Optional.of(dbUser);
        user.setPassword("");

        when(mockRepo.findById(id)).thenReturn(dbOptional);
        when(mockRepo.save(user)).thenReturn(user);

        User resultingType = userService.update(id, user);
        assertEquals("property is incorrect", id, resultingType.getId());
        assertEquals("property is incorrect", "username", resultingType.getUsername());
        assertEquals("property is incorrect", "", resultingType.getPassword());
    }

    @Test(expected = NotFoundException.class)
    public void test_update_handlesNotFound() throws NotFoundException {
        Long id = 3L;
        Optional<User> dbOptional = Optional.empty();
        when(mockRepo.findById(anyLong())).thenReturn(dbOptional);

        userService.update(id, new User());
    }

    @Test
    public void test_delete_happy() {
        when(mockRepo.existsById(anyLong())).thenReturn(true);

        try {
            userService.delete(1L);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        verify(mockRepo, times(1)).deleteById(anyLong());
    }

    @Test
    public void test_delete_handlesIdNotFound() {
        when(mockRepo.existsById(anyLong())).thenReturn(false);

        try {
            userService.delete(1L);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        verify(mockRepo, times(0)).deleteById(anyLong());
    }

}
