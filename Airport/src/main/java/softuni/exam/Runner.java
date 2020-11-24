package softuni.exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import softuni.exam.service.PassengerService;
import softuni.exam.service.PlaneService;
import softuni.exam.service.TicketService;
import softuni.exam.service.TownService;

@Component
public class Runner implements CommandLineRunner {

    private final TownService townService;
    private final PassengerService passengerService;
    private final PlaneService planeService;
    private final TicketService ticketService;

    @Autowired
    public Runner(TownService townService, PassengerService passengerService, PlaneService planeService, TicketService ticketService) {
        this.townService = townService;
        this.passengerService = passengerService;
        this.planeService = planeService;
        this.ticketService = ticketService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(this.townService.importTowns());
        System.out.println(this.passengerService.importPassengers());
        System.out.println(this.planeService.importPlanes());
        System.out.println(this.ticketService.importTickets());
        System.out.println(this.passengerService.getPassengersOrderByTicketsCountDescendingThenByEmail());
    }
}
