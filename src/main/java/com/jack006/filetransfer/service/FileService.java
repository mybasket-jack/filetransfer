package com.jack006.filetransfer.service;

import com.jack006.filetransfer.config.FileConfig;
import com.jack006.filetransfer.exception.GlobalException;
import com.jack006.filetransfer.result.CodeMsg;
import com.jack006.filetransfer.util.AESUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {
    private final Path fileStorageLocation; // 文件在本地存储的地址

    @Autowired
    public FileService(FileConfig fileConfig) {
        this.fileStorageLocation = Paths.get(fileConfig.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new GlobalException(CodeMsg.FILE_CREATE_PATH_ERROR);
        }
    }

    @Autowired
    FileConfig fileConfig;

    /**
     * 存储文件到系统
     *
     * @param file 文件
     * @return 文件名
     */
    public String storeFile(MultipartFile file) {
        // 获取文件名称
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            // 校验文件是否包含非法字符
            if(fileName.contains("..")) {
                throw new GlobalException(CodeMsg.FILE_CHARACTER_ERROR);
            }
            // 存储文件
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new GlobalException(CodeMsg.FILE_SAVE_ERROR);
        }
    }

    /**
     * 读取文件的内容
     * @param fileName
     * @return
     */
    public String readContext(String fileName){
        Path filePath = this.fileStorageLocation.resolve(fileName).normalize();

        UrlResource resource = null;
        String fileContext = null;
        try {
            resource = new UrlResource(filePath.toUri());
            if (resource.exists()){
                InputStream in = resource.getInputStream();
                fileContext = IOUtils.toString(in, "utf-8");
                in .close();
            }
            return fileContext;
        } catch (IOException e) {
            e.printStackTrace();
            throw new GlobalException(CodeMsg.FILE_READ_ERROR);
        }
    }

    /**
     * 对内容加密或解密
     * @param context 传入内容
     * @param isEncrypt 是否加密 true加密  false 解密
     * @return
     */
    public String aes(String context,boolean isEncrypt) {
        String aesStr = null;
        try {
            String aesIv = fileConfig.getAesIv();
            String aesKey = fileConfig.getAesKey();
            if (isEncrypt){
                aesStr = AESUtil.encode(context,aesKey,aesIv);
            }else{
                aesStr = AESUtil.decode(context,aesKey,aesIv);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw  new GlobalException(CodeMsg.FILE_AES_ERROR);
        }
        return aesStr;
    }

    public  void deleteFile(String fileName){
        String filePath = fileConfig.getUploadDir()+"/"+fileName;
        File file = new File(filePath);
        if (file.exists()){
            file.delete();
        }
    }

}
