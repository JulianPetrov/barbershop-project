package com.example.barbershopproject.service;

import com.example.barbershopproject.repository.FileSystemRepository;
import com.example.barbershopproject.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {

  private final FileSystemRepository fileSystemRepository;
  private final ImageRepository imageRepository;

  //TODO: ADJUST
 /* public void saveAllImages(List<MultipartFile> images, CarListing carListing) {
    int counter = 1;
    for (MultipartFile image : images) {
      try {
        String imageName = String.format("image_%d_%d", counter, carListing.getId());
        save(image.getBytes(), imageName, carListing);
        counter++;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void save(byte[] bytes, String imageName, CarListing carListing) throws Exception {
    String fileName = fileSystemRepository.save(bytes, imageName);
    imageRepository.save(new Image(fileName, carListing));
  }

  public List<String> getImageNamesForCarListing(Long carListingId) {
    return imageRepository.findAllByCarListing_Id(carListingId).stream()
        .map(Image::getName)
        .collect(Collectors.toList());
  }

  public void deleteImagesOfCarListing(Long carListingId){
    List<Image> carListingImages = imageRepository.findAllByCarListing_Id(carListingId);
    for (Image img : carListingImages) {
      fileSystemRepository.deleteImage(img.getName());
    }
    imageRepository.deleteAll(carListingImages);
  }*/
}
