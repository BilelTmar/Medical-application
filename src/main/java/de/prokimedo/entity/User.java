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
    //! \name Account
    //@{
    //! Auto generated database ID.
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    //! User's user name.
    @Field
    @Column(length = 70, nullable = true)
    private String userName;
    //! User's password.
    @Field
    @Column(length = 70, nullable = true)
    private String password;
    //@}

    //! \name Personal data
    //@{
    //! User's titels.
    @Field
    @Column(length = 70, nullable = true)
    private String titles;    
    //! User's family names.
    @Field
    @Column(length = 70, nullable = true)
    private String familyNames;
    //! User's first names
    @Field
    @Column(length = 70, nullable = true)
    private String firstNames;
    //@}
    

    //! Constructor.
    public User(String id, String firstNames) {
        this.id = id;
        this.firstNames = firstNames;
    }
    //! Standard constructor.
    public User() {

    }
    
    //! Information about this class.
    @Override
    public String toString() {
        String str = ""; 
        str += "User {";
        {
            str += "id="        + id;
            str += " ";
            str += "firstNames=" + firstNames;
        }
        str += "}";
        return str;
    }
}



