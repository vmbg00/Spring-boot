package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.importDtos.PassengerImportDto;
import softuni.exam.models.entities.Passenger;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.util.ValidatorUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

@Service
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final Gson gson;
    private final static String PASSENGER_PATH = "src/main/resources/files/json/passengers.json";
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;
    private final TownRepository townRepository;

    @Autowired
    public PassengerServiceImpl(PassengerRepository passengerRepository, Gson gson, ModelMapper modelMapper, ValidatorUtil validatorUtil, TownRepository townRepository) {
        this.passengerRepository = passengerRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
        this.townRepository = townRepository;
    }

    @Override
    public boolean areImported() {
        return this.passengerRepository.count() > 0;
    }

    @Override
    public String readPassengersFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(PASSENGER_PATH)));
    }

    @Override
    public String importPassengers() throws IOException {
        StringBuilder sb = new StringBuilder();
        PassengerImportDto[] passengerImportDto = this.gson.fromJson(readPassengersFileContent(), PassengerImportDto[].class);

        for (PassengerImportDto importDto : passengerImportDto) {

            Optional<Passenger> byName = this.passengerRepository.findByEmail(importDto.getEmail());
            if (this.validatorUtil.isValid(importDto) && byName.isEmpty()) {
                Passenger passenger = this.modelMapper.map(importDto, Passenger.class);

                Optional<Town> townName = this.townRepository.findByName(importDto.getTown());

                passenger.setTown(townName.get());

                this.passengerRepository.saveAndFlush(passenger);
                sb.append(String.format("Successfully imported Passenger %s - %s",importDto.getLastName(), importDto.getEmail()))
                        .append(System.lineSeparator());
            }
            else {
                sb.append("Invalid Passenger")
                        .append(System.lineSeparator());
            }
        }

        return sb.toString();
    }

    @Override
    public String getPassengersOrderByTicketsCountDescendingThenByEmail() {
        StringBuilder sb = new StringBuilder();
        Set<Passenger> passenger = this.passengerRepository.exportPassengers();

        for (Passenger pass : passenger) {
            sb
                    .append(String.format("Passenger %s %s", pass.getFirstName(), pass.getLastName()))
                    .append(System.lineSeparator())
                    .append(String.format("\tEmail - %s",pass.getEmail()))
                    .append(System.lineSeparator())
                    .append(String.format("\tPhone - %s",pass.getPhoneNumber()))
                    .append(System.lineSeparator())
                    .append(String.format("\tNumber of tickets - %d",pass.getTickets().size()))
                    .append(System.lineSeparator());
        }

        return sb.toString();
    }
}
