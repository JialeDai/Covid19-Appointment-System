package edu.nyu.covid19vaccinationsignupsystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.nyu.covid19vaccinationsignupsystem.utils.FilePath;
import edu.nyu.covid19vaccinationsignupsystem.utils.PropertiesUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Controller
@RequestMapping(path = "/api")
public class FileController {
    @PostMapping(path = "/file")
    public @ResponseBody String uploadSingleFile(@RequestParam("file") MultipartFile multipartFile) throws JsonProcessingException {
        System.out.println("file controller called");
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("no file is selected");
        }
        FilePath.getInstance().setUrl(null);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> res = new HashMap<>();
        res.put("uploadStatus","fail");
        try {
            String contentType = multipartFile.getContentType();
            String originalFilename = multipartFile.getOriginalFilename();
            byte[] bytes = multipartFile.getBytes();

            String filePath = PropertiesUtil.getProperty("upload-path");
            File parentPath = new File(filePath,"additional_info");
            try {
                File destFile = new File(parentPath,originalFilename);
                FileUtils.writeByteArrayToFile(destFile,multipartFile.getBytes());
                FilePath.getInstance().setUrl(destFile.getPath());
                res.put("uploadStatus","success");
                res.put("url",destFile.getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objectMapper.writeValueAsString(res);
    }
}
