package vn.dkc.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.dkc.jobhunter.service.FileService;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/${dkc.application.version}")
public class FileController{
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Value("${dkc.upload-file.base-uri}")
    private String baseURI;

    @PostMapping("/files")
    public ResponseEntity<Void> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folder") String folder
    ) throws URISyntaxException, IOException {
        //Skip validate

        //Create directory
        this.fileService.createDirectory(baseURI+folder);

        //Save file
        this.fileService.storeFile(file, folder);

        return ResponseEntity.ok(null);
    }
}
