package ec.redcode.tas.on.android.activities.custom;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import ec.redcode.tas.on.android.R;

public class CustomDateTimeDialog extends AlertDialog implements DialogInterface.OnClickListener {

    private View.OnClickListener mClickListener;
    private TextView data;

    private String formatDate;

    private SimpleDateFormat dateFormat;

    private Calendar defaultDate;

    public CustomDateTimeDialog(Context context, View.OnClickListener onClickListener, Date defaultDate) {
        super(context);

        this.mClickListener = onClickListener;
        this.formatDate = context.getString(R.string.defaultDateTime);

        if (defaultDate != null) {
            this.defaultDate = Calendar.getInstance();
            this.defaultDate.setTime(defaultDate);
        }

        this.load();
    }

    public CustomDateTimeDialog(Context context, View.OnClickListener onClickListener, Date defaultDate, String formatDate) {
        super(context);

        this.mClickListener = onClickListener;
        this.formatDate = formatDate;

        if (defaultDate != null) {
            this.defaultDate = Calendar.getInstance();
            this.defaultDate.setTime(defaultDate);
        }


        this.load();
    }


    @SuppressLint("NewApi")
    protected void load() {
        dateFormat = new SimpleDateFormat(this.formatDate, new Locale("es", "EC"));

        final View dialogView = View.inflate(getContext(), R.layout.date_time_common, null);
        final DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
        final TimePicker timePicker = dialogView.findViewById(R.id.time_picker);

        if (defaultDate != null) {
            datePicker.updateDate(defaultDate.get(Calendar.YEAR), defaultDate.get(Calendar.MONTH), defaultDate.get(Calendar.DAY_OF_MONTH));
            try {
                timePicker.setHour(defaultDate.get(Calendar.HOUR));
                timePicker.setMinute(defaultDate.get(Calendar.MINUTE));
            } catch (NoSuchMethodError error) {
            }
        }
        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());
                data = new TextView(getContext());

                data.setText(dateFormat.format(calendar.getTime()));

                if (mClickListener != null) {
                    mClickListener.onClick(data);
                }
                dismiss();
            }
        });
        this.setView(dialogView);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                if (mClickListener != null) {
                    mClickListener.onClick(data);
                }
                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }
    }
}
