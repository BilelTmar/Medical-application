package de.prokimedo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;


//! \name A user like a doctor, nurse, etc.
@Entity
@Indexed
public class ProkimedoUser implements Serializable
{
    //! \name Account
    //@{
    //! Auto generated database ID.
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
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
    public ProkimedoUser(Long id, String firstNames) {
        this.id = id;
        this.firstNames = firstNames;
    }
    //! Standard constructor.
    public ProkimedoUser() {

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



