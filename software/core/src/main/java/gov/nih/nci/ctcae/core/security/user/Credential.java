package gov.nih.nci.ctcae.core.security.user;

import gov.nih.nci.ctcae.core.domain.User;

public class Credential {
    private String _userName;

       private String _password;

       private User user;

       public Credential(String userName, String password) {
           _userName = userName;
           _password = password;
       }

       public String getPassword() {
           return _password;
       }

       public String getUserName() {
           return _userName;
       }

       public User getUser() {
           return user;
       }

       public void setUser(User user) {
           this.user = user;
       }

}
