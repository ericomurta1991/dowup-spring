package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDowUp {
	private String fileName;
	private String fileDownloadUri;
	private String fileType;
	private Long size;
	
}
