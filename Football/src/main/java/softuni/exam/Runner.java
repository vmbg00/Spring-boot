package softuni.exam;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import softuni.exam.service.PictureService;
import softuni.exam.service.PlayerService;
import softuni.exam.service.TeamService;

@Component
public class Runner implements CommandLineRunner {

    private final PictureService pictureService;
    private final TeamService teamService;
    private final PlayerService playerService;

    @Autowired
    public Runner(PictureService pictureService, TeamService teamService, PlayerService playerService) {
        this.pictureService = pictureService;
        this.teamService = teamService;
        this.playerService = playerService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(this.pictureService.importPictures());
        System.out.println(this.teamService.importTeams());
        System.out.println(this.playerService.importPlayers());
        System.out.println(this.playerService.exportPlayersInATeam());
        System.out.println(this.playerService.exportPlayersWhereSalaryBiggerThan());
    }
}
