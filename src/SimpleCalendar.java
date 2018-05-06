public class SimpleCalendar {
    /**
     * Shows a calendar GUI for MyCalendar (Based on HW2)
     */
    public static void main(String[] args){
        MyCalendar model = new MyCalendar();
        SimpleCalendarView view = new SimpleCalendarView(model);  //View is also controller
    }
}
