package org.openpcm.controller;

import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.Encounter;
import org.openpcm.service.EncounterService;
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
public class EncounterController extends BaseController {
	private final EncounterService service;

	@Autowired
	public EncounterController(EncounterService service) {
		this.service = service;
	}

	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "createEncounter", response = Encounter.class)
	@PostMapping(value = "encounter")
	@ApiResponses({ @ApiResponse(code = 201, response = Encounter.class, message = "created encounter") })
	public @ResponseBody Encounter createEncounter(
			@ApiParam(value = "encounter to create", name = "encounter", required = true) @RequestBody Encounter encounter)
			throws DataViolationException {
		return service.create(encounter);
	}

	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "readEncounters", response = Encounter.class, responseContainer = "List")
	@GetMapping(value = "encounter")
	@ApiResponses({
			@ApiResponse(code = 200, response = Encounter.class, message = "read encounters", responseContainer = "List") })
	public @ResponseBody Page<Encounter> readEncounters(Pageable pageable) {
		return service.read(pageable);
	}

	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "updateEncounter", response = Encounter.class)
	@PutMapping(value = "encounter/{id}")
	@ApiResponses({ @ApiResponse(code = 200, response = Encounter.class, message = "updated encounter") })
	public @ResponseBody Encounter readEncounter(
			@ApiParam(value = "id of encounter", name = "id", required = true) @PathVariable Long id,
			@ApiParam(value = "encounter to update", name = "encounter", required = true) @RequestBody Encounter encounter)
			throws NotFoundException {
		return service.update(id, encounter);
	}

	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "readEncounter", response = Encounter.class)
	@GetMapping(value = "encounter/{id}")
	@ApiResponses({ @ApiResponse(code = 200, response = Encounter.class, message = "read encounter") })
	public @ResponseBody Encounter readEncounter(
			@ApiParam(value = "encounter to read", name = "id", required = true) @PathVariable Long id)
			throws NotFoundException {
		return service.read(id);
	}

	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "deleteEncounter")
	@DeleteMapping(value = "encounter/{id}")
	public void deleteEncounter(
			@ApiParam(value = "encounter to delete", name = "id", required = true) @PathVariable Long id) {
		service.delete(id);
	}
}
