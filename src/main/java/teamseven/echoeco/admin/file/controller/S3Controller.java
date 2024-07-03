package teamseven.echoeco.admin.file.controller;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import teamseven.echoeco.config.ApiResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static teamseven.echoeco.config.aws.S3LocalConfig.BUCKET_NAME;

@RequiredArgsConstructor
@RequestMapping("/file")
@RestController
public class S3Controller {
    private final AmazonS3 amazonS3;

    @GetMapping("/download")
    public ResponseEntity<byte[]> get(@RequestParam String fileName) throws IOException {
        S3Object s3Object = amazonS3.getObject(BUCKET_NAME, fileName);
        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectInputStream);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ApiResponse<String> upload(@RequestParam MultipartFile file) {
        File uploadFile = convert(file);
        String url = uploadToS3(uploadFile);
        return ApiResponse.success(url);
    }

    private File convert(MultipartFile multipartFile) {
        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return file;
    }

    private String uploadToS3(File uploadFile) {
        try {
            amazonS3.putObject(BUCKET_NAME, uploadFile.getName(), uploadFile);
            return amazonS3.getUrl(BUCKET_NAME, uploadFile.getName()).toString();
        } catch (SdkClientException e) {
            throw e;
        } finally {
            removeNewFile(uploadFile);
        }
    }

    private void removeNewFile(File uploadFile) {
        uploadFile.delete();
    }
}
