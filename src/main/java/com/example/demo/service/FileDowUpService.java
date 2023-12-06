package com.example.demo.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;

import com.example.demo.config.FileDowUpProperties;
import com.example.demo.exception.FileDowUpException;
import com.example.demo.exception.MyFileNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileDowUpService {
	private final Path fileStorageLocation;
	
	public FileDowUpService(FileDowUpProperties fileDowUpProperties) {
		this.fileStorageLocation = Paths.get(fileDowUpProperties.getUploadDir())
				.toAbsolutePath().normalize();
		
		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch(Exception ex) {
			throw new FileDowUpException("Could not create the directory where the upload files will be stored.", ex);
		}
		
	}
	
	public String storeFile(MultipartFile file) {
		//normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		
		try {
			// check if this file's name contains invalid characters
			if(fileName.contains("...")) {
				throw new FileDowUpException("Sorry! Filename contains invalid path sequence" + fileName);
			}
			
			//Copy file to the target location (replacing existing file with same name)
			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			return fileName;
		} catch(IOException ex) {
			throw new FileDowUpException("Could not store file" + fileName + ". Please try again!", ex);
		}
	}
	
	public Resource loadFileAsResource(String fileName) {
		try {
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			 Resource resource = new UrlResource(filePath.toUri());
			 if(resource.exists()) {
				 return resource;
			 }  else {
				 throw new MyFileNotFoundException("File not found" + fileName);
			 }
		} catch(MalformedURLException ex) {
			throw new MyFileNotFoundException("File not found" + fileName, ex);
		}
		
	}
	
	public String getContentType(HttpServletRequest request, Resource resource) {
		//try to determine file's content type
		String contetType = null;
		try {
			contetType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch(IOException ex) {
			log.info("Could not determine file type");
		}
		
		
		//fallback to the default content type if type could not be determined
		if(contetType == null) {
			contetType = "application/octet-stream";
		}
		return contetType;
	}	
	
	
}
