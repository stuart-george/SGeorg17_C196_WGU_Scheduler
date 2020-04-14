package edu.wgu.sgeor17.wguscheduler.utility;

import java.text.SimpleDateFormat;

import edu.wgu.sgeor17.wguscheduler.R;
import edu.wgu.sgeor17.wguscheduler.ui.main.MainActivity;

public class TextFormatting {
    public static SimpleDateFormat shortDateFormat = new SimpleDateFormat(MainActivity.getContext().getString(R.string.date_format_short));
    public static SimpleDateFormat mediumDateFormat = new SimpleDateFormat(MainActivity.getContext().getString(R.string.date_format_medium));
    public static SimpleDateFormat longDateFormat = new SimpleDateFormat(MainActivity.getContext().getString(R.string.date_format_long));

}
