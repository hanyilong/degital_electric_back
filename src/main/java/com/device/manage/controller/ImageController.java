package com.device.manage.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Value("${file.upload.dir}")
    private String uploadDir;

    /**
     * 图片预览接口
     * @param filename 文件名
     * @return 图片文件
     */
    @GetMapping("/preview")
    public ResponseEntity<byte[]> previewImage(@RequestParam String filename) {
        try {
            // 构建文件路径
            Path filePath = Paths.get(uploadDir, filename);
            File file = filePath.toFile();

            // 检查文件是否存在
            if (!file.exists()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // 读取文件内容
            byte[] imageBytes = Files.readAllBytes(filePath);

            // 确定文件类型
            String contentType = determineContentType(filename);

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentLength(imageBytes.length);

            // 返回图片
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据文件名确定文件类型
     * @param filename 文件名
     * @return MIME类型
     */
    private String determineContentType(String filename) {
        if (filename == null || !filename.contains(".")) {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG_VALUE;
            case "png":
                return MediaType.IMAGE_PNG_VALUE;
            case "gif":
                return MediaType.IMAGE_GIF_VALUE;
            case "bmp":
                return "image/bmp";
            case "webp":
                return "image/webp";
            default:
                return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
    }
}