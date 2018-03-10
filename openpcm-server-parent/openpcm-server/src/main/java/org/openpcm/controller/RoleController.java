package org.openpcm.controller;

import java.util.List;

import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.Role;
import org.openpcm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RoleController extends BaseController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "createRole", response = Role.class)
    @PostMapping(value = "role")
    @ApiResponses({ @ApiResponse(code = 201, response = Role.class, message = "created role") })
    public @ResponseBody Role createRole(@ApiParam(value = "role to create", name = "role", required = true) @RequestBody Role role)
                    throws DataViolationException {
        return roleService.create(role);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "readRoles", response = Role.class, responseContainer = "List")
    @GetMapping(value = "role")
    @ApiResponses({ @ApiResponse(code = 200, response = Role.class, message = "read roles", responseContainer = "List") })
    public @ResponseBody List<Role> readRoles() {
        return roleService.readAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "updateRole", response = Role.class)
    @PutMapping(value = "role/{id}")
    @ApiResponses({ @ApiResponse(code = 200, response = Role.class, message = "updated role") })
    public @ResponseBody Role readRole(@ApiParam(value = "id of role", name = "id", required = true) @PathVariable Long id,
                    @ApiParam(value = "role to update", name = "role", required = true) @RequestBody Role role) throws NotFoundException {
        return roleService.update(id, role);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "readRole", response = Role.class)
    @GetMapping(value = "role/{id}")
    @ApiResponses({ @ApiResponse(code = 200, response = Role.class, message = "read role") })
    public @ResponseBody Role readRole(@ApiParam(value = "role to read", name = "id", required = true) @PathVariable Long id) throws NotFoundException {
        return roleService.read(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "deleteRole")
    @DeleteMapping(value = "role/{id}")
    public void deleteRole(@ApiParam(value = "role to delete", name = "id", required = true) @PathVariable Long id) {
        roleService.delete(id);
    }
}
