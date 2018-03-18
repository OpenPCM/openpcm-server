package org.openpcm.controller;

import javax.validation.constraints.NotNull;

import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.ParameterType;
import org.openpcm.service.ParameterTypeService;
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
public class ParameterTypeController extends BaseController {

	private final ParameterTypeService service;

	@Autowired
	public ParameterTypeController(ParameterTypeService service) {
		this.service = service;
	}

	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "createParameterType", response = ParameterType.class)
	@PostMapping(value = "parameterType")
	@ApiResponses({ @ApiResponse(code = 201, response = ParameterType.class, message = "created parameterType") })
	public @ResponseBody ParameterType createParameterType(
			@ApiParam(value = "parameterType to create", name = "parameterType", required = true) @RequestBody ParameterType parameterType)
			throws DataViolationException {
		return service.create(parameterType);
	}

	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "readParameterTypes", response = ParameterType.class, responseContainer = "List")
	@GetMapping(value = "parameterType")
	@ApiResponses({
			@ApiResponse(code = 200, response = ParameterType.class, message = "read parameterTypes", responseContainer = "List") })
	public @ResponseBody Page<ParameterType> readParameterTypes(@NotNull final Pageable pageable) {
		return service.read(pageable);
	}

	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "readParameterType", response = ParameterType.class)
	@GetMapping(value = "parameterType/{id}")
	@ApiResponses({ @ApiResponse(code = 200, response = ParameterType.class, message = "read parameterType") })
	public @ResponseBody ParameterType readParameterType(
			@ApiParam(value = "parameterType to read", name = "id", required = true) @PathVariable Long id)
			throws NotFoundException {
		return service.read(id);
	}

	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "updateParameterType", response = ParameterType.class)
	@PutMapping(value = "parameterType/{id}")
	@ApiResponses({ @ApiResponse(code = 200, response = ParameterType.class, message = "updated parameterType") })
	public @ResponseBody ParameterType updateParameterType(
			@ApiParam(value = "id of parameterType", name = "id", required = true) @PathVariable Long id,
			@ApiParam(value = "parameterType to update", name = "parameterType", required = true) @RequestBody ParameterType parameterType)
			throws NotFoundException {
		return service.update(id, parameterType);
	}

	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "deleteParameterType")
	@DeleteMapping(value = "parameterType/{id}")
	public void deleteRole(
			@ApiParam(value = "parameterType to delete", name = "id", required = true) @PathVariable Long id) {
		service.delete(id);
	}
}
