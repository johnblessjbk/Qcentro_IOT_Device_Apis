package com.auth.service.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
	private String status;
	private String message;
	private T data;
	// private PageMetadata metadata;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime timestamp = LocalDateTime.now();

	public ApiResponse(String status, String message, T data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}

//    
//
//    @Data
//    @AllArgsConstructor
//    public static class PageMetadata {
//        private int page;
//        private int size;
//        private long totalElements;
//        private int totalPages;
//    }
}