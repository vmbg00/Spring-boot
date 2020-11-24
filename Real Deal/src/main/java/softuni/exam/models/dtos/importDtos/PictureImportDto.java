package softuni.exam.models.dtos.importDtos;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;
import softuni.exam.models.entities.Car;

import javax.persistence.Column;
import java.time.LocalDateTime;

public class PictureImportDto {

    @Expose
    private String name;
    @Expose
    private LocalDateTime dateAndTime;
    @Expose
    private int car;

    public PictureImportDto() {
    }


    public int getCar() {
        return car;
    }

    public void setCar(int car) {
        this.car = car;
    }

    @Length(min = 2, max = 19)
    @Column(unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "date_and_time")
    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
}
