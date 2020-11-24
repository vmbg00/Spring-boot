package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.importDtos.CarImportDto;
import softuni.exam.models.entities.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.ValidatorUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final Gson gson;
    private final static String CAR_PATH = "src/main/resources/files/json/cars.json";
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, Gson gson, ModelMapper modelMapper, ValidatorUtil validatorUtil) {
        this.carRepository = carRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
    }

    @Override
    public boolean areImported() {
        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(CAR_PATH)));
    }

    @Override
    public String importCars() throws IOException {
        StringBuilder sb = new StringBuilder();
        CarImportDto[] carImportDto = this.gson.fromJson(readCarsFileContent(), CarImportDto[].class);
        for (CarImportDto importDto : carImportDto) {
            if (this.validatorUtil.isValid(importDto)) {
                Car car = this.modelMapper.map(importDto, Car.class);
                car.setKilometres(importDto.getKilometers());
                this.carRepository.saveAndFlush(car);

                sb.append(String.format("Successfully imported car - %s - %s", importDto.getMake(), importDto.getModel()))
                        .append(System.lineSeparator());
            }
            else {
                sb.append("Invalid car").append(System.lineSeparator());
            }
        }

        return sb.toString();
    }

    @Override
    public String getCarsOrderByPicturesCountThenByMake() {
        Set<Car> cars = this.carRepository.exportCars();
        StringBuilder sb = new StringBuilder();

        for (Car car : cars) {
            sb
                    .append(String.format("Car make - %s, model - %s",car.getMake(), car.getModel()))
                    .append(System.lineSeparator())
                    .append(String.format("\tKilometers - %d",car.getKilometres()))
                    .append(System.lineSeparator())
                    .append(String.format("\tRegistered on - %s",car.getRegisteredOn()))
                    .append(System.lineSeparator())
                    .append(String.format("\tNumber of pictures - %d",car.getPictures().size()))
                    .append(System.lineSeparator());
        }

        return sb.toString();
    }
}
