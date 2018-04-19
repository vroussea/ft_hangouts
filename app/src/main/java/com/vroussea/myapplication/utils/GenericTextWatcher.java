package com.vroussea.myapplication.utils;

import android.text.Editable;
import android.text.TextWatcher;

import com.vroussea.myapplication.activities.ContactEdit;

public class GenericTextWatcher implements TextWatcher {
    private ContactEdit contactEdit;

    public GenericTextWatcher(ContactEdit _contactEdit) {
        this.contactEdit = _contactEdit;
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void afterTextChanged(Editable editable) {
        contactEdit.checkAllData();
    }

}
