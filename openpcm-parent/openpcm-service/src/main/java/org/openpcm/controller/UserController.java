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
@Api(value = "/api/v1/patient")
@RequestMapping("/api/v1/patient")
public class UserController extends BaseController {

    private final UserService patientService;

    @Autowired
    public UserController(UserService patientService) {
        this.patientService = patientService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "createPatient", response = User.class)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public @ResponseBody User createPatient(@RequestBody User patient) throws DataViolationException {
        return patientService.create(patient);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "readPatients", response = User.class, responseContainer = "List")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody List<User> readPatients() {
        return patientService.readAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "updatePatient", response = User.class)
    @PutMapping(value = "/{id}")
    public @ResponseBody User readPatient(@PathVariable Long id, @RequestBody User patient) throws NotFoundException {
        return patientService.update(id, patient);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "readPatient", response = User.class)
    @GetMapping(value = "/{id}")
    public @ResponseBody User readPatient(@PathVariable Long id) throws NotFoundException {
        return patientService.read(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "deletePatient")
    public void deletePatient(@PathVariable Long id) {
        patientService.delete(id);
    }
}
