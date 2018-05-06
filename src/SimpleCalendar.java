public class SimpleCalendar {
    /**
     * Shows a calendar GUI
     */
    public static void main(String[] args){
        MyCalendar model = new MyCalendar();
        SimpleCalendarView view = new SimpleCalendarView(model);  //View is also controller
    }
}
