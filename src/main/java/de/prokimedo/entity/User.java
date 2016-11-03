package de.prokimedo.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Field;


//! \name A user like a doctor, nurse, etc.
@Entity
@Indexed
public class User implements Serializable
{
    //! Auto generated database ID
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    //! User's first name
    @Field
    @Column(length = 70, nullable = true)
    private String firstName;

    //! Constructor.
    public User(String id, String firstName) {
        this.id = id;
        this.firstName = firstName;
    }
    //! Standard constructor.
    public User() {

    }
    
    //! Information about this class.
    @Override
    public String toString() {
        String str = ""; 
        str += "Krankheit {";
        {
            str += "id="        + id;
            str += " ";
            str += "firstName=" + firstName;
        }
        str += "}";
        return str;
    }
}



