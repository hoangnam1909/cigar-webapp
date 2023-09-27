package com.nhn.cigarwebapp.service.impl;

import com.cloudinary.Cloudinary;
import com.nhn.cigarwebapp.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        try {
            return cloudinary.uploader()
                    .upload(file.getBytes(),
                            Map.of("public_id", UUID.randomUUID().toString()))
                    .get("url")
                    .toString();
        } catch (IOException ioException) {
            throw new IOException("Upload fail");
        }
    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> files) {
        List<String> result = new ArrayList<>();

        files.forEach(file -> {
            try {
                result.add(cloudinary.uploader()
                        .upload(file.getBytes(),
                                Map.of("public_id", UUID.randomUUID().toString()))
                        .get("url")
                        .toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return result;
    }

}
