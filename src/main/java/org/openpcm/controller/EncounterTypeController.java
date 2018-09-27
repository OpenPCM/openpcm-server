package org.openpcm.controller;

import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.EncounterType;
import org.openpcm.service.EncounterTypeService;
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
public class EncounterTypeController extends BaseController {

    private final EncounterTypeService service;

    @Autowired
    public EncounterTypeController(EncounterTypeService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "createEncounterType", response = EncounterType.class)
    @PostMapping(value = "encountertype")
    @ApiResponses({ @ApiResponse(code = 201, response = EncounterType.class, message = "created encounterType") })
    public @ResponseBody EncounterType createEncounterType(
                    @ApiParam(value = "encounterType to create", name = "encounterType", required = true) @RequestBody EncounterType encounterType)
                    throws DataViolationException {
        return service.create(encounterType);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "readEncounterTypes", response = EncounterType.class, responseContainer = "List")
    @GetMapping(value = "encountertype")
    @ApiResponses({ @ApiResponse(code = 200, response = EncounterType.class, message = "read encounterTypes", responseContainer = "List") })
    public @ResponseBody Page<EncounterType> readEncounterTypes(Pageable pageable) {
        return service.read(pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "updateEncounterType", response = EncounterType.class)
    @PutMapping(value = "encountertype/{id}")
    @ApiResponses({ @ApiResponse(code = 200, response = EncounterType.class, message = "updated encounterType") })
    public @ResponseBody EncounterType readEncounterType(@ApiParam(value = "id of encounterType", name = "id", required = true) @PathVariable Long id,
                    @ApiParam(value = "encounterType to update", name = "encounterType", required = true) @RequestBody EncounterType encounterType)
                    throws NotFoundException {
        return service.update(id, encounterType);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "readEncounterType", response = EncounterType.class)
    @GetMapping(value = "encountertype/{id}")
    @ApiResponses({ @ApiResponse(code = 200, response = EncounterType.class, message = "read encounterType") })
    public @ResponseBody EncounterType readEncounterType(@ApiParam(value = "encounterType to read", name = "id", required = true) @PathVariable Long id)
                    throws NotFoundException {
        return service.read(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "deleteEncounterType")
    @DeleteMapping(value = "encountertype/{id}")
    public void deleteEncounterType(@ApiParam(value = "encounterType to delete", name = "id", required = true) @PathVariable Long id) {
        service.delete(id);
    }
}
