package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.importDtos.jsonImport.PlayersImportDto;
import softuni.exam.domain.entities.Player;
import softuni.exam.repository.PlayerRepository;
import softuni.exam.service.PlayerService;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;

import javax.transaction.Transactional;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

@Service
public class PlayerServiceImpl implements PlayerService {


    private final ModelMapper modelMapper;
    private final Gson gson;
    private final PlayerRepository playerRepository;
    private final ValidatorUtil validatorUtil;
    private final static String PLAYER_PATH = "src/main/resources/files/json/players.json";

    @Autowired
    public PlayerServiceImpl(ModelMapper modelMapper, Gson gson, PlayerRepository playerRepository, ValidatorUtil validatorUtil) {
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.playerRepository = playerRepository;
        this.validatorUtil = validatorUtil;
    }

    @Override
    @Transactional
    public String importPlayers() throws IOException {
        StringBuilder sb = new StringBuilder();
        PlayersImportDto[] playersImportDto = this.gson.fromJson(new FileReader(PLAYER_PATH), PlayersImportDto[].class);
        for (PlayersImportDto importDto : playersImportDto) {

            if (this.validatorUtil.isValid(importDto)) {
                Player player = this.modelMapper.map(importDto, Player.class);

                this.playerRepository.saveAndFlush(player);
                sb.append(String.format("Successfully imported player: %s %s", importDto.getFirstName(), importDto.getLastName()))
                        .append(System.lineSeparator());
            }
            else {
                sb.append("Invalid player")
                        .append(System.lineSeparator());
            }
        }

        return sb.toString();
    }

    @Override
    public boolean areImported() {
        return this.playerRepository.count() > 0;
    }

    @Override
    public String readPlayersJsonFile() throws IOException {
        return String.join("", Files.readAllLines(Path.of(PLAYER_PATH)));
    }

    @Override
    public String exportPlayersWhereSalaryBiggerThan() {
        StringBuilder sb = new StringBuilder();
        Set<Player> players = this.playerRepository.findAllBySalaryGreaterThanOrderBySalaryDesc(BigDecimal.valueOf(100000));
        for (Player player : players) {
            sb
                    .append(String.format("Player name: %s %s",player.getFirstName(),player.getLastName()))
                    .append(System.lineSeparator())
                    .append(String.format("\tNumber: %d",player.getNumber()))
                    .append(System.lineSeparator())
                    .append(String.format("\tSalary: %s",player.getSalary()))
                    .append(System.lineSeparator())
                    .append(String.format("\tTeam: %s",player.getTeam().getName()))
                    .append(System.lineSeparator());
        }

        return sb.toString();
    }

    @Override
    public String exportPlayersInATeam() {
        StringBuilder sb = new StringBuilder();
        Set<Player> north_hub = this.playerRepository.exportPlayersInNorthHub("North Hub");
        for (Player player : north_hub) {
            sb
                    .append(String.format("Player name: %s %s - %s", player.getFirstName(), player.getLastName(), player.getPosition().toString()))
                    .append(System.lineSeparator())
                    .append(String.format("Number: %d", player.getNumber()))
                    .append(System.lineSeparator());
        }

        return sb.toString();
    }
}
