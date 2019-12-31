package Dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class StartTimeSettingDialog extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

    public int position;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        listener.onDialogPositiveClick_start(hourOfDay, minute, position);
    }

    public interface NoticeDialogListener{
        public void onDialogPositiveClick_start(int hour, int minute, int position);
        public void onDialogNegativeClick_start(int hour, int minute, int position);
    }

    NoticeDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            listener = (NoticeDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString()+" must implement NoticeDialogListener");
        }
    }
}
