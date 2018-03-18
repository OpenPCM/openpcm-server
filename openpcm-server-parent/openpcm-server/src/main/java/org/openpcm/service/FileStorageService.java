package org.openpcm.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.openpcm.exceptions.FileEmptyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageService.class);

	@Value("${openpcm.fileStorage:./target/}")
	private String fileStorage;

	public void storeFile(MultipartFile file) throws FileEmptyException, IOException {
		if (file.isEmpty()) {
			throw new FileEmptyException("file was empty");
		}

		byte[] bytes = file.getBytes();
		Path path = Paths.get(fileStorage + file.getOriginalFilename());
		Files.write(path, bytes);

		LOGGER.debug("Successfully uploaded: {}. {} bytes", file.getOriginalFilename(), file.getSize());

	}

}
