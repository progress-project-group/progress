package Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.progress_android.R;

import java.util.UUID;


public class SubLongTermScheduleDialog extends DialogFragment{
    private static final String ARG_ID = "sub_uuid";
    private static final String ARG_TITLE = "sub_schedule_title";
    private static final String ARG_PERCENT = "sub_schedule_percent";
    private static final String ARG_FINISHED = "sub_schedule_finished";

    public static final String EXTRA_IF_DELETE = "com.progress_android.if_delete_sub_schedule";
    public static final String EXTRA_TITLE = "com.progress_android.sub_title";
    public static final String EXTRA_PERCENT = "com.progress_android.sub_percent";
    public static final String EXTRA_ID = "com.progress_android.sub_id";
    public static final String EXTRA_FINISHED = "com.progress_android.sub_finished";


    private EditText mSubTitleEditText;
    private NumberPicker mPercentNumberPicker;
    private CheckBox mFinishedCheckBox;

    private String mTitle;
    private int mPercent;
    private UUID mUUID;
    private boolean mIsFinished;


    public static SubLongTermScheduleDialog newInstance(UUID subID, String subTitle, int subPercent, boolean finished){
        Bundle args = new Bundle();
        args.putSerializable(ARG_ID,subID);
        args.putString(ARG_TITLE,subTitle);
        args.putInt(ARG_PERCENT,subPercent);
        args.putBoolean(ARG_FINISHED,finished);

        SubLongTermScheduleDialog subLongTermScheduleDialog = new SubLongTermScheduleDialog();
        subLongTermScheduleDialog.setArguments(args);
        return subLongTermScheduleDialog;
    }

    private void sendResult(boolean deleteFlag,int resultCode){
        if(getTargetFragment()==null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_IF_DELETE,deleteFlag);
        intent.putExtra(EXTRA_TITLE, mTitle);
        intent.putExtra(EXTRA_PERCENT, mPercent);
        intent.putExtra(EXTRA_ID,mUUID);
        intent.putExtra(EXTRA_FINISHED, mIsFinished);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstances){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_long_tem_sub_schedule,null);
        mSubTitleEditText = (EditText) view.findViewById(R.id.subLongTermTitle);
        mPercentNumberPicker = (NumberPicker) view.findViewById(R.id.subLongTermPercent);
        mFinishedCheckBox = (CheckBox) view.findViewById(R.id.subLongTermFinished);

        mTitle = (String)getArguments().getString(ARG_TITLE);
        mPercent = (int)getArguments().getInt(ARG_PERCENT);
        mIsFinished = (boolean)getArguments().getBoolean(ARG_FINISHED);
        mUUID = (UUID)getArguments().getSerializable(ARG_ID);

        mSubTitleEditText.setText(mTitle);
        mPercentNumberPicker.setMaxValue(100);
        mPercentNumberPicker.setMinValue(0);
        mPercentNumberPicker.setValue(mPercent);
        mFinishedCheckBox.setChecked(mIsFinished);

        mSubTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitle = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mFinishedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIsFinished = isChecked;
            }
        });

        mPercentNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mPercent = newVal;
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.subLongTermScheduleDialog)
                .setPositiveButton(R.string.subLongTernScheduleOK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(false,Activity.RESULT_OK);
                    }
                })
                .setNeutralButton(R.string.subLongTermScheduleDelete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(true,Activity.RESULT_OK);
                    }
                })
                .create();
    }



}
