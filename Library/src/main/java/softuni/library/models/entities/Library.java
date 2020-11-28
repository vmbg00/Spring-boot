package softuni.library.models.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "libraries")
public class Library extends BaseEntity{

    private String name;
    private String location;
    private int reating;
    private Set<Book> books;

    public Library() {
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "libraries_books",
    joinColumns = @JoinColumn(name = "library_id"),
    inverseJoinColumns = @JoinColumn(name = "books_id"))
    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Column
    public int getReating() {
        return reating;
    }

    public void setReating(int reating) {
        this.reating = reating;
    }
}
