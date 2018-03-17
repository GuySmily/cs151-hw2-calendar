import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final String name;
    private final String text;
    private LinkedHashMap<String, Runnable> actionsMap = new LinkedHashMap<>();

    public Menu(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public void putAction(String name, Runnable action) {
        actionsMap.put(name, action);
    }

    public String generateText() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(": ");
        sb.append(text).append(":\n");
        List<String> actionNames = new ArrayList<>(actionsMap.keySet());
        for (int i = 0; i < actionNames.size(); i++) {
            sb.append(String.format(" %d: %s%n", i + 1, actionNames.get(i)));
        }
        return sb.toString();
    }

    public void executeAction(int actionNumber) {
        int effectiveActionNumber = actionNumber - 1;
        if (effectiveActionNumber < 0 || effectiveActionNumber >= actionsMap.size()) {
            System.out.println("Ignoring menu choice: " + actionNumber);
        } else {
            List<Runnable> actions = new ArrayList<>(actionsMap.values());
            actions.get(effectiveActionNumber).run();
        }
    }

    public static class App {
        private Menu menu;
        private String password = "blahblah";

        public App() {
            Menu mainMenu = new Menu("Main", "main menu");
            Menu subMenuGetPassword = new Menu("Password", "get passwords");
            Menu subMenuSetPassword = new Menu("Set Password", "set password");

            mainMenu.putAction("get password menu", () -> activateMenu(subMenuGetPassword));
            mainMenu.putAction("quit", () -> System.exit(0));

            subMenuGetPassword.putAction("get password", () -> System.out.println(password));
            subMenuGetPassword.putAction("set password menu", () -> activateMenu(subMenuSetPassword));
            subMenuGetPassword.putAction("main menu", () -> activateMenu(mainMenu));
            subMenuGetPassword.putAction("quit", () -> System.exit(0));

            subMenuSetPassword.putAction("get password", this::setPassword);
            subMenuSetPassword.putAction("step back menu", () -> activateMenu(subMenuGetPassword));
            subMenuSetPassword.putAction("main menu", () -> activateMenu(mainMenu));
            subMenuSetPassword.putAction("quit", () -> System.exit(0));

            activateMenu(mainMenu);
        }

        private void activateMenu(Menu newMenu) {
            menu = newMenu;
            System.out.println(newMenu.generateText());
            Scanner scanner = new Scanner(System.in);
            while (true) {
                // TODO some error checking.
                int actionNumber = scanner.nextInt();
                menu.executeAction(actionNumber);
            }
        }

        private void setPassword() {
            // TODO ask for password on command lin eand set it
            password = "p2";
        }
    }

    public static void main(String[] args) {
        new App();
    }
}