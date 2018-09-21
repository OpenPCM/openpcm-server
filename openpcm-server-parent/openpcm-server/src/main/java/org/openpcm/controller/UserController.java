package org.openpcm.controller;

import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.User;
import org.openpcm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "/api/v1/")
@RequestMapping("/api/v1/")
public class UserController extends BaseController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "createUser", response = User.class)
    @PostMapping(value = "user")
    @ApiResponses({ @ApiResponse(code = 201, response = User.class, message = "created user") })
    public @ResponseBody User createUser(@ApiParam(value = "user to create", name = "user", required = true) @RequestBody User user)
                    throws DataViolationException {
        return userService.create(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "readUsers", response = User.class, responseContainer = "List")
    @GetMapping(value = "user")
    @ApiResponses({ @ApiResponse(code = 200, response = User.class, message = "read users", responseContainer = "List") })
    public @ResponseBody Page<User> readUsers(Pageable pageable) {
        return userService.read(pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "updateUser", response = User.class)
    @PutMapping(value = "user/{id}")
    @ApiResponses({ @ApiResponse(code = 200, response = User.class, message = "updated user") })
    public @ResponseBody User readUser(@ApiParam(value = "user to update", name = "id", required = true) @PathVariable Long id,
                    @ApiParam(value = "user to update", name = "user", required = true) @RequestBody User user) throws NotFoundException {
        return userService.update(id, user);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "readUser", response = User.class)
    @GetMapping(value = "user/{id}")
    @ApiResponses({ @ApiResponse(code = 200, response = User.class, message = "read user") })
    public @ResponseBody User readUser(@ApiParam(value = "user to read", name = "id", required = true) @PathVariable Long id) throws NotFoundException {
        return userService.read(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "deleteUser")
    @DeleteMapping(value = "user/{id}")
    public void deleteUser(@ApiParam(value = "user to delete", name = "id", required = true) @PathVariable Long id) throws NotFoundException {
        userService.delete(id);
    }
}
