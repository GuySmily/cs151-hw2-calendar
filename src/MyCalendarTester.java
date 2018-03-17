import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

import java.text.SimpleDateFormat;
import java.text.DateFormatSymbols;

public class MyCalendarTester
{
    public static void main(String [] args)
    {
        // In case execution spans change of day, this calendar shall represent "today" for the rest of execution
        MyCalendar cal = new MyCalendar(); // defaults to today's date
        cal.set(Calendar.DAY_OF_MONTH, 31);
        cal.printMonth();
        System.out.println();

        cal.add(Calendar.MONTH, 1);
        cal.printMonth();
        System.out.println();

        cal.add(Calendar.MONTH, 1);
        cal.printMonth();
        System.out.println();
//        Scanner sc = new Scanner(System.in);
//        printCalendar(cal);
////        "Select one of the following options: \n"
////        "[L]oad  [V]iew by  [C]reate  [G]o to  [E]vent list  [D]elete  [Q]uit"
//
//        //Required screenshots:
//        //Screen 1: Show the initial calendar after starting the application
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