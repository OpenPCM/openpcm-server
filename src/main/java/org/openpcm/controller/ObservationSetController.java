package org.openpcm.controller;

import javax.validation.constraints.NotNull;

import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.ObservationSet;
import org.openpcm.model.ParameterType;
import org.openpcm.service.ObservationSetService;
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
public class ObservationSetController extends BaseController {

    private final ObservationSetService service;

    @Autowired
    public ObservationSetController(ObservationSetService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "createObservationSet", response = ObservationSet.class)
    @PostMapping(value = "observationset")
    @ApiResponses({ @ApiResponse(code = 201, response = ParameterType.class, message = "created observationSet") })
    public @ResponseBody ObservationSet createObservationSet(
                    @ApiParam(value = "observationSet to create", name = "observationSet", required = true) @RequestBody ObservationSet observationSet)
                    throws DataViolationException {
        return service.create(observationSet);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "readObservationSets", response = ObservationSet.class, responseContainer = "List")
    @GetMapping(value = "observationset")
    @ApiResponses({ @ApiResponse(code = 200, response = ObservationSet.class, message = "read observationSets", responseContainer = "List") })
    public @ResponseBody Page<ObservationSet> readObservationSets(@NotNull final Pageable pageable) {
        return service.read(pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "readObsevationSet", response = ObservationSet.class)
    @GetMapping(value = "observationSet/{id}")
    @ApiResponses({ @ApiResponse(code = 200, response = ObservationSet.class, message = "read observationSet") })
    public @ResponseBody ObservationSet readObservationSet(@ApiParam(value = "observationSet to read", name = "id", required = true) @PathVariable Long id)
                    throws NotFoundException {
        return service.read(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "updateObservationSet", response = ObservationSet.class)
    @PutMapping(value = "observationset/{id}")
    @ApiResponses({ @ApiResponse(code = 200, response = ObservationSet.class, message = "updated observationSet") })
    public @ResponseBody ObservationSet updateObservationSet(@ApiParam(value = "id of observationSet", name = "id", required = true) @PathVariable Long id,
                    @ApiParam(value = "observationSet to update", name = "observationSet", required = true) @RequestBody ObservationSet observationSet)
                    throws NotFoundException {
        return service.update(id, observationSet);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "deleteObservationSet")
    @DeleteMapping(value = "observationset/{id}")
    public void deleteObservationSet(@ApiParam(value = "observationSet to delete", name = "id", required = true) @PathVariable Long id)
                    throws NotFoundException {
        service.delete(id);
    }
}
