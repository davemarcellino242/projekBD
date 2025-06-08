package currentUser;

import Model.mahasiswa;

public class SessionManager {
    private static mahasiswa currentUser;

    public static void setCurrentUser(mahasiswa user) {
        currentUser = user;
    }

    public static mahasiswa getCurrentUser() {
        return currentUser;
    }

    public static void clearSession() {
        currentUser = null;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
