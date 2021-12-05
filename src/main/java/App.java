import controllers.LoginController;
import models.User;

public class App {
    public static void main(final String[] args) {
        final LoginController loginController = LoginController.getOnlyInstance();

        loginController.register("test3", "password", User.UserRole.RENTER).ifPresent(user -> System.out.println(user.getUsername()));

    }
}
