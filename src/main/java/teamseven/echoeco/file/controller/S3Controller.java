package teamseven.echoeco.file.controller;

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
import org.springframework.beans.factory.annotation.Value;
import teamseven.echoeco.config.ApiResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@RequiredArgsConstructor
@RequestMapping("/admin/file")
@RestController
public class S3Controller {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @GetMapping("/download")
    public ResponseEntity<byte[]> get(@RequestParam("fileName") String fileName) throws IOException {
        S3Object s3Object = amazonS3.getObject(bucket, fileName);
        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectInputStream);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ApiResponse<String> upload(@RequestParam("file") MultipartFile file) {
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
            amazonS3.putObject(bucket, uploadFile.getName(), uploadFile);
            return amazonS3.getUrl(bucket, uploadFile.getName()).toString();
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
