import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        DayPanel dayPanel = new DayPanel(calendarModel);

        /*/**********************************************************
         * Finalize window frame
         ***********************************************************/
        GridBagConstraints gbc = new GridBagConstraints();  //Reset everything to default
        //Add MonthPanel to window
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        frame.add(monthPanel, gbc);

        //Add DayPanel to window
        gbc.gridy = 0;
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(dayPanel, gbc);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class DayPanel extends JPanel {
    private static final int CELLS_PER_HOUR = 2;
    private ObservedCalendar selectedDate;  // todo: This should be represented by the application model
    JLabel headerLabel;
    JLabel[][] labels;

    public DayPanel(GregorianCalendar initialDate){
        this.setLayout(new GridBagLayout());
        labels = new JLabel[24][CELLS_PER_HOUR];

        //Get the date (BUT NOT TIME) from the calendar passed in
        selectedDate = new ObservedCalendar(initialDate); // todo: This should be represented by the application model
        selectedDate.attach(e -> {
            updateHeader();
            updateGrid();
        });

        //-----------------------------------------------------------
        //Create month label panel
        //-----------------------------------------------------------
        JPanel headerPanel = new JPanel(new GridBagLayout());

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
        headerPanel.add(prevMonth, gbc);
        prevMonth.addActionListener(e -> selectedDate.add(Calendar.DAY_OF_YEAR, -1));

        //Month Name + Year
        gbc.gridy = 0;
        gbc.gridx++;
        gbc.weightx = 1;
        gbc.weighty = 0;  // Shrink to minimum
        gbc.anchor = GridBagConstraints.CENTER;
        headerLabel = new JLabel();  //Initialize the label
        headerPanel.add(headerLabel, gbc);
        updateHeader();

        //Right arrow
        gbc.gridy = 0;
        gbc.gridx++;
        gbc.weightx = 0;  // Shrink to minimum
        gbc.weighty = 0;  // Shrink to minimum
        gbc.anchor = GridBagConstraints.LINE_END;
        JButton nextMonth = new JButton(" > ");
        nextMonth.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        headerPanel.add(nextMonth, gbc);
        nextMonth.addActionListener(e -> selectedDate.add(Calendar.DAY_OF_YEAR, 1));

        //-----------------------------------------------------------
        //Create month grid panel
        //-----------------------------------------------------------
        JPanel gridPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();  //reset gbc
        //Initialize labels for every grid cell
        for(int row = 0; row < 24; row++) {
            //Hour label
            String hour;
            if (row == 0) { hour = "12am"; }
            else if (row < 12) { hour = row + "am"; }
            else { hour = String.valueOf(row - 12) + "pm"; }
            JLabel hourLabel = new JLabel(hour);
            gbc.gridy = row * CELLS_PER_HOUR;
            gbc.gridx = 0;
            gbc.gridheight = CELLS_PER_HOUR;
            gbc.anchor = GridBagConstraints.FIRST_LINE_END;
            gridPanel.add(hourLabel,gbc);

            //Event label
            JPanel cellPanel = new JPanel();
            // second dimension is subdivisions within hour
            for (int sub = 0; sub < CELLS_PER_HOUR; sub++){
                labels[row][sub] = new JLabel("_" + sub, SwingConstants.CENTER);
                cellPanel.add(labels[row][sub]);
                gbc.gridy = row * CELLS_PER_HOUR + sub;
                gbc.gridx = 1;
                gbc.gridheight = 1;
                gbc.anchor = GridBagConstraints.CENTER;
                gridPanel.add(cellPanel, gbc);
            }
        }

        //Day numbers
        updateGrid();

        //-----------------------------------------------------------
        // Add components to DayPanel
        //-----------------------------------------------------------
        //Header
        gbc = new GridBagConstraints();  //Reset everything to default
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.weighty = .5;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(headerPanel, gbc);

        //Grid
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.weighty = .5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_START;
        this.add(gridPanel, gbc);
    }
    /**
     * Updates the text of the monthLabel to the current month
     */
    private void updateHeader(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE M/d");
        headerLabel.setText(sdf.format(selectedDate.getTime()));
    }

    /**
     * Updates the text of the monthGrid's labels
     */
    private void updateGrid(){
    }
}

/**
 * MonthPanel
 * Contains 2 sections
 * - top contains Month/Year with nav arrows in a gridbaglayout
 * - bottom contains month grid
 * Buttons change month view without changing the actual calendar date
 */
class MonthPanel extends JPanel {
    private ObservedCalendar displayedMonth;  // This acts as the model for this panel
    private ObservedCalendar selectedDate;  // todo: This should be represented by the application model

    final int ROWS = 7;
    final int COLS = 7;
    JLabel[][] labels;
    JLabel monthLabel;


    /**
     * Creates a Panel with view of the month
     * @param initialDate Calendar object set to initial date to be shown
     */
    public MonthPanel(GregorianCalendar initialDate){
        this.setLayout(new GridBagLayout());
        labels = new JLabel[ROWS][COLS];

        selectedDate = new ObservedCalendar(initialDate); // todo: This should be represented by the application model
        //Get the date (BUT NOT TIME) from the calendar passed in
        displayedMonth = new ObservedCalendar(initialDate);
        displayedMonth.attach(e -> {
            updateHeaderLabel();
            updateMonthGrid();
        });

        //-----------------------------------------------------------
        //Create month label panel
        //-----------------------------------------------------------
        JPanel headerPanel = new JPanel(new GridBagLayout());

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
        headerPanel.add(prevMonth, gbc);
        prevMonth.addActionListener(e -> displayedMonth.add(Calendar.MONTH, -1));

        //Month Name + Year
        gbc.gridy = 0;
        gbc.gridx++;
        gbc.weightx = 1;
        gbc.weighty = 0;  // Shrink to minimum
        gbc.anchor = GridBagConstraints.CENTER;
        monthLabel = new JLabel();
        updateHeaderLabel();
        headerPanel.add(monthLabel, gbc);

        //Right arrow
        gbc.gridy = 0;
        gbc.gridx++;
        gbc.weightx = 0;  // Shrink to minimum
        gbc.weighty = 0;  // Shrink to minimum
        gbc.anchor = GridBagConstraints.LINE_END;
        JButton nextMonth = new JButton(" > ");
        nextMonth.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        headerPanel.add(nextMonth, gbc);
        nextMonth.addActionListener(e -> displayedMonth.add(Calendar.MONTH, 1));

        //-----------------------------------------------------------
        //Create month grid panel
        //-----------------------------------------------------------
        JPanel monthGrid = new JPanel(new GridLayout(ROWS, COLS));

        //Initialize labels for every grid cell
        for(int row = 0; row < ROWS; row++) {
            for(int col = 0; col < COLS; col++) {
                //todo: Put the label in its own panel and add a little padding to fix calendar stretching slightly when border is added to current date's label
                // All text centered within labels
                labels[row][col] = new JLabel(String.valueOf(row*col), SwingConstants.CENTER);
                monthGrid.add(labels[row][col]);
            }
        }

        //Day numbers
        updateMonthGrid();

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
        this.add(headerPanel, gbc);

        //Grid
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = .5;
        gbc.weighty = 1;  // Maximize
        gbc.anchor = GridBagConstraints.PAGE_START;
        this.add(monthGrid, gbc);
    }

    /**
     * Updates the text of the monthLabel to the current month
     */
    private void updateHeaderLabel(){
        SimpleDateFormat sdf = new SimpleDateFormat("MMMMM yyyy");
        monthLabel.setText(sdf.format(displayedMonth.getTime()));
    }

    /**
     * Updates the text of the monthGrid's labels
     */
    private void updateMonthGrid(){
        //Create a temporary calendar object for parsing through month without editing our current day
        GregorianCalendar c = new GregorianCalendar();
        c.set(displayedMonth.get(Calendar.YEAR), displayedMonth.get(Calendar.MONTH), 1);
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

                //Set/remove border for selected date / other dates
                labels[row][col].setBorder(dateMatches(c, selectedDate) ?
                        BorderFactory.createLineBorder(Color.GRAY, 1)  // Border if date matches
                        : BorderFactory.createEmptyBorder()                     // No border if mismatch
                );

                //Gray out days outside of month (visual only - still should be clickable)
                labels[row][col].setForeground(monthMatches(c, displayedMonth) ? Color.BLACK :Color.GRAY);

                //increment day
                c.add(Calendar.DAY_OF_YEAR, 1);
            }
        }
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

/**
 * Decorator class for GregorianCalendar
 * Adds notifications to add() function
 */
class ObservedCalendar extends GregorianCalendar{
    private GregorianCalendar c;
    private ArrayList<ChangeListener> listeners;

    /**
     * Constructs an Observed calendar from the year/month/day of passed calendar WITHOUT TIME
     * @param cal calendar to copy date from
     */
    ObservedCalendar(GregorianCalendar cal){
        c = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        listeners = new ArrayList<>();
    }

    /**
     * Attach a listener
     * @param l the listener
     */
    public void attach(ChangeListener l)
    {
        listeners.add(l);
    }

    @Override
    public void add(int field, int amount) {
        super.add(field, amount);
        for (ChangeListener l : listeners){
            l.stateChanged(new ChangeEvent(this));
        }
    }
}