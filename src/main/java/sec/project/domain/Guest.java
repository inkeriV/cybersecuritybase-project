package sec.project.domain;

import javax.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Guest extends AbstractPersistable<Long> {

    private String username;
    private String name;
    private String phonenumber;
    private String email;

    public Guest() {
        super();
    }

    public Guest(String username, String name, String number, String email) {
        this();
        this.username = username;
        this.name = name;
        this.phonenumber = number;
        this.email = email;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return phonenumber;
    }

    public void setNumber(String address) {
        this.phonenumber = address;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

}

