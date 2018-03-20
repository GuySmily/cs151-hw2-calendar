import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

/**
 * A Menu system that automatically converts bracketed [C]haracter into keys for selection
 * ORIGINAL SOURCE: https://codereview.stackexchange.com/a/106478
 */
public class Menu {
    private final String name;
    private final String text;
    private LinkedHashMap<String, Runnable> actionsMap = new LinkedHashMap<>();
    private LinkedHashMap<String, String> namesMap = new LinkedHashMap<>();

    /**
     * Creates a new menu, without any choices.  Call addChoice() to add choices.
     * @param name Menu name.  Displayed on first line, if not blank.
     * @param text Description.  Displayed on second line, if not blank.
     */
    public Menu(String name, String text) {
        this.name = name;
        this.text = text;
    }

    /**
     * Adds item to menu.  Specify key to press by enclosing it in [b]rackets.
     * @param name   Item name, with key in [b]rackets
     * @param action function to be called by this item
     * @return true if item was successfully added to menu
     */
    public boolean addChoice(String name, Runnable action) {
        int keyLoc = name.indexOf('[');
        String keyChar;

        //Guard clauses  //todo: more input validation
        if (keyLoc < 0 || keyLoc >= name.length() - 1) {
            System.out.println("No key character specified: " + name);
            return false;
        }
        keyChar = name.substring(keyLoc + 1, keyLoc + 2);
        if (namesMap.get(keyChar) != null){
            System.out.println("A menu item already exists with this key: " + keyChar);
            return false;
        }

        // todo: What's the right data structure for this?  Some kind of table/array?
        namesMap.put(keyChar.toLowerCase(), name);
        actionsMap.put(name, action);
        return true;
    }

    /**
     * Displays menu name on first line (if not blank), text on second line (if not blank), and choices on third line.
     * @return First character that was entered by user
     */
    public char prompt() {
        System.out.println(generateText());
        Scanner scanner = new Scanner(System.in);
        // todo: Input validation
        return Character.toLowerCase(scanner.next().charAt(0));
    }

    /**
     * Generates text for prompt, with menu name, text, and choices.
     * @return string containing prompt for user
     */
    private String generateText() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(name.equals("") ? "" : "\n");
        sb.append(text).append(text.equals("") ? "" : "\n");
        List<String> actionNames = new ArrayList<>(actionsMap.keySet());
        for (int i = 0; i < actionNames.size(); i++) {
            sb.append(String.format("%s  ", actionNames.get(i)));
        }
        return sb.toString();
    }

    /**
     * NOT USED / NOT FULLY IMPLEMENTED - Runs action for given menu choice
     * @param actionChar menu item's character
     */
    public void executeOption(char actionChar) {
        //TODO: Input validation
        //TODO: Need to convert actionChar -> Name in namesMap first
        Runnable action = actionsMap.get(String.valueOf(actionChar));
        if (action != null)
            action.run();
    }
}