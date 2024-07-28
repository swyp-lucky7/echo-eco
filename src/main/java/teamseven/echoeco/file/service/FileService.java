package teamseven.echoeco.file.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileService {
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile file) {
        File uploadFile = convert(file);
        String url = uploadToS3(uploadFile);
        return url;
    }

    public File convert(MultipartFile multipartFile) {
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
