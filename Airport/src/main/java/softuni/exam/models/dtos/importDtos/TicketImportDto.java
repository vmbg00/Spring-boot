package softuni.exam.models.dtos.importDtos;

import org.hibernate.validator.constraints.Length;
import softuni.exam.config.LocalDateTimeAdapter;

import javax.validation.constraints.DecimalMin;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@XmlRootElement(name = "ticket")
@XmlAccessorType(XmlAccessType.FIELD)
public class TicketImportDto {

    @XmlElement(name = "serial-number")
    private String serialNumber;
    @XmlElement
    private double price;
    @XmlElement(name = "take-off")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime takeOff;
    @XmlElement(name = "from-town")
    private TicketFromTownImportDto fromTown;
    @XmlElement(name = "to-town")
    private TicketToTownImportDto toTown;
    @XmlElement
    private TicketPassengerImportDto passenger;
    @XmlElement
    private TicketPlaneImportDto plane;

    public TicketImportDto() {
    }


    @Length(min = 2)
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @DecimalMin(value = "0")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDateTime getTakeOff() {
        return takeOff;
    }

    public void setTakeOff(LocalDateTime takeOff) {
        this.takeOff = takeOff;
    }

    public TicketFromTownImportDto getFromTown() {
        return fromTown;
    }

    public void setFromTown(TicketFromTownImportDto fromTown) {
        this.fromTown = fromTown;
    }

    public TicketToTownImportDto getToTown() {
        return toTown;
    }

    public void setToTown(TicketToTownImportDto toTown) {
        this.toTown = toTown;
    }

    public TicketPassengerImportDto getPassenger() {
        return passenger;
    }

    public void setPassenger(TicketPassengerImportDto passenger) {
        this.passenger = passenger;
    }

    public TicketPlaneImportDto getPlane() {
        return plane;
    }

    public void setPlane(TicketPlaneImportDto plane) {
        this.plane = plane;
    }
}
