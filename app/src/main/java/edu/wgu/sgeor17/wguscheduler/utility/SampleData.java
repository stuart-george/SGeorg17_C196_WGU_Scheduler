package edu.wgu.sgeor17.wguscheduler.utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import edu.wgu.sgeor17.wguscheduler.model.Term;

public class SampleData {
    private static Date getDate(int diff) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_YEAR, diff);
        return cal.getTime();
    }

    public static List<Term> getTerms() {
        List<Term> terms = new ArrayList<>();
        terms.add(new Term(1, "Spring 2020", getDate(0), getDate(10)));
        terms.add(new Term(1, "Summer 2020", getDate(30), getDate(40)));
        terms.add(new Term(1, "Fall 2020", getDate(60), getDate(70)));
        return terms;
    }
}
