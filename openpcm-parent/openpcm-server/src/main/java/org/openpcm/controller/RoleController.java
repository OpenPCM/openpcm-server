package org.openpcm.controller;

import java.util.List;

import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.Role;
import org.openpcm.service.RoleService;
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
@Api(value = "/api/v1/role")
@RequestMapping("/api/v1/role")
public class RoleController extends BaseController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "createRole", response = Role.class)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public @ResponseBody Role createRole(@RequestBody Role role) throws DataViolationException {
        return roleService.create(role);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "readRoles", response = Role.class, responseContainer = "List")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody List<Role> readRoles() {
        return roleService.readAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "updateRole", response = Role.class)
    @PutMapping(value = "/{id}")
    public @ResponseBody Role readRole(@PathVariable Long id, @RequestBody Role role) throws NotFoundException {
        return roleService.update(id, role);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "readRole", response = Role.class)
    @GetMapping(value = "/{id}")
    public @ResponseBody Role readRole(@PathVariable Long id) throws NotFoundException {
        return roleService.read(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "deleteRole")
    public void deleteRole(@PathVariable Long id) {
        roleService.delete(id);
    }
}
