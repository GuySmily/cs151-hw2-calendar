import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.TreeSet;

public class MyCalendar extends GregorianCalendar {
    private TreeSet<Event> events;

    //TODO: Is this needed?
    public MyCalendar(){
        events = new TreeSet<>();
    }

    /**
     * Adds an Event to this calendar
     * @param newEvent Event to add to calendar
     */
    public void addEvent(Event newEvent) {
        events.add(newEvent);
    }

    public void printEvents() {
        SimpleDateFormat sdfYear = new SimpleDateFormat("YYYY");
        Iterator<Event> eventIterator = events.iterator();

        Event currEvent = null;
        String year = "";
        String currYear = "I'm different";
        while (eventIterator.hasNext()) {
            currEvent = eventIterator.next();
            currYear = sdfYear.format(currEvent.getStart().getTime());
            if (! year.equals(currYear)){
                year = currYear;
                System.out.println(year);
            }
            System.out.println("  " + currEvent);
        }
    }
    /**
     * Print calendar for given year/month, with brackets around specified date if within given year/month.
     * A blank line will not be printed below calendar.
     */
    public void printMonth(){
        Calendar today = GregorianCalendar.getInstance();
        //Note: Throughout, we will add 1 character left pad to make space for today indicator in case it's Sunday
        int width = 22; // Calendar width in characters - including side gutters

        //Create a new calendar object for parsing through this month
        GregorianCalendar c = new GregorianCalendar();
        c.set(this.get(Calendar.YEAR), this.get(Calendar.MONTH), 1);

        // -------- Month/Year label --------
        String labelText = new SimpleDateFormat("MMMMM yyyy").format(c.getTime());
        System.out.println(centerPad(labelText, width));
//        int headerLength = header.length() + (width - header.length()) / 2; // + half of remaining whitespace
//        System.out.println(" " + String.format("%" + headerLength + "s", header));

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

        int day = 0;  // Could call c.get instead of using day variable
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

            // todo: Unverified assumption: Does c.getActualMaximum(Calendar.DAY_OF_WEEK) work for calendars with different last day of week?
            if (c.get(Calendar.DAY_OF_WEEK) == c.getActualMaximum(Calendar.DAY_OF_WEEK)) {
                //Don't print a newline if we just finished the last day of the month (this prevents duplicate newline when month ends on Saturday)
                if (day != lastDayOfMonth)
                    System.out.println(skipNextSpace ? "" : " ");
                skipNextSpace = false;
            }
            //If today then print [ else print space

            c.add(Calendar.DAY_OF_MONTH, 1);
        }
        System.out.println(" "); // TODO: A space gets printed even if there are brackets on the last day of the month
    }

    public void printDay() {
        String dayString = new SimpleDateFormat("EEE, MMM d yyyy").format(this.getTime());
        System.out.println(dayString);
    }

    private static boolean dateMatches(Calendar today, Calendar comp){
        return comp.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                && comp.get(Calendar.MONTH) == today.get(Calendar.MONTH)
                && comp.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
    }

    //Todo: Should this go into some generic utils.java file?
    private static String centerPad(String text, int width){
        if (text.length() >= width)
            return text;

        int leftPad = (width - text.length()) / 2; // half of remaining whitespace
        int rightPad = width - leftPad - text.length();
        String rightText = "";
        if (rightPad > 0)
            rightText = String.format("%" + rightPad + "s", " ");

        return String.format("%" + (leftPad + text.length()) + "s", text) + rightText;

    }
}
