package Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.progress_android.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddEventDialogFragment extends DialogFragment {

    public EditText eventContent;
    String TAG = "AddEventDialogFragment";
    private Dialog dialog;

    public int getAddItemTag() {
        return addItemTag;
    }

    private int addItemTag;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        addItemTag = getArguments().getInt("AddItemTag");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View DialogView = inflater.inflate(R.layout.add_event_dialog, null);
        eventContent = DialogView.findViewById(R.id.content_editText);

        dialog = builder.setView(DialogView)
                .setTitle(R.string.addEvent)
                .setCancelable(false)   //这个函数不起作用
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG,eventContent.getText().toString());
                        if(TextUtils.isEmpty(eventContent.getText())){
                            Log.d(TAG, "内容为空");
                            eventContent.setHint("内容不可为空！");
                            eventContent.setHintTextColor(getResources().getColor(R.color.red));

                        } else {
                            setCancelable(true);
                            listener.onDialogPositiveClick_add(AddEventDialogFragment.this);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setCancelable(true);
                        AddEventDialogFragment.this.getDialog().cancel();
                    }
                })
                .create();

        return dialog;
    }

    public interface NoticeDialogListener{
        public void onDialogPositiveClick_add(AddEventDialogFragment dialog);
        public void onDialogNegativeClick_add(AddEventDialogFragment dialog);
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
