package org.openpcm.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.openpcm.exceptions.FileEmptyException;
import org.openpcm.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageService.class);

	private final String fileStorage;

	private final Path rootLocation;

	public FileStorageService(@Value("${openpcm.fileStorage:./target/}") String fileStorage) {
		this.fileStorage = fileStorage;
		this.rootLocation = Paths.get(fileStorage);
	}

	public void storeFile(MultipartFile file) throws FileEmptyException, IOException {
		if (file.isEmpty()) {
			throw new FileEmptyException("file was empty");
		}

		byte[] bytes = file.getBytes();
		Path path = Paths.get(fileStorage + file.getOriginalFilename());
		Files.write(path, bytes);

		LOGGER.debug("Successfully uploaded: {}. {} bytes", file.getOriginalFilename(), file.getSize());
	}

	public Resource loadAsResource(String filename) throws NotFoundException {
		try {
			Path file = rootLocation.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new NotFoundException("Could not read file: " + filename);

			}
		} catch (MalformedURLException e) {
			throw new NotFoundException("Could not read file: " + filename, e);
		}
	}

}
