package com.mananluvtocode.jobportal.util;

import org.aspectj.apache.bcel.util.ClassPath;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class FileSaveUtil {
    public static void addPhoto(String uploaddirectory, String fileName, MultipartFile multipartFile) throws IOException {
        File file = new ClassPathResource("static").getFile();
        Path path = Paths.get(file.getAbsolutePath() + File.separator + uploaddirectory);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        path = Paths.get(file.getAbsolutePath() + File.separator + uploaddirectory + File.separator + multipartFile.getOriginalFilename());
        Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
    }
}