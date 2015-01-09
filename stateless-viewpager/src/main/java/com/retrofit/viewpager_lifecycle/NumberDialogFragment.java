/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.retrofit.viewpager_lifecycle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class NumberDialogFragment extends DialogFragment {

    private static final String TAG = NumberDialogFragment.class.getSimpleName();
    private static final String KEY_TOTAL_PAGE = "NumberDialogFragment:KEY_TOTAL_PAGE";
    private static final String KEY_CURRENT_PAGE = "NumberDialogFragment:KEY_CURRENT_PAGE";

    int totalPages;
    int currentPage;

    public int mValue;

    private OnPageSelectedListener onPageSelectedListener;

    public static void show(FragmentManager fm, int currentPage, int totalPages,
                            OnPageSelectedListener onPageSelectedListener) {
        NumberDialogFragment dialogFragment = (NumberDialogFragment)
                fm.findFragmentByTag(TAG);

        if (dialogFragment == null) {
            Bundle args = new Bundle();
            args.putInt(KEY_TOTAL_PAGE, totalPages);
            args.putInt(KEY_CURRENT_PAGE, currentPage);

            dialogFragment = new NumberDialogFragment();
            dialogFragment.setArguments(args);
            dialogFragment.setPageSelectedListener(onPageSelectedListener);
            dialogFragment.show(fm, TAG);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        totalPages = args.getInt(KEY_TOTAL_PAGE);
        currentPage = args.getInt(KEY_CURRENT_PAGE);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        ViewGroup customView = (ViewGroup) LayoutInflater.from(getActivity())
                .inflate(R.layout.number_dialog_layout, (ViewGroup) getView(), false);
        final NumberPicker numberPicker = (NumberPicker)
                customView.findViewById(R.id.numberPicker);
        mValue = numberPicker.getValue();

        int inputId = getActivity().getResources().getIdentifier("numberpicker_input", "id", "android");
        final EditText editText = (EditText) numberPicker.findViewById(inputId);
        editText.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence sequence, int start, int before, int count) {
                try {
                    mValue = Integer.valueOf(String.valueOf(sequence));
                } catch (NumberFormatException ex) {
                    // swallow error
                }
            }
        });
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mValue = newVal;
            }
        });
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(totalPages);

        builder.setTitle("Select page");
        builder.setView(customView);
        builder.setCancelable(true);
        builder.setNegativeButton(android.R.string.cancel, null);

        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Selection.removeSelection(editText.getText());
                    dispatchOnPageSelected();
                }
                return false;
            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dispatchOnPageSelected();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                numberPicker.setValue(currentPage);
            }
        });

        return dialog;
    }

    private void dispatchOnPageSelected() {
        if (onPageSelectedListener != null) {
            onPageSelectedListener.onPageSelected(mValue);
        }
        dismiss();
    }

    public void setPageSelectedListener(OnPageSelectedListener onPageSelectedListener) {
        this.onPageSelectedListener = onPageSelectedListener;
    }

    public interface OnPageSelectedListener {
        void onPageSelected(int page);
    }

    private static class AbstractTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}
