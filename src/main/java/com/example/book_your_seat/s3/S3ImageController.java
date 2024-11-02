package com.example.book_your_seat.s3;

import com.example.book_your_seat.s3.dto.UploadRequest;
import com.example.book_your_seat.s3.dto.UploadResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class S3ImageController {

    private final S3ImageService s3ImageService;

    @PostMapping
    public ResponseEntity<UploadResponse> uploadFile(@ModelAttribute UploadRequest uploadRequest) throws IOException {
        return ResponseEntity.ok(
                s3ImageService.upload(uploadRequest.files())
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteImage(@RequestParam("fileUrl") String fileUrl) {
        s3ImageService.delete(fileUrl);
        return ResponseEntity.ok(null);
    }
}
