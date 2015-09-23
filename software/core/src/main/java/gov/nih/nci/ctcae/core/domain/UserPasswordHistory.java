package gov.nih.nci.ctcae.core.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "PASSWORD_HISTORY")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_password_history_id")})
public class UserPasswordHistory extends BasePersistable implements Comparable<UserPasswordHistory>{

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    
    @Column(name = "password_creation_timestamp")
    private Timestamp passwordCreationTimestamp;
    
    public UserPasswordHistory() {
	}
    
	public UserPasswordHistory(String password, Timestamp passwordLastSet) {
		this.password = password;
		this.passwordCreationTimestamp = passwordLastSet;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getPasswordCreationTimestamp() {
		return passwordCreationTimestamp;
	}

	public void setPasswordCreationTimestamp(Timestamp passwordCreationTimestamp) {
		this.passwordCreationTimestamp = passwordCreationTimestamp;
	}

	@Override
	public int compareTo(UserPasswordHistory other) {
		if(other != null){
			return this.passwordCreationTimestamp.compareTo(other.passwordCreationTimestamp);
		}
		return -1;
	}
	
	@Override
	public boolean equals(Object object){
		if(object != null && object instanceof UserPasswordHistory){
			UserPasswordHistory other = (UserPasswordHistory) object;
			return password.equals(other.password) &&
					 user.equals(other.user);
		}
		return false;
	}
	
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

}
