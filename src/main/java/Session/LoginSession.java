package Session;

import java.time.LocalDateTime;

public class LoginSession {
    private static LoginSession instance;

    private String nrp;
    private String nama;
    private String email;
    private String role;
    private LocalDateTime waktuLogin;

    private LoginSession() {}

    public static LoginSession getInstance() {
        if (instance == null) {
            instance = new LoginSession();
        }
        return instance;
    }

    public void setSession(String nrp, String nama, String email, String role) {
        this.nrp = nrp;
        this.nama = nama;
        this.email = email;
        this.role = role;
        this.waktuLogin = LocalDateTime.now();
    }

    public void clearSession() {
        instance = null;
    }

    public String getNrp() { return nrp; }
    public String getNama() { return nama; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public LocalDateTime getWaktuLogin() { return waktuLogin;}
}
