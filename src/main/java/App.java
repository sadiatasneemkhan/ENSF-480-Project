import controllers.LoginController;
import view.LoginForm;

public class App {
    private static final LoginController loginController = LoginController.getOnlyInstance();
    public static void main(final String[] args) {
        javax.swing.SwingUtilities.invokeLater(App::systemFlow);
    }

    private static void systemFlow() {
        final LoginForm loginForm = new LoginForm();
    }
}
