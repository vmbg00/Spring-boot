package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.importDtos.PictureImportDto;
import softuni.exam.models.entities.Picture;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.PictureService;
import softuni.exam.util.ValidatorUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class PictureServiceImpl implements PictureService {

    private final ModelMapper modelMapper;
    private final Gson gson;
    private final PictureRepository pictureRepository;
    private final ValidatorUtil validatorUtil;
    private final static String PICTURE_PATH = "src/main/resources/files/json/pictures.json";
    private final CarRepository carRepository;

    @Autowired
    public PictureServiceImpl(ModelMapper modelMapper, Gson gson, PictureRepository pictureRepository, ValidatorUtil validatorUtil, CarRepository carRepository) {
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.pictureRepository = pictureRepository;
        this.validatorUtil = validatorUtil;
        this.carRepository = carRepository;
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesFromFile() throws IOException {
        return String.join("", Files.readAllLines(Path.of(PICTURE_PATH)));
    }

    @Override
    public String importPictures() throws IOException {
        StringBuilder sb = new StringBuilder();
        PictureImportDto[] pictureImportDtos = this.gson.fromJson(readPicturesFromFile(), PictureImportDto[].class);

        for (PictureImportDto pictureImportDto : pictureImportDtos) {
            Optional<Picture> byName = this.pictureRepository.findByName(pictureImportDto.getName());
            if (this.validatorUtil.isValid(pictureImportDto) && byName.isEmpty()) {
                Picture pic = this.modelMapper.map(pictureImportDto, Picture.class);
                pic.setCar(this.carRepository.getOne(pictureImportDto.getCar()));

                this.pictureRepository.saveAndFlush(pic);

                sb.append(String.format("Successfully import picture %s",pictureImportDto.getName()))
                        .append(System.lineSeparator());
            }
            else {
                sb.append("Invalid picture").append(System.lineSeparator());
            }
        }
        return sb.toString();
    }
}
