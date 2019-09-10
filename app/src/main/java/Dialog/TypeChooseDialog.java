package Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.progress_android.R;
import Item.Item;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class TypeChooseDialog extends DialogFragment {
    public RadioButton studyChoose_button;
    public RadioButton sportChoose_button;
    public RadioButton relaxChoose_button;
    public RadioButton otherChoose_button;
    public RadioGroup typeGroup;
    private Dialog dialog;
    public int position;
    public int checkedItem;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            listener = (TypeChooseListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString()+" must implement TypeChooseListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog (@Nullable Bundle savedInstanceState) {
        fragmentTag = getArguments().getInt("FragmentTag");
        position = getArguments().getInt("POSITION");
        checkedItem = getArguments().getInt("TYPE");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View DialogView = inflater.inflate(R.layout.type_choose_dialog, null);
        studyChoose_button = DialogView.findViewById(R.id.study_button);
        sportChoose_button = DialogView.findViewById(R.id.sport_button);
        relaxChoose_button = DialogView.findViewById(R.id.relax_button);
        otherChoose_button = DialogView.findViewById(R.id.other_button);
        switch(checkedItem){
            case Item.STUDY: studyChoose_button.setChecked(true); break;
            case Item.SPORT: sportChoose_button.setChecked(true); break;
            case Item.RELAX: relaxChoose_button.setChecked(true); break;
            case Item.OTHER: otherChoose_button.setChecked(true); break;

        }
        typeGroup = DialogView.findViewById(R.id.type_group);
        typeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.study_button: 
                        dialog.setTitle("学习");
                        checkedItem = Item.STUDY;
                        break;
                    case R.id.sport_button: 
                        dialog.setTitle("运动");
                        checkedItem = Item.SPORT;
                        break;
                    case R.id.relax_button: 
                        dialog.setTitle("休息");
                        checkedItem = Item.RELAX;
                        break;
                    case R.id.other_button: 
                        dialog.setTitle("其他");
                        checkedItem = Item.OTHER;
                        break;
                }
            }
        });
        dialog = builder.setView(DialogView)
                .setTitle("选择种类:")
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogPositiveClick_type(TypeChooseDialog.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TypeChooseDialog.this.getDialog().cancel();
                    }
                })
                .create();
        return dialog;
    }

    public interface TypeChooseListener{
        public void onDialogPositiveClick_type(TypeChooseDialog dialog);
        public void onDialogNegativeClick_type(TypeChooseDialog dialog);
    }

    TypeChooseListener listener;

    public int getFragmentTag() {
        return fragmentTag;
    }

    private int fragmentTag;
}
