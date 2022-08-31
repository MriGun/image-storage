package com.mritech.imagestorage.repo;

import com.mritech.imagestorage.entity.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileDataRepo extends JpaRepository<FileData, Long> {

    Optional<FileData> findByName(String fileName);
}
