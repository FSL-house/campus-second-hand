package com.campus.controller;

import com.campus.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

/**
 * 文件上传控制层。
 */
@RestController
@RequestMapping("/file")
public class FileController {

    /**
     * 图片保存目录。
     */
    @Value("${file.upload-path}")
    private String uploadPath;

    /**
     * 上传商品图片接口。
     *
     * @param file 前端上传的图片文件
     * @return 上传后的图片访问路径
     * @throws IOException 文件保存异常
     */
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return Result.fail("请选择要上传的图片");
        }

        String originalFilename = file.getOriginalFilename();
        String suffix = getFileSuffix(originalFilename);
        if (!isImageSuffix(suffix)) {
            return Result.fail("只支持 jpg、jpeg、png、gif、webp 格式图片");
        }

        File directory = new File(uploadPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = UUID.randomUUID().toString().replace("-", "") + suffix;
        File targetFile = new File(directory, fileName);
        file.transferTo(targetFile);

        return Result.success("/images/" + fileName);
    }

    /**
     * 获取文件后缀名。
     *
     * @param fileName 原始文件名
     * @return 小写后缀名，包含点号
     */
    private String getFileSuffix(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".")).toLowerCase(Locale.ROOT);
    }

    /**
     * 判断是否为允许上传的图片格式。
     *
     * @param suffix 文件后缀名
     * @return true 表示允许上传
     */
    private boolean isImageSuffix(String suffix) {
        return ".jpg".equals(suffix)
                || ".jpeg".equals(suffix)
                || ".png".equals(suffix)
                || ".gif".equals(suffix)
                || ".webp".equals(suffix);
    }
}
