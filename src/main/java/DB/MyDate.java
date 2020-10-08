package DB;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDate implements Comparable, Serializable {

    private int year;
    private int month;
    private int day;
    private boolean miladi;
    static String[] strMonths = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private static String[] strDays = {"Saturday", "Sunday", "Monday", "Tuesday", "Wendsday", "Thursday", "Friday"};
    static String[] monthNameShamsi = {"Farvardin", "Ordibehesht", "Khordad", "Tir", "Mordad", "Shahrivar", "Mehr", "Aban", "Azar", "Dey", "Bahman", "Esfand"};

    private static int[] daysInMonths = {31, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static int[] daysInMonthsShamsi = {31, 31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29};

    public static boolean isLeapYearMiladi(int year) {
        if (year % 400 == 0) {
            return true;
        } else if (year % 4 == 0) {
            if (year % 100 != 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLeapYearShamsi(int year) {
        double a = 0.025;
        double b = 266;
        double leapDays0, leapDays1;
        if (year > 0) {
            leapDays0 = ((year + 38) % 2820) * 0.24219 + a;
            leapDays1 = ((year + 39) % 2820) * 0.24219 + a;
        } else if (year < 0) {
            leapDays0 = ((year + 39) % 2820) * 0.24219 + a;
            leapDays1 = ((year + 40) % 2820) * 0.24219 + a;
        } else {
            return false;
        }

        double frac0 = (int) ((leapDays0 - (int) (leapDays0)) * 1000);
        double frac1 = (int) ((leapDays1 - (int) (leapDays1)) * 1000);

        if (frac0 <= b && frac1 > b) {
            return true;
        } else {
            return false;
        }
    }

    public static MyDate nowDateMiladi() {

        DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd");
        Date date = new Date();
        String[] temp = dateFormat.format(date).split(":");
        try {

            return new MyDate(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MyDate nowDateShamsi() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd");
        Date date = new Date();
        String[] temp = dateFormat.format(date).split(":");

        try {
            return convertToShamsi(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isValidDateMiladi(int year, int month, int day) {
        if (year < 1 || year > 9999) {
            return false;
        }
        if (month < 1 || month > 12) {
            return false;
        }
        if (isLeapYearMiladi(year)) {
            daysInMonths[2] = 29;
        }
        if (day >= 1) {
            if (day <= daysInMonths[month]) {
                daysInMonths[2] = 28;
                return true;
            }
        }
        return false;
    }

    public static boolean isValidDateShamsi(int year, int month, int day) {
        if (year < 1 || year > 9999) {
            return false;
        }
        if (month < 1 || month > 12) {
            return false;
        }
        if (isLeapYearShamsi(year)) {
            daysInMonthsShamsi[12] = 30;
        }
        if (day >= 1) {
            if (day <= daysInMonthsShamsi[month]) {
                daysInMonthsShamsi[12] = 29;
                return true;
            }
        }
        return false;
    }

    //Zeller�s algorithm (https://en.wikipedia.org/wiki/Determination_of_the_day_of_the_week)
    public static int getDayOfWeek(int year, int month, int day) {
        int week, y, p, q, r, s, t, u, v;
        if (month == 1 || month == 2) {
            y = year - 1;
        } else {
            y = year;
        }
        q = y / 100;
        p = y - q * 100;
        t = p / 4;
        u = q / 4;
        r = (month + 9) % 12 + 3;
        s = 13 * (r + 1) / 5;
        v = day + s + p + t + u - 2 * q + 7000;
        week = v % 7;
        return week;
    }

    public static int getDayOfWeek(MyDate date) {
        int year = date.getYear();
        int month = date.getMonth();
        int day = date.getDay();
        return getDayOfWeek(year, month, day);
    }


    public MyDate(int year, int month, int day, boolean miladi) throws IllegalArgumentException {
        if (miladi) {
            if (!isValidDateMiladi(year, month, day)) {
                throw new IllegalArgumentException("Invalid year, month, or day!");
            }
        } else if (!isValidDateShamsi(year, month, day)) {
            throw new IllegalArgumentException("Invalid year, month, or day!");
        }
        this.miladi = miladi;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public MyDate(String date,boolean miladi) {
        int year = Integer.parseInt(date.substring(0, 4).trim());
        int month = Integer.parseInt(date.substring(5, 7).trim());
        int day = Integer.parseInt(date.substring(8, 10).trim());
        if(miladi) {
            try {
                if (!isValidDateMiladi(year, month, day)) {
                    throw new IllegalArgumentException("Invalid year, month, or day!");
                }
                this.miladi = true;
                this.year = year;
                this.month = month;
                this.day = day;
            } catch (Exception e) {
                Date ndate = new Date();
                this.year = ndate.getYear();
                this.month = ndate.getMinutes();
                this.day = ndate.getDay();
            }
        }else {
            try {
                if (!isValidDateShamsi(year, month, day)) {
                    throw new IllegalArgumentException("Invalid year, month, or day!");
                }
                this.miladi = false;
                this.year = year;
                this.month = month;
                this.day = day;
            } catch (Exception e) {
                Date ndate = new Date();
                this.year = ndate.getYear();
                this.month = ndate.getMinutes();
                this.day = ndate.getDay();
            }
        }

    }

    public int getDay() {
        return day;
    }

    public static int getDayOfMonth(int month) {
        if (month >= 1 && month <= 12) {
            return daysInMonths[month];
        }
        return -1;
    }

    public void setDay(int day) throws Exception {
        if (!isValidDateMiladi(year, month, day)) {
            throw new IllegalArgumentException("Invalid year, month, or day!");
        }
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) throws Exception {
        if (!isValidDateMiladi(year, month, day)) {
            throw new IllegalArgumentException("Invalid year, month, or day!");
        }
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) throws Exception {
        if (!isValidDateMiladi(year, month, day)) {
            throw new IllegalArgumentException("Invalid year, month, or day!");
        }
        this.month = month;
    }

    public String toString() {
        return String.format("%04d/%02d/%02d", year, month, day);
    }

    public MyDate nextDay() throws Exception {
        if (isLeapYearMiladi(year)) {
            daysInMonths[2] = 29;
        }
        if (++day > daysInMonths[month]) {
            day = 1;
            nextMonth();
        }
        daysInMonths[2] = 28;
        return new MyDate(year, month, day, true);
    }

    public MyDate nextMonth() throws Exception {
        if (++month == 13) {
            month = 1;
            nextYear();
        }
        while (!isValidDateMiladi(year, month, day)) {
            nextDay();
        }
        return new MyDate(year, month, day, true);
    }

    public MyDate nextYear() throws Exception {
        year++;
        while (!isValidDateMiladi(year, month, day)) {
            nextDay();
        }
        return new MyDate(year, month, day, true);
    }

    public MyDate previousDay() throws Exception {
        if (isLeapYearMiladi(year)) {
            daysInMonths[2] = 29;
        }
        if (--day == 0) {
            day = daysInMonths[month];
            previousMonth();
        }
        daysInMonths[2] = 28;
        if (!isValidDateMiladi(year, month, day))
            return previousDay();
        return new MyDate(year, month, day, true);

    }

    public MyDate previousMonth() throws Exception {
        if (--month == 0) {
            month = 12;
            previousYear();
        }
        while (!isValidDateMiladi(year, month, day)) {
            previousDay();
        }
        return new MyDate(year, month, day, true);
    }

    public MyDate previousYear() throws Exception {
        year--;
        while (!isValidDateMiladi(year, month, day)) {
            previousDay();
        }
        return new MyDate(year, month, day, true);
    }

    public boolean equals(Object obj) {
        MyDate temp = (MyDate) obj;
        if (temp.getDay() != day) {
            return false;
        }
        if (temp.getMonth() != month) {
            return false;
        }
        if (temp.getYear() != year) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        MyDate temp = (MyDate) o;
        if (equals(o)) {
            return 0;
        }
        if (year > temp.getYear()) {
            return 1;
        } else if (year < temp.getYear()) {
            return -1;
        } else {
            if (month > temp.getMonth()) {
                return 2;
            } else if (month < temp.getMonth()) {
                return -2;
            } else {
                if (day > temp.getDay()) {
                    return 3;
                } else if (day < temp.getDay()) {
                    return -3;
                }
            }
        }
        return 0;
    }


    public static MyDate convertToMiladi(MyDate date) {
        return convertToMiladi(date.year, date.month, date.day);
    }

    public static MyDate convertToShamsi(MyDate date) {
        return convertToShamsi(date.year, date.month, date.day);
    }

    public MyDate convertToMiladi() {
        return convertToMiladi(year, month, day);
    }

    public MyDate convertToShamsi() {
        return convertToShamsi(year, month, day);
    }

    public static MyDate convertToMiladi(int year, int month, int day) {
        int y, m, d, dayCount, diff, remainDay;
        y = year + 621;
        if (isLeapYearMiladi(y)) {
            diff = 12;
        } else {
            diff = 11;
        }
        if ((month >= 1) && (month <= 6)) {
            dayCount = ((month - 1) * 31) + day;
        } else {
            dayCount = (6 * 31) + ((month - 7) * 30) + day;
        }

        if (dayCount <= diff) {
            m = 3;
            d = dayCount+20;
        } else {
            if (isLeapYearMiladi(y + 1)) {
                daysInMonths[2] = 29;
            }
            remainDay = dayCount - diff;
            int i;
            for (i = 4; remainDay > daysInMonths[i]; i++) {
                remainDay = remainDay - daysInMonths[i];
                if (i == 12) {
                    y++;
                    i = 0;
                }
            }
            m = i;
            d = remainDay;
            daysInMonths[2] = 28;
        }
        try {
            return new MyDate(y, m, d, false);
        } catch (Exception e) {
            return null;
        }
    }

    public static MyDate convertToShamsi(int year, int month, int day) {
        int nDay = day;
        int y = 0, m = 0, d = 0;
        for (int i = 1; i < month; i++)
            nDay += daysInMonths[i];
        if (nDay > 79) {
            nDay = nDay - 79;
            if (nDay <= 186) {
                m = nDay / 31;
                d = nDay % 31;
                if (d == 0) {
                    d = 31;
                } else {
                    m++;
                }
                y = year - 621;
            } else if (nDay > 186) {
                nDay -= 186;
                m += (nDay / 30);
                d = nDay % 30;
                if (d == 0) {
                    d = 30;
                } else {
                    m += 7;
                }
                y = year - 621;
            }
        } else {
            if (isLeapYearMiladi(year - 1)) {
                nDay = nDay + 11;
            } else {
                nDay = nDay + 10;
            }
            if (nDay % 30 == 0) {
                m = (nDay / 30) + 9;
                d = 30;
            } else {
                m = (nDay / 30) + 10;
                d = nDay % 30;
            }
            y = year - 622;
        }
        daysInMonths[2] = 28;
        try {
            return new MyDate(y, m, d, false);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected Object clone() {
        try {
            return new MyDate(getYear(), getMonth(), getDay(), isMiladi());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isMiladi() {
        return miladi;
    }



}
