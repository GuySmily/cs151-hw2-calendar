import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
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

/*/********************************************************************
 * Month Panel
 * Contains 2 sections
 * - top contains Month/Year with nav arrows in a gridbaglayout
 * - bottom contains month grid
 * Buttons change month view without changing the actual calendar date
 *********************************************************************/
class MonthPanel extends JPanel {
    GregorianCalendar cal;
    public MonthPanel(GregorianCalendar date){
        this.setLayout(new GridBagLayout());

        //Copy the calendar date parameter to our calendar
        cal = new GregorianCalendar();
        cal.setTime(date.getTime());

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
        JButton prevMonth = new JButton(" < ");
        //Trick: By setting a border the button becomes much smaller, and
        //       though we lose the 3d effect we keep the color change on click
        prevMonth.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        monthHeader.add(prevMonth, gbc);

        //Right arrow
        gbc.gridy = 0;
        gbc.gridx++;
        gbc.weightx = 0;  // Shrink to minimum
        gbc.weighty = 0;  // Shrink to minimum
        JButton nextMonth = new JButton(" > ");
        nextMonth.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        monthHeader.add(nextMonth, gbc);

        //Month Name + Year
        gbc.gridy = 0;
        gbc.gridx++;
        gbc.weightx = .5;
        gbc.weighty = 0;  // Shrink to minimum
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
        JLabel monthLabel = new JLabel(sdf.format(cal.getTime()));
        monthHeader.add(monthLabel, gbc);

        //Add monthHeader to monthPanel later

        //-----------------------------------------------------------
        //Create month grid panel
        //-----------------------------------------------------------
        JPanel monthGrid = new JPanel(new GridBagLayout());

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;  // All the width!
        gbc.weightx = .5;  // It's alone anyway
        gbc.weighty = 1;  // Take up all free space
        gbc.anchor = GridBagConstraints.PAGE_START; //Top center

        monthGrid.add(new JLabel("Month grid goes here"));



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

}