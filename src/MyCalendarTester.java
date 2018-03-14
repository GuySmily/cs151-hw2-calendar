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
        GregorianCalendar cal = new GregorianCalendar(); // captures today's date
//        printMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal);
//        System.out.println();

//        printMonth(cal.get(Calendar.YEAR), Calendar.JANUARY, cal);
        printMonth(cal.get(Calendar.YEAR), Calendar.FEBRUARY, cal);
        printMonth(cal.get(Calendar.YEAR), Calendar.MARCH, cal);
        printMonth(cal.get(Calendar.YEAR), Calendar.APRIL, cal);
//        printMonth(cal.get(Calendar.YEAR), Calendar.MAY, cal);
//        printMonth(cal.get(Calendar.YEAR), Calendar.JUNE, cal);
//        printMonth(cal.get(Calendar.YEAR), Calendar.JULY, cal);
        printMonth(cal.get(Calendar.YEAR), Calendar.AUGUST, cal);
        printMonth(cal.get(Calendar.YEAR), Calendar.SEPTEMBER, cal);
        printMonth(cal.get(Calendar.YEAR), Calendar.OCTOBER, cal);
//        printMonth(cal.get(Calendar.YEAR), Calendar.NOVEMBER, cal);
//        printMonth(cal.get(Calendar.YEAR), Calendar.DECEMBER, cal);

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

    /**
     * Print camendar for given year/month, with brackets around specified date
     * @param year
     * @param month
     * @param today Brackets will be placed around the date represented in this Calendar
     */
    public static void printMonth(int year, int month, Calendar today){
        //Note: Throughout, we will add 1 character left pad to make space for today indicator in case it's Sunday
        int width = 21; // Calendar width in characters -- todo: this includes that extra character - is it off center?

        //Initialize calendar object to 1st of given year/month
        GregorianCalendar c = new GregorianCalendar();
        c.set(year, month, 1);

        // -------- Month/Year label --------
        SimpleDateFormat sdfHeader = new SimpleDateFormat("MMMMM yyyy");
        String header = sdfHeader.format(c.getTime());                      // Calculate length of Month/Year
        int headerLength = header.length() + (width - header.length()) / 2; // + half of remaining whitespace
        System.out.println(" " + String.format("%" + headerLength + "s", header));

        // -------- Weekday labels --------
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] weekdays = dfs.getShortWeekdays();      //List of weekdays
        System.out.print(" ");  // Add 1 character pad to the left in case of brackets
        for(int i = 1; i < weekdays.length; i++)  // Note: getShortWeekdays seems to be start at 1 instead of 0
        {
            System.out.print(weekdays[i].substring(0,2) + " ");
        }
        System.out.println();

        // -------- Day numbers --------
        int spaces = c.get(Calendar.DAY_OF_WEEK) - 1;  // Print blank spaces up to first day
        if (spaces > 0)
            System.out.print(String.format("%" + spaces * 3 + "s", " "));

        // We'll use ints instead of calling c.get over and over
        int day = 0;
        int lastDayOfMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        boolean skipNextSpace = false;
        while(day + 1 <= lastDayOfMonth){
            day = c.get(Calendar.DAY_OF_MONTH);  // Current day being printed

            //Space before number
            if (!skipNextSpace)
                System.out.print(dateMatches(today, c) ? "[" : " ");
            else
                skipNextSpace = false; //Done skipping space; print the next one

            System.out.print(String.format("%2d", day));  //Day number

            if (dateMatches(c, today)){
                System.out.print("]");
                skipNextSpace = true;
            }

            // todo: Unverified assumption.  Does c.getActualMaximum(Calendar.DAY_OF_WEEK) work for calendars with different last day of week?
            if (c.get(Calendar.DAY_OF_WEEK) == c.getActualMaximum(Calendar.DAY_OF_WEEK)) {
                //Don't print a newline if we just finished the last day of the month (this prevents duplicate newline when month ends on Saturday)
                if (day != lastDayOfMonth)
                    System.out.println();
                skipNextSpace = false;
            }
            //If today then print [ else print space

            c.add(Calendar.DAY_OF_MONTH, 1);
        }
        System.out.println();
    }

    private static boolean dateMatches(Calendar today, Calendar comp){
        return comp.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                && comp.get(Calendar.MONTH) == today.get(Calendar.MONTH)
                && comp.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
    }
}