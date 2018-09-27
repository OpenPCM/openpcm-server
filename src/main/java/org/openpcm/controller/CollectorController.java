package org.openpcm.controller;

import java.util.List;

import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.Collector;
import org.openpcm.service.CollectorService;
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
public class CollectorController extends BaseController {

    private final CollectorService service;

    @Autowired
    public CollectorController(CollectorService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "createCollector", response = Collector.class)
    @PostMapping(value = "collector")
    @ApiResponses({ @ApiResponse(code = 201, response = Collector.class, message = "created collector") })
    public @ResponseBody Collector createCollector(
                    @ApiParam(value = "collector to create", name = "collector", required = true) @RequestBody Collector collector)
                    throws DataViolationException {
        return service.create(collector);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "readCollectors", response = Collector.class, responseContainer = "List")
    @GetMapping(value = "collector")
    @ApiResponses({ @ApiResponse(code = 200, response = Collector.class, message = "read collectors", responseContainer = "List") })
    public @ResponseBody Page<Collector> readCollectors(Pageable pageable) {
        return service.read(pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "updateCollector", response = Collector.class)
    @PutMapping(value = "collector/{id}")
    @ApiResponses({ @ApiResponse(code = 200, response = Collector.class, message = "updated collector") })
    public @ResponseBody Collector readCollector(@ApiParam(value = "id of collector", name = "id", required = true) @PathVariable Long id,
                    @ApiParam(value = "collector to update", name = "collector", required = true) @RequestBody Collector collector) throws NotFoundException {
        return service.update(id, collector);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "readCollector", response = Collector.class)
    @GetMapping(value = "collector/{id}")
    @ApiResponses({ @ApiResponse(code = 200, response = Collector.class, message = "read collector") })
    public @ResponseBody Collector readCollector(@ApiParam(value = "collector to read", name = "id", required = true) @PathVariable Long id)
                    throws NotFoundException {
        return service.read(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "readCollectorByKeyAndValue", response = Collector.class)
    @GetMapping(value = "collector/{key}/{value}")
    @ApiResponses({ @ApiResponse(code = 200, response = Collector.class, responseContainer = "list", message = "read collector by key and value") })
    public @ResponseBody List<Collector> readCollectorByKeyAndValue(
                    @ApiParam(value = "key of attribute of collector", name = "key", required = true) @PathVariable String key,
                    @ApiParam(value = "value ofattribute of collector", name = "value", required = true) @PathVariable String value) throws NotFoundException {
        return service.readByKeyandValue(key, value);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "deleteCollector")
    @DeleteMapping(value = "collector/{id}")
    public void deleteCollector(@ApiParam(value = "collector to delete", name = "id", required = true) @PathVariable Long id) {
        service.delete(id);
    }
}
