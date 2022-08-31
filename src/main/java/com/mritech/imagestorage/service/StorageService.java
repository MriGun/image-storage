package com.mritech.imagestorage.service;

import com.mritech.imagestorage.entity.FileData;
import com.mritech.imagestorage.entity.ImageData;
import com.mritech.imagestorage.repo.FileDataRepo;
import com.mritech.imagestorage.repo.StorageRepo;
import com.mritech.imagestorage.util.ImageUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
public class StorageService {

    private final StorageRepo storageRepo;
    private final FileDataRepo fileDataRepo;
    private final String FOLDER_PATH = "D:\\tmp\\spring-files\\";

    public StorageService(StorageRepo storageRepo, FileDataRepo fileDataRepo) {
        this.storageRepo = storageRepo;
        this.fileDataRepo = fileDataRepo;
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

    public String uploadImageInFileSystem(MultipartFile file)  {

        FileData fileData = null;
        String filePath = "";
        try {
            filePath = FOLDER_PATH + file.getOriginalFilename();
            fileData = fileDataRepo.save(FileData.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .filePath(filePath)
                    .build());
            file.transferTo(new File(filePath));

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if (fileData != null) {
            return "file uploaded successfully in the file system : " + filePath;
        }
        return null;
    }

    public byte[] downloadImageFromFileSystem(String fileName) {
        Optional<FileData> fileData = fileDataRepo.findByName(fileName);
        String filePath = fileData.get().getFilePath();
        byte[] imageBytes = new byte[0];
        try {
            imageBytes = Files.readAllBytes(new File(filePath).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageBytes;

    }

}
