import java.util.*;

public class MyCalendarTester
{
    public static void main(String [] args)
    {
        // --------- Create menus ---------
        // Main menu
        //Note: Second parameter not used in this implementation.
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

        // Delete menu
        Menu deleteMenu = new Menu("", "Which events should be deleted?");
        deleteMenu.addChoice("[S]elected",    null);
        deleteMenu.addChoice("[A]ll",         null);

        // Yes/No  ((wow this feels so VB6))
        Menu yesNoMenu = new Menu("", "");
        yesNoMenu.addChoice("[Y]es",    null);
        yesNoMenu.addChoice("[N]o",         null);

        // --------- Create calendar ---------
        MyCalendar cal = new MyCalendar(); // defaults to today's date

        // --------- Required screenshots ---------
        //Screen 1: Show the initial calendar after starting the application
        cal.printMonth();
        System.out.println();

        //Shows prompt and gets user input (first character)
        mainLoop:  //Label for breaking out when user wants to quit
        while (true) {
            switch (mainMenu.prompt()) {
                case 'l':   //load ---------------------
                    // The system loads events.txt to populate the calendar.
                    cal.loadEvents();
                    break;

                case 'v':   //view by ---------------------
                    char viewChoice = viewMenu.prompt();

                    boolean exitView = false;
                    while (!exitView) {
                        switch (viewChoice) {
                            case 'd':
                                //Screen 3: Select [V]iew by and choose the Day view and show the current Day view.
                                cal.printDay();  //Display day
                                //todo: print events
                                break;

                            default: //case 'm':
                                //Screen 7: Select [M]ain menu and select [V]iew by and [M]onth view and show the current Month view.
                                viewChoice = 'm';
                                cal.printMonth();  //Display month
                                //todo: print events
                                break;
                        }

                        char navChoice = navMenu.prompt();
                        switch (navChoice) {
                            //Screen 4: From the Day view, press [P]revious and show the previous day.
                            //Screen 5: From the previous view, press [N]ext THREE times and show the next day.
                            //Screen 6: Move the Day view to show a day with an event.
                            //Screen 8: Move the Month view to show a month with an event.
                            case 'p':   //Prev
                            case 'n':   //Next
                                cal.add(viewChoice == 'd' ? Calendar.DAY_OF_MONTH : Calendar.MONTH,
                                        navChoice == 'p' ? -1 : 1);
                                break;
                            case 'm':   //Main menu
                                //todo: reset cal to today's date?
                                exitView = true;
                                break;
                            default:
                                // Do nothing - the same day will be reprinted
                        }
                    }
                    break;  //end case 'v'

                case 'c':   //create event ---------------------
                    Event myEvent = new Event();  //This constructor includes prompts
                    cal.addEvent(myEvent);
                    System.out.println();
                    break;

                case 'g':   //go to ---------------------
                    cal.goToDay();
                    break;

                case 'e':   //event list ---------------------
                    cal.printAllEvents();
                    break;

                case 'd':   //delete ---------------------
                    Date dayToDelete = cal.promptForDate();
                    cal.setTime(dayToDelete);  // Set date to day entered by user
                    cal.printDay();
                    char deleteChoice = deleteMenu.prompt();
                    switch (deleteChoice) {
                        case 's':   //Selected
                            //Note: No requirements were specified for selecting events
                            System.out.println("Which event?");  //todo: Don't even ask if there's only one event

                            // Get iterator of events
                            Iterator<Event> eventIterator = cal.getEventIteratorForSingleDayOfEvents();
                            Event currEvent = null;
                            // loop through events
                            while (eventIterator.hasNext()) {
                                currEvent = eventIterator.next();
                                System.out.print(currEvent.toString() + "? ");
                                switch (yesNoMenu.prompt()) {
                                    case 'y':
                                        // TODO: Current implementation of Comparable cannot distinguish between different events with same start time.
                                        cal.deleteEvent(currEvent);
                                        break;
                                    default:
                                        // Do nothing
                                }
                                //todo: change from loop to removeAll(eventCollection)?
                            }

                            break;
                        case 'a':   //All
                            cal.deleteEventsForEntireDay();
                            break;
                        default:
                            System.out.println("Invalid entry.  Nothing deleted.");
                    }
                    break;
                case 'q':   //quit ---------------------
                    if (cal.saveEvents())  //Don't quit if events weren't saved
                        break mainLoop;
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
//
//        //Screen 9: Select [C]reate and create an event. Show [E]vent list to confirm that the event is created.
//        //Screen 10: Select [C]reate and create an event with time conflict. Show you handled the time conflict.
//        //Screen 11: [G]o to a specific date with multiple events and show the events are listed in order.
//        //Screen 12: Select [D]elete and [S]elected to delete a selected event scheduled on a particular date. After deleting it, doe [E]vent list to show the result of the deletion.
//        //Screen 13: Select [D]elete and [A]ll to delete all events on a particular date. After deleting it, doe [E]vent list to show the result of the deletion.
//        //Screen 14: Create two more events and quit. Show your events.txt.

    }


}