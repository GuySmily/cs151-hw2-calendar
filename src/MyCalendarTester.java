import com.sun.tools.javac.Main;

import java.util.*;

import java.text.SimpleDateFormat;
import java.text.DateFormatSymbols;

public class MyCalendarTester
{
    public static void main(String [] args)
    {
        // --------- Create menus ---------
        // Main menu
        Menu mainMenu = new Menu("", "Select one of the following options:");
        mainMenu.addChoice("[L]oad",        null);
        mainMenu.addChoice("[V]iew by",     null);
        mainMenu.addChoice("[C]reate",      null);
        mainMenu.addChoice("[G]o to",       null);
        mainMenu.addChoice("[E]vent list",  null);
        mainMenu.addChoice("[D]elete",      null);
        mainMenu.addChoice("[Q]uit",        null);

        // View menu
        Menu viewMenu = new Menu("", "Select a view:");
        viewMenu.addChoice("[D]ay view",  null);
        viewMenu.addChoice("[M]onth view",null);

        // Nav menu
        Menu navMenu = new Menu("", "");
        navMenu.addChoice("[P]revious",    null);
        navMenu.addChoice("[N]ext",        null);
        navMenu.addChoice("[M]ain menu",   null);

        // --------- Create calendar ---------
        MyCalendar cal = new MyCalendar(); // defaults to today's date

        // --------- Required screenshots ---------
        //Screen 1: Show the initial calendar after starting the application
        cal.printMonth();
        System.out.println();

        //Shows prompt and gets user input (first character)
        while (true) {
            switch (mainMenu.prompt()) {
                case 'l':   //load
                    // The system loads events.txt to populate the calendar.
                    break;
                case 'v':   //view by
                    char viewChoice = viewMenu.prompt();

                    boolean exitView = false;
                    while (!exitView) {
                        switch (viewChoice) {
                            case 'd':
                                //Display day
                                cal.printDay();
                                //todo: print events
                                break;

                            default: //case 'm':
                                viewChoice = 'm';
                                //Display month
                                cal.printMonth();
                                //todo: print events
                                break;
                        }

                        char navChoice = navMenu.prompt();
                        switch (navChoice) {
                            case 'p':   //Prev
                            case 'n':   //Next
                                cal.add(viewChoice == 'd' ? Calendar.DAY_OF_MONTH : Calendar.MONTH,
                                        navChoice == 'p' ? -1 : 1);
                                break;
                            default: //case 'm':   //Main menu
                                //todo: reset cal to today's date?
                                exitView = true;
                                break;
                        }
                    }
                    break;  //end View by
                //}
                case 'c':   //create
                    // This option allows the user to schedule an event.
                    break;
                case 'g':   //go to
                    break;
                case 'e':   //event list
                    break;
                case 'd':   //delete
                    break;
                case 'q':   //quit
                    break;
            }
        }
//        Scanner sc = new Scanner(System.in);
//        printCalendar(cal);
////        "Select one of the following options: \n"
////        "[L]oad  [V]iew by  [C]reate  [G]o to  [E]vent list  [D]elete  [Q]uit"
//
//
//        //Screen 2: [L]oad your events.txt and show [E]vent list
//
//        //Screen 3: Select [V]iew by and choose the Day view and show the current Day view.
//
//        //Screen 4: From the Day view, press [P]revious and show the previous day.
//        //Screen 5: From the previous view, press [N]ext THREE times and show the next day.
//        //Screen 6: Move the Day view to show a day with an event.
//        //Screen 7: Select [M]ain menu and select [V]iew by and [M]onth view and show the current Month view.
//        //Screen 8: Move the Month view to show a month with an event.
//        //Screen 9: Select [C]reate and create an event. Show [E]vent list to confirm that the event is created.
//        //Screen 10: Select [C]reate and create an event with time conflict. Show you handled the time conflict.
//        //Screen 11: [G]o to a specific date with multiple events and show the events are listed in order.
//        //Screen 12: Select [D]elete and [S]elected to delete a selected event scheduled on a particular date. After deleting it, doe [E]vent list to show the result of the deletion.
//        //Screen 13: Select [D]elete and [A]ll to delete all events on a particular date. After deleting it, doe [E]vent list to show the result of the deletion.
//        //Screen 14: Create two more events and quit. Show your events.txt.
//        while (sc.hasNextLine())
//        {
//            String input = sc.nextLine();
//            if (input.equals("p"))
//            {
//                cal.add(Calendar.MONTH,-1);
//                printCalendar(cal);
//            }
//            else if (input.equals("n"))
//            {
//                cal.add(Calendar.MONTH,1);
//                printCalendar(cal);
//            }
//        }
//        System.out.println("Bye!");
    }


}