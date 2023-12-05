package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;


@Component
@Getter
public class FileDowUpProperties {
	
	@Value("${file.uploadDir}")
	private String uploadDir;
	
}
