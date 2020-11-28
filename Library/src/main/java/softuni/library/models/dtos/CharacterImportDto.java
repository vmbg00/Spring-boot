package softuni.library.models.dtos;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import softuni.library.config.LocalDateTimeAdapter;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;


@XmlRootElement(name = "character")
@XmlAccessorType(XmlAccessType.FIELD)
public class CharacterImportDto {

    @XmlElement
    private int age;
    @XmlElement
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDate birthday;
    @XmlElement(name = "first-name")
    private String firstName;
    @XmlElement(name = "last-name")
    private String lastName;
    @XmlElement(name = "middle-name")
    private String middleName;
    @XmlElement
    private String role;
    @XmlElement(name = "book")
    private CharacterBookImportDto book;

    public CharacterImportDto() {
    }

    @Range(min = 3, max = 66)
    @NotNull
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @NotNull
    public LocalDate getBirthDay() {
        return birthday;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthday = birthDay;
    }

    @Length(min = 3)
    @NotNull
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Length(min = 3)
    @NotNull
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Length(max = 1)
    @NotNull
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @Length(min = 5)
    @NotNull
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public CharacterBookImportDto getBook() {
        return book;
    }

    public void setBook(CharacterBookImportDto book) {
        this.book = book;
    }
}
