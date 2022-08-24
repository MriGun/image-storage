package com.mritech.imagestorage.service;

import com.mritech.imagestorage.entity.ImageData;
import com.mritech.imagestorage.repo.StorageRepo;
import com.mritech.imagestorage.util.ImageUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class StorageService {

    private final StorageRepo storageRepo;

    public StorageService(StorageRepo storageRepo) {
        this.storageRepo = storageRepo;
    }

    public String uploadImage(MultipartFile file)  {
        ImageData imageData = null;
        try {
            imageData = storageRepo.save(ImageData.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .imageData(ImageUtils.compressImage(file.getBytes()))
                    .build()
            );
        }
        catch (IOException ex) {
            return "Exception occured : " + ex.getMessage();
        }


        if (imageData != null) {
            return "file uploaded successfully : " + file.getOriginalFilename();
        }
        return null;
    }

    public byte[] downloadImage(String fileName) {
        Optional<ImageData> imageData = storageRepo.findByName(fileName);
        byte[] imageBytes = ImageUtils.decompressImage(imageData.get().getImageData());
        return imageBytes;

    }
}
