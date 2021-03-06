package com.md.luck.lottery.controller.web;

import com.md.luck.lottery.common.util.BasePath;
import com.md.luck.lottery.common.ResponMsg;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 上传文件处理
 */
@Controller
@RequestMapping("/lottery")
public class UploadController {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    /**
     * 上传图片
     * @param req req
     * @return ResponMsg
     */
    @ResponseBody
    @RequestMapping(value = "/upload/image", method = RequestMethod.POST)
    public ResponMsg upLoadFile(HttpServletRequest req, @RequestParam("file") MultipartFile image) {
        StringBuffer url = new StringBuffer();
        String filePath = "/lottery/" + sdf.format(new Date());
        String imgFolderPath = req.getServletContext().getRealPath(filePath);
        File imgFolder = new File(imgFolderPath);
        if (!imgFolder.exists()) {
            imgFolder.mkdirs();
        }
        url.append(req.getScheme())
                .append("://")
                .append(req.getServerName())
                .append(":")
                .append(req.getServerPort())
                .append(req.getContextPath())
                .append(filePath);
        String imgName = UUID.randomUUID() + "_" + image.getOriginalFilename().replaceAll(" ", "");
        try {
            System.out.println(BasePath.getBasePath());
            Thumbnails.of(image.getInputStream())
                    .size(760, 460)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(BasePath.getBasePath() + "/static/loicon.png")), 0.7f)
                    .outputQuality(0.8)
                    .toFile(new File(imgFolder, imgName));


//            IOUtils.write(image.getBytes(), new FileOutputStream(new File(imgFolder, imgName)));
            url.append("/").append(imgName);
            return ResponMsg.newSuccess(url.toString()).setMsg("success");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponMsg.newFail(null).setMsg("error:上传失败!");
    }
}
