package com.cdtu.reggie.controller;

import com.cdtu.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){

        //获取文件原始名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.indexOf("."));
        //通过UUID重新声明文件名，防止文件名重复覆盖文件
        String fileName = UUID.randomUUID().toString() + suffix;
        //创建文件存放目录
        File dir = new File(basePath);
        //创建文件
        if (!dir.exists()){
            dir.mkdirs();
        }
        //将上传的临时文件存放到指定位置
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        //定义输入流，通过输入流读取文件
        try {
            FileInputStream file = new FileInputStream(new File(basePath + name));
            //通过response对象，获取到输出流
            ServletOutputStream outputStream = response.getOutputStream();
            //通过response对象设置响应数据格式(image/jpeg)
            response.setContentType("image/jpeg");
            //通过输入流读取文件数据，然后通过上述的输出流写回浏览器
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = file.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            file.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
