package org.openpcm.controller;

import java.util.List;

import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.User;
import org.openpcm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "/api/v1/user")
@RequestMapping("/api/v1/user")
public class UserController extends BaseController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "createUser", response = User.class)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public @ResponseBody User createUser(@RequestBody User user) throws DataViolationException {
        return userService.create(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "readUsers", response = User.class, responseContainer = "List")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody List<User> readUsers() {
        return userService.readAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "updateUser", response = User.class)
    @PutMapping(value = "/{id}")
    public @ResponseBody User readUser(@PathVariable Long id, @RequestBody User user) throws NotFoundException {
        return userService.update(id, user);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "readUser", response = User.class)
    @GetMapping(value = "/{id}")
    public @ResponseBody User readUser(@PathVariable Long id) throws NotFoundException {
        return userService.read(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "deleteUser")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}
