package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.importDtos.TicketImportDto;
import softuni.exam.models.dtos.importDtos.TicketsImportRootDto;
import softuni.exam.models.entities.Passenger;
import softuni.exam.models.entities.Plane;
import softuni.exam.models.entities.Ticket;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.repository.TicketRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TicketService;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final XmlParser xmlParser;
    private final ValidatorUtil validatorUtil;
    private final ModelMapper modelMapper;
    private final static String TICKET_PATH = "src/main/resources/files/xml/tickets.xml";
    private final PlaneRepository planeRepository;
    private final TownRepository townRepository;
    private final PassengerRepository passengerRepository;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, XmlParser xmlParser, ValidatorUtil validatorUtil, ModelMapper modelMapper, PlaneRepository planeRepository, TownRepository townRepository, PassengerRepository passengerRepository) {
        this.ticketRepository = ticketRepository;
        this.xmlParser = xmlParser;
        this.validatorUtil = validatorUtil;
        this.modelMapper = modelMapper;
        this.planeRepository = planeRepository;
        this.townRepository = townRepository;
        this.passengerRepository = passengerRepository;
    }


    @Override
    public boolean areImported() {
        return this.ticketRepository.count() > 0;
    }

    @Override
    public String readTicketsFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(TICKET_PATH)));
    }

    @Override
    @Transactional
    public String importTickets() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        TicketsImportRootDto ticketsImportRootDto = this.xmlParser.parseXml(TicketsImportRootDto.class, TICKET_PATH);

        for (TicketImportDto importDto : ticketsImportRootDto.getTickets()) {
            Optional<Ticket> bySerialNumber = this.ticketRepository.findBySerialNumber(importDto.getSerialNumber());
            if (this.validatorUtil.isValid(importDto) && bySerialNumber.isEmpty()) {
                Ticket ticket = this.modelMapper.map(importDto, Ticket.class);

                ticket.setPassenger(this.passengerRepository.findByEmail(importDto.getPassenger().getEmail()).get());
                ticket.setPlanes(this.planeRepository.findByRegisterNumber(importDto.getPlane().getRegisterNumber()).get());

                this.ticketRepository.saveAndFlush(ticket);
                sb.append(String.format("Successfully imported Ticket %s - %s",importDto.getFromTown().getName(),importDto.getToTown().getName()))
                        .append(System.lineSeparator());
            }
            else {
                sb.append("Invalid Ticket")
                        .append(System.lineSeparator());
            }

        }

        return sb.toString();
    }
}
