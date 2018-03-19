package org.openpcm.controller;

import java.io.IOException;

import org.openpcm.exceptions.FileEmptyException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "/api/v1/")
@RequestMapping("/api/v1/")
public class FileStorageController extends BaseController {

	@Autowired
	private FileStorageService service;

	@ApiOperation(value = "uploadFile")
	@PostMapping(value = "upload")
	@ApiResponses({ @ApiResponse(code = 200, message = "successfully uploaded file") })
	public void uploadFile(
			@ApiParam(value = "file to upload", name = "file", required = true) @RequestParam("file") MultipartFile file)
			throws FileEmptyException, IOException {
		service.storeFile(file);
	}

	@ApiOperation(value = "downloadFile")
	@PostMapping(value = "download/{filename}")
	@ApiResponses({ @ApiResponse(code = 200, message = "successfully downloaded file") })
	public ResponseEntity<Resource> downloadFile(
			@ApiParam(value = "filename to download", name = "filename", required = true) @PathVariable("filename") String filename)
			throws FileEmptyException, IOException, NotFoundException {
		Resource file = service.loadAsResource(filename);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

}
