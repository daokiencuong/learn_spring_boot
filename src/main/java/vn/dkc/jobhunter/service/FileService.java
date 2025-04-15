package vn.dkc.jobhunter.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {
    @Value("${dkc.upload-file.base-uri}")
    private String baseURI;

    public void createDirectory(String folder) throws URISyntaxException {
        URI uri = new URI(folder);
        Path path = Paths.get(uri);
        File tmpDir = new File(path.toString());
        if(!tmpDir.isDirectory()){
            try{
                Files.createDirectory(tmpDir.toPath());
                System.out.println("Directory created: " + tmpDir.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("Failed to create directory");
                e.printStackTrace();
            }
        } else {
            System.out.println("Directory already exists");
        }
    }

    public void storeFile(MultipartFile file, String folder) throws URISyntaxException, IOException {
        String finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        String pathFile = baseURI+folder+"/"+finalName;

        URI uri = new URI(pathFile.replace(" ", "%20"));
        Path path = Paths.get(uri);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File saved: " + path.toString());
        }
    }
}
