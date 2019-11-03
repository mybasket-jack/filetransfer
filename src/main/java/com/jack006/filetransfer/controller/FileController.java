package com.jack006.filetransfer.controller;

import com.jack006.filetransfer.exception.GlobalException;
import com.jack006.filetransfer.result.CodeMsg;
import com.jack006.filetransfer.service.FileService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;


@Controller
@RequestMapping("/")
public class FileController {
    private static Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    FileService fileService;

    @RequestMapping("/list")
    public String list() {
        return "decry";
    }

    @RequestMapping("/encode")
    public String encode() {
        return "encry";
    }

    @RequestMapping(value = "/encryFile",method = RequestMethod.POST)
    public void encryFile(@RequestParam(value = "file") MultipartFile file, HttpServletResponse response) {
        String fileName = fileService.storeFile(file);
        logger.info(fileName+"文件进行加密。");
        String context = fileService.readContext(fileName);
        String aesStr = fileService.aes(context, true);
        if (aesStr != null){
            ByteArrayInputStream in = new ByteArrayInputStream(aesStr.getBytes());
            ServletOutputStream out = null;
            try {
                //重置输出流
                response.reset();
                response.setContentType("application/x-download;charset=UTF-8");
                response.addHeader("Cache-Control","no-cache");
                //为文件重新设置名字，采用数据库内存储的文件名称
                response.addHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"),"ISO8859-1") );
                out = response.getOutputStream();
                IOUtils.copy(in,out);
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                fileService.deleteFile(fileName);
            }
        }else{
            throw new GlobalException(CodeMsg.FILE_ENCODE_ERROR);
        }


    }

    @RequestMapping(value = "/decryFile",method=RequestMethod.POST)
    public String decryFile(@RequestParam(value = "file") MultipartFile file, HttpServletResponse response) {
        String fileName = fileService.storeFile(file);
        logger.info(fileName+"文件进行解密。");
        String context = fileService.readContext(fileName);
        String aesStr = fileService.aes(context, false);
        if (aesStr != null){
            ServletOutputStream out = null;
            try {
                ByteArrayInputStream in = new ByteArrayInputStream(aesStr.getBytes("UTF-8"));
                //重置输出流
                response.reset();
                response.setContentType("application/x-download;charset=UTF-8");
                //为文件重新设置名字，采用数据库内存储的文件名称
                response.addHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"),"ISO8859-1") );
                response.addHeader("Cache-Control","no-cache");
                out = response.getOutputStream();
                IOUtils.copy(in,out);
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                fileService.deleteFile(fileName);
            }
        }else{
            throw  new GlobalException(CodeMsg.FILE_DECODE_ERROR);
        }
        return null;
    }



}
