import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class Event {
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
        System.out.println("Please enter event details.");
        start = new GregorianCalendar();

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
                startDate = sdfDate.parse(scanner.next());
            }
            catch (ParseException e) {
                System.out.println("Invalid date entry.");
            }
        }
        start.setTime(startDate);
        end.setTime(startDate);  // Only same-day events supported

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
        start.setTime(startTime);

        // ------ Compile date + time into Calendar object ------
        //todo?????????

        // ------ End time ------
        Date endTime = null;
        while (endTime == null)
        {
            System.out.print("End time (HH:mm, on same day, optional): ");
            try {
                endTime = sdfDate.parse(scanner.next());
            }
            catch (ParseException e) {
                System.out.println("Invalid time entry.");
            }
        }
        end.setTime(endTime);

    }

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
