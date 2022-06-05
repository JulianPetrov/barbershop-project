package com.example.barbershopproject.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Repository
@Slf4j
public class FileSystemRepository {

  @Value("${image.folder}")
  private String IMAGES_FOLDER;

  @Value("${image.size}")
  private Integer IMAGE_SIZE;

  //String IMAGES_FOLDER = FileSystemRepository.class.getResource("/").getPath();

  public String save(byte[] content, String imageName) throws Exception {
    String fileName = new Date().getTime() + "-" + imageName + ".png";
    Path newFile = Paths.get(IMAGES_FOLDER + fileName);
    Files.createDirectories(newFile.getParent());

    Files.write(newFile, content);
    resizeImage(newFile.toFile());

    return fileName;
  }

  public FileSystemResource findInFileSystem(String location) {
    try {
      return new FileSystemResource(Paths.get(location));
    } catch (Exception e) {
      // Handle access or file not found problems.
      throw new RuntimeException();
    }
  }
  public byte[] getFileFromFileSystemAsByteArray(String location) {
    try {
      InputStream in = getClass()
              .getResourceAsStream(location);
      if (in != null) {
        return IOUtils.toByteArray(in);
      }return null;
    } catch (Exception e) {
      // Handle access or file not found problems.
      throw new RuntimeException();
    }
  }


  public boolean resizeImage(File sourceFile) {
    try {
      BufferedImage bufferedImage = ImageIO.read(sourceFile);
      BufferedImage outputImage = Scalr.resize(bufferedImage, IMAGE_SIZE);
      Path path = Paths.get(IMAGES_FOLDER, sourceFile.getName());
      File newImageFile = path.toFile();
      ImageIO.write(outputImage, "jpg", newImageFile);
      outputImage.flush();
      return true;
    } catch (IOException e) {
      log.error(e.getMessage(),e);
      return false;
    }
  }

  public boolean deleteImage(String fileName){
    File fileToDelete = FileUtils.getFile(IMAGES_FOLDER + fileName);
    return FileUtils.deleteQuietly(fileToDelete);
  }
}
