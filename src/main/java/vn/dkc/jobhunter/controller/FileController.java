package vn.dkc.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.dkc.jobhunter.domain.response.File.ResFileUploadDTO;
import vn.dkc.jobhunter.service.FileService;
import vn.dkc.jobhunter.util.annotation.ApiMessage;
import vn.dkc.jobhunter.util.error.StorageException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;

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
    @ApiMessage("Upload single file")
    public ResponseEntity<ResFileUploadDTO> uploadFile(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder
    ) throws URISyntaxException, IOException {
        //Check file
        if(file == null || file.isEmpty()){
            throw new StorageException("File is empty. Please upload a file.");
        }

        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = List.of("jpg", "png", "jpeg", "pdf", "docx", "doc");

        boolean isValid = allowedExtensions.stream()
                .anyMatch(extension -> fileName != null && fileName.toLowerCase().endsWith(extension));

        if(!isValid){
            throw new StorageException("File type is not supported. Please upload a valid file: " + String.join(", ", allowedExtensions));
        }

        //Create directory
        this.fileService.createDirectory(baseURI+folder);

        //Save file
        String uploadFile = this.fileService.storeFile(file, folder);

        ResFileUploadDTO resFileUploadDTO = new ResFileUploadDTO(uploadFile, Instant.now());

        return ResponseEntity.ok(resFileUploadDTO);
    }
}
