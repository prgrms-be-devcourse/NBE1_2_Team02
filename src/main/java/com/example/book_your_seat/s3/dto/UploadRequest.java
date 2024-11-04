package com.example.book_your_seat.s3.dto;

import org.springframework.web.multipart.MultipartFile;

public record UploadRequest(
        MultipartFile files
) {
}
