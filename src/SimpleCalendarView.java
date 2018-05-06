import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class SimpleCalendarView {
    MyCalendar calendarModel;
    public SimpleCalendarView(MyCalendar calendar){
        this.calendarModel = calendar;


        final JFrame frame = new JFrame();
        frame.setSize(400, 300);
        frame.setTitle("PedrosaTech Calendar");
        frame.setLayout(new GridBagLayout());

        //Month Section
        MonthPanel monthPanel = new MonthPanel(calendarModel);

        //Day section
        //DayPanel dayPanel = new DayPanel(calendarModel);

        /*/**********************************************************
         * Finalize window frame
         ***********************************************************/
        GridBagConstraints gbc = new GridBagConstraints();  //Reset everything to default
        //Add MonthPanel to window
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = .5;
        gbc.weighty = 0;  // Shrink to minimum
        gbc.anchor = GridBagConstraints.PAGE_START;
        frame.add(monthPanel, gbc);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class DayPanel extends JPanel {
    private GregorianCalendar cal;

}

/**
 * MonthPanel
 * Contains 2 sections
 * - top contains Month/Year with nav arrows in a gridbaglayout
 * - bottom contains month grid
 * Buttons change month view without changing the actual calendar date
 */
class MonthPanel extends JPanel {
    private GregorianCalendar cal;

    final int ROWS = 7;
    final int COLS = 7;
    JLabel[][] labels = new JLabel[ROWS][COLS];

    /**
     * Creates a Panel with view of the month
     * @param initialDate Calendar object set to initial date to be shown
     */
    public MonthPanel(GregorianCalendar initialDate){
        this.setLayout(new GridBagLayout());

        //Get the date (BUT NOT TIME) from the calendar passed in
        cal = new GregorianCalendar(initialDate.get(Calendar.YEAR), initialDate.get(Calendar.MONTH), initialDate.get(Calendar.DAY_OF_MONTH));

        //-----------------------------------------------------------
        //Create month label panel
        //-----------------------------------------------------------
        JPanel monthHeader = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        //Left arrow
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0;  // Shrink to minimum
        gbc.weighty = 0;  // Shrink to minimum
        gbc.anchor = GridBagConstraints.LINE_START;
        JButton prevMonth = new JButton(" < ");
        //Trick: By setting a border the button becomes much smaller, and
        //       though we lose the 3d effect we keep the color change on click
        prevMonth.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        monthHeader.add(prevMonth, gbc);

        //Month Name + Year
        gbc.gridy = 0;
        gbc.gridx++;
        gbc.weightx = 1;
        gbc.weighty = 0;  // Shrink to minimum
        gbc.anchor = GridBagConstraints.CENTER;
        SimpleDateFormat sdf = new SimpleDateFormat("MMMMM yyyy");
        JLabel monthLabel = new JLabel(sdf.format(cal.getTime()));
        monthHeader.add(monthLabel, gbc);

        //Right arrow
        gbc.gridy = 0;
        gbc.gridx++;
        gbc.weightx = 0;  // Shrink to minimum
        gbc.weighty = 0;  // Shrink to minimum
        gbc.anchor = GridBagConstraints.LINE_END;
        JButton nextMonth = new JButton(" > ");
        nextMonth.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        monthHeader.add(nextMonth, gbc);

        //Add monthHeader to monthPanel later

        //-----------------------------------------------------------
        //Create month grid panel
        //-----------------------------------------------------------
        JPanel monthGrid = new JPanel(new GridLayout(ROWS, COLS));

        //Initialize labels for every grid cell
        for(int row = 0; row < ROWS; row++) {
            for(int col = 0; col < COLS; col++) {
                // All text centered within labels
                labels[row][col] = new JLabel(String.valueOf(row*col), SwingConstants.CENTER);
                monthGrid.add(labels[row][col]);
            }
        }

        //Day numbers
        //Create a temporary calendar object for parsing through month without editing our current day
        GregorianCalendar c = new GregorianCalendar();
        c.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
        //Rewind the calendar to start at the first day of the week (Sunday for USA)
        while (c.get(Calendar.DAY_OF_WEEK) != c.getFirstDayOfWeek()) {
            c.add(Calendar.DAY_OF_YEAR, -1);
        }
        //Set values for all the labels in the grid
        for(int row = 1; row < ROWS; row++) {
            for(int col = 0; col < COLS; col++) {
                //Also print day label in row 0
                if(row == 1){
                    labels[0][col].setText(new SimpleDateFormat("EEE").format(c.getTime()).substring(0,2));
                }
                //Set text value
                labels[row][col].setText(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));

                //Set/remove border
                labels[row][col].setBorder(dateMatches(c, cal) ?
                    BorderFactory.createLineBorder(Color.GRAY, 1)  // Border if date matches
                    : BorderFactory.createEmptyBorder()                     // No border if mismatch
                );

                //Gray out days outside of month (visual only - still should be clickable)
                labels[row][col].setForeground(monthMatches(c, cal) ? Color.BLACK :Color.GRAY);

                //increment day
                c.add(Calendar.DAY_OF_YEAR, 1);
            }
        }

        //-----------------------------------------------------------
        // Add components to MonthPanel
        //-----------------------------------------------------------
        //Header
        gbc = new GridBagConstraints();  //Reset everything to default
        //Add MonthHeader to window
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = .5;
        gbc.weighty = 0;  // Shrink to minimum
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(monthHeader, gbc);

        //Grid
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = .5;
        gbc.weighty = 1;  // Maximize
        gbc.anchor = GridBagConstraints.PAGE_START;
        this.add(monthGrid, gbc);
    }

    /**
     * Checks if two dates (Calendar objects) are the same without comparing their times.
     * @param today Calender object
     * @param comp Calendar object to compare to
     * @return true if dates are the same regardless of their times
     */
    private static boolean dateMatches(Calendar today, Calendar comp){
        return comp.get(Calendar.YEAR) == today.get(Calendar.YEAR)
            && comp.get(Calendar.MONTH) == today.get(Calendar.MONTH)
            && comp.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Checks if two Calendar objects' months are the same
     * @param today Calender object
     * @param comp Calendar object to compare to
     * @return true if both calendars are set to the same month
     */
    private static boolean monthMatches(Calendar today, Calendar comp){
        return comp.get(Calendar.MONTH) == today.get(Calendar.MONTH);
    }
}