package com.example.demo.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.entity.FileDowUp;
import com.example.demo.service.FileDowUpService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/files")
@AllArgsConstructor
public class FileDowUpController {
	
	private FileDowUpService fileDowUpService;
	
	@PostMapping("/uploadFile")
	public FileDowUp fileDowUp(@RequestPart("file") MultipartFile file ) {
		String fileName = fileDowUpService.storeFile(file);
		
		String fileDowLoadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/Documentos/downloadFile/")
				.path(fileName)
				.toUriString();
		
		return new FileDowUp(fileName,fileDowLoadUri, file.getContentType(), file.getSize());
	}
	
	@GetMapping("/downloadFile/{fileName}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request){
		//Load File as Resource
		Resource resource = fileDowUpService.loadFileAsResource(fileName);
		
		//try to determine file's content type
		String contentType = fileDowUpService.getContentType(request, resource);
		
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
		
		
	}
	
}
