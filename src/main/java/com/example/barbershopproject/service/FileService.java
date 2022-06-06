package com.example.barbershopproject.service;

import com.example.barbershopproject.model.Image;
import com.example.barbershopproject.model.Salon;
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

  public void saveAllImages(List<MultipartFile> images, Salon salon) {
    int counter = 1;
    for (MultipartFile image : images) {
      try {
        String imageName = String.format("image_%d_%d", counter, salon.getId());
        save(image.getBytes(), imageName, salon);
        counter++;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void save(byte[] bytes, String imageName, Salon salon) throws Exception {
    String fileName = fileSystemRepository.save(bytes, imageName);
    imageRepository.save(new Image(fileName, salon));
  }

  public List<String> getImageNamesForSalon(Long salonId) {
    return imageRepository.findAllBySalon_Id(salonId).stream()
        .map(Image::getName)
        .collect(Collectors.toList());
  }

  public void deleteImagesOfCarListing(Long carListingId){
    List<Image> carListingImages = imageRepository.findAllBySalon_Id(carListingId);
    for (Image img : carListingImages) {
      fileSystemRepository.deleteImage(img.getName());
    }
    imageRepository.deleteAll(carListingImages);
  }
}
