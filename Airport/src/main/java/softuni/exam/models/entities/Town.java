package softuni.exam.models.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "towns")
public class Town extends BaseEntity{

    private String name;
    private int population;
    private String guide;
    private Set<Ticket> fromTickets;
    private Set<Ticket> toTickets;
    private Set<Passenger> passengers;

    public Town() {
    }

    @OneToMany(mappedBy = "town",fetch = FetchType.EAGER)
    public Set<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
    }

    @OneToMany(mappedBy = "toTown",fetch = FetchType.EAGER)
    public Set<Ticket> getToTickets() {
        return toTickets;
    }

    public void setToTickets(Set<Ticket> toTickets) {
        this.toTickets = toTickets;
    }

    @OneToMany(mappedBy = "fromTown",fetch = FetchType.EAGER)
    public Set<Ticket> getFromTickets() {
        return fromTickets;
    }

    public void setFromTickets(Set<Ticket> tickets) {
        this.fromTickets = tickets;
    }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    @Column
    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }
}
