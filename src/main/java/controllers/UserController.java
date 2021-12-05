package controllers;

public class UserController {
    private static final UserController userController = new UserController();

    private UserController() {}

    public UserController getOnlyInstance() {
        return userController;
    }
}
