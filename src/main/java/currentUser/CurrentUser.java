package currentUser;

public class CurrentUser {
    private static String currentUserNRP;

    public static String getCurrentUserNRP() {
        return currentUserNRP;
    }

    public static void setCurrentUserNRP(String currentUserNRP) {
        CurrentUser.currentUserNRP = currentUserNRP;
    }
}
