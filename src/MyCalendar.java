import java.io.*;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A Calendar with its own set of events.
 * Conflicting events are not allowed (must be nice!)
 */
public class MyCalendar extends GregorianCalendar {
    private TreeSet<Event> events;  // see: https://www.geeksforgeeks.org/treeset-in-java-with-examples/

    /**
     * Creates a new MyCalendar with an empty set of events.
     */
    public MyCalendar(){
        events = new TreeSet<>();
    }

    /**
     * Adds an Event to this calendar, if there are no conflicts with existing events
     * @param newEvent Event to add to calendar
     * @return true if event was added
     */
    public boolean addEvent(Event newEvent) {
        boolean conflict = false;

        // Backup the currently set date/time of this calendar
        Calendar dateBackup = new GregorianCalendar();
        dateBackup.setTime(this.getTime());

        // Get iterator of events scheduled for the same day as newEvent
        this.setTime(newEvent.getStart().getTime());  // Needed for getEventIterator...
        Iterator<Event> eventIterator = getEventIteratorForSingleDayOfEvents();
        this.setTime(dateBackup.getTime());           // Restore backed up date/time
        Event currEvent = null;
        while (eventIterator.hasNext()) {
            currEvent = eventIterator.next();

            //Conflict detection:
            //SIMPLIFIED:
            //If the new event starts or ends during an existing event
            //OR an existing event starts or ends during the new event  <-- needed in case one is encased by the other
            //FULL VERSION:
            //( if newEvent's startTime is >= currEvent's startTime
            //  AND newEvent's startTime is < currEvent's endTime
            //) OR (
            //( if currEvent's startTime is >= newEvent's startTime
            //  AND currEvent's startTime is < newEvent's endTime )
            //...then there is a conflict
            //todo: THIS ISN'T WORKING IN BOTH DIRECTIONS!!!
            if
            (
                (
                    (
                        newEvent.getStart().getTime().after(currEvent.getStart().getTime()) ||
                        newEvent.getStart().getTime().equals(currEvent.getStart().getTime())
                    )
                    &&
                    (
                        newEvent.getStart().getTime().before(currEvent.getEnd().getTime())
                    )
                )
                ||
                (
                    (
                        currEvent.getStart().getTime().after(newEvent.getStart().getTime()) ||
                        currEvent.getStart().getTime().equals(newEvent.getStart().getTime())
                    )
                    &&
                    (
                        currEvent.getStart().getTime().before(newEvent.getEnd().getTime())
                    )
                )
            )
            {
                // A conflicting event was found!
                conflict = true;
                break;  //Don't bother looping anymore - we'll print this event for user
            }
        }

        if (conflict) {
            System.out.println("Event not added.  Conflicts with existing event:");
            System.out.println(currEvent.toString(false));
        }
        else {
            events.add(newEvent);
            System.out.println("Event added.");
        }


        return !conflict;
    }

    /**
     * Prints all events, grouped by year
     */
    public void printAllEvents() {
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
        System.out.println();
    }

    /**
     * Prints the events for the currently set day.
     */
    public void printEventsForDay() {
        Iterator<Event> eventIterator = getEventIteratorForSingleDayOfEvents();

        Event currEvent = null;
        while (eventIterator.hasNext()) {
            currEvent = eventIterator.next();
            System.out.println("  " + currEvent.toString(false));
        }
        System.out.println();
    }

    /**
     * @return Interator for events that occur on a single day (this calendar's current date)
     */
    public Iterator<Event> getEventIteratorForSingleDayOfEvents() {
        Calendar dummyStart = new GregorianCalendar();
        Calendar dummyEnd = new GregorianCalendar();
        dummyStart.setTime(this.getTime());
        dummyEnd.setTime(this.getTime());
        dummyEnd.add(DAY_OF_MONTH, 1);
        //Get an iterator for the events in the given range
        //todo: generalize for use with any range (events in week, month.. parameterize it)
        return events.subSet(
                new Event(dummyStart, dummyStart, "dummy"),
                new Event(dummyEnd, dummyEnd, "dummy")
        ).iterator();
    }


    /**
     * Prompt user for a date, and print that day
     */
    public void goToDay() {
        //todo: Turn this into a function, replace here and in Event constructor x2
        Date startDate = promptForDate();
        this.setTime(startDate);
        this.printDay();
    }

    /**
     * Prompts user to enter date, looping until their entry is valid
     * @return Date object
     */
    public Date promptForDate() {
        Date startDate = null;
        SimpleDateFormat sdfDate = new SimpleDateFormat("MM/dd/yyyy");
        Scanner scanner = new Scanner(System.in);
        while (startDate == null)
        {
            System.out.print("Enter date (MM/dd/yyyy): ");
            try {
                startDate = sdfDate.parse(scanner.next());
            }
            catch (ParseException e) {
                System.out.println("Invalid date entry.");
            }
        }
        return startDate;
    }

    /**
     * Prints day represented by calendar and events scheduled for that day
     */
    public void printDay() {
        String dayString = new SimpleDateFormat("EEEEEE, MMM d yyyy").format(this.getTime());
        System.out.println(dayString);
        printEventsForDay();
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

    /**
     * Checks if this calendar's date is the same as another one.
     * @param other Calendar object to compare to
     * @return true if dates are the same regardless of their times
     */
    public boolean dateEquals(Calendar other){
        return this.get(Calendar.YEAR) == other.get(Calendar.YEAR)
                && this.get(Calendar.MONTH) == other.get(Calendar.MONTH)
                && this.get(Calendar.DAY_OF_MONTH) == other.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Checks if two dates (Calendar objects) are the same without comparing their times.
     * @param today Calender object
     * @param comp Calendar object to compare to
     * @return true if dates are the same regardless of their times
     */
    public static boolean dateMatches(Calendar today, Calendar comp){
        return comp.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                && comp.get(Calendar.MONTH) == today.get(Calendar.MONTH)
                && comp.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Centers text in given space
     * @param text Text to center
     * @param width Total width of space to center text in
     * @return centered text with whitespace on both sides
     */
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

    /**
     * Deletes all events scheduled for currently set date
     * todo: Make similar function for range of dates? Not needed for this assignment
     * todo: Can cause java.util.ConcurrentModificationException - WHEN? WHY?
     */
    public void deleteEventsForEntireDay() {
        Iterator<Event> eventIterator = getEventIteratorForSingleDayOfEvents();
        Event currEvent = null;
        while (eventIterator.hasNext()) {
            currEvent = eventIterator.next();
            events.remove(currEvent);  //todo: change from loop to removeAll(eventCollection)?
        }
        System.out.println();
    }

    /**
     * Deletes a specific event
     * @param eventToDelete Event to delete
     */
    public void deleteEvent(Event eventToDelete) {
        events.remove(eventToDelete);
    }

    /**
     * Loads events from events.txt
     */
    public void loadEvents() {
        boolean success = false;
        try {
            FileInputStream fis = new FileInputStream("events.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            events = (TreeSet<Event>) ois.readObject();
            ois.close();
            fis.close();
            success = true;
        } catch (IOException e) {
            System.out.println("No saved events found.  Is this your first run?  Your events will be saved when you quit.");
        } catch (ClassNotFoundException e) {
            System.out.println("ObjectInputStream failure.  Your events file may be corrupted!");
        }
        if (success)
            System.out.println("Events loaded.");
        System.out.println();
    }

    /**
     * Saves events to events.txt
     * @return true if save was successful
     */
    public boolean saveEvents() {
        boolean success = false;
        try {
            FileOutputStream fos = new FileOutputStream("events.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(events);
            oos.close();
            fos.close();
            success = true;
        } catch (IOException e) {
            System.out.println("Failed to save events.");
        }
        System.out.println("Events saved.");
        System.out.println();
        return success;
    }

}
