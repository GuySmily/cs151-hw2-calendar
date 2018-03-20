import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

/**
 * An event with title, start date/time (Calendar), and end date/time (Calendar)
 */
public class Event implements Comparable<Event>, Serializable {
    String title;
    Calendar start;
    Calendar end;

    /**
     * Event constructor without prompts
     * @param startDate
     * @param endDate
     * @param title
     */
    public Event(Calendar startDate, Calendar endDate, String title){
        this.start = startDate;
        this.end = endDate;
        this.title = title;
    }

    /**
     * Event constructor - with prompts for title and start/end dates/times
     */
    public Event() {
        start = new GregorianCalendar();
        end = new GregorianCalendar();

        System.out.println("Please enter event details.");

        Scanner scanner = new Scanner(System.in);

        // ------ Title ------
        System.out.print("Title: ");
        this.title = scanner.next();

        // ------ Start date ------
        Date startDate = null;
        SimpleDateFormat sdfDate = new SimpleDateFormat("MM/dd/yyyy");
        while (startDate == null)
        {
            System.out.print("Start date (MM/dd/yyyy): ");
            try {
                startDate = sdfDate.parse(scanner.nextLine());
            }
            catch (ParseException e) {
                System.out.println("Invalid date entry.");
            }
        }
        start.setTime(startDate);
        end.setTime(startDate);  // Send end date = start date (Only same-day events supported)

        // ------ Start time ------
        Date startTime = null;
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");  // Note: reused for end time
        while (startTime == null)
        {
            System.out.print("Start time (HH:mm): ");
            try {
                startTime = sdfTime.parse(scanner.next());
            }
            catch (ParseException e) {
                System.out.println("Invalid time entry.");
            }
        }
        // Date methods are deprecated, so create a temporary calendar and do setTime() and get()
        Calendar temp = Calendar.getInstance();
        temp.setTime(startTime);
        start.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
        start.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));

        // ------ End time ------
        Date endTime = null;
        while (endTime == null)
        {
            System.out.print("End time (HH:mm, on same day,  Type same time as start for no end time): ");
            try {
                endTime = sdfTime.parse(scanner.next());
            }
            catch (ParseException e) {
                System.out.println("Invalid time entry.");
            }
        }
        temp.setTime(endTime);
        end.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
        end.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
    }

    /**
     * Compares events by their start times.
     * @param otherEvent Event to compare to.
     * @return 0 if start date/time are the same.
     *         Negative if this event starts first.
     *         Positive if other event starts first.
     */
    @Override
    public int compareTo(Event otherEvent) {
        //todo: Should we just say otherEvent.start?
        return this.start.compareTo(otherEvent.getStart());
    }

    /**
     * @return Event date/times and text, WITHOUT YEAR
     */
    @Override
    public String toString() {
        SimpleDateFormat sdfStart = new SimpleDateFormat("EEE MMM dd HH:mm");
        SimpleDateFormat sdfEnd = new SimpleDateFormat("HH:mm");
        //return sdfStart.format(start.getTime()) + " - " + sdfEnd.format(end.getTime()) + " " + title;
        return sdfStart.format(start.getTime())
                + (start.getTime().equals(end.getTime()) ? "" : " - " + sdfEnd.format(end.getTime()))
                + " " + title;
    }
    /**
     * @param printDate whether to include the date as well, or only the time
     * @return String: start date if opted + start time + end time + title
     */
    public String toString(boolean printDate) {
        SimpleDateFormat sdfStart = new SimpleDateFormat(printDate ? "EEE MMM dd " : "" + "HH:mm");
        SimpleDateFormat sdfEnd = new SimpleDateFormat("HH:mm");

        return sdfStart.format(start.getTime())
                + (start.getTime().equals(end.getTime()) ? "" : " - " + sdfEnd.format(end.getTime()))
                + " " + title;
    }

    // ------ Accessors/Mutators ------
    public void setStart(Calendar start) {
        this.start = start;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Calendar getStart() {
        return start;
    }

    public Calendar getEnd() {
        return end;
    }

    public String getTitle() {
        return title;
    }
}
