package com.vroussea.myapplication.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.vroussea.myapplication.App;
import com.vroussea.myapplication.R;
import com.vroussea.myapplication.contact.Contact;
import com.vroussea.myapplication.contact.ContactBuilder;
import com.vroussea.myapplication.contact.ContactHelper;
import com.vroussea.myapplication.utils.BitmapToBytes;
import com.vroussea.myapplication.utils.Colors;
import com.vroussea.myapplication.utils.GenericTextWatcher;
import com.vroussea.myapplication.utils.ImagePicker;
import com.vroussea.myapplication.utils.PhoneNumberPrefix;

public class ContactEdit extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private final int PICK_IMAGE_ID = 101;
    private final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private Activity thisActivity = this;
    private ContactHelper contactHelper = new ContactHelper();
    private Contact contact;
    private ImageView picture;
    private Bitmap bitmap;

    private String firstName;
    private String lastName;
    private String nickname;
    private String phoneNumber;
    private String eMail;

    private EditText firstNameText;
    private EditText lastNameText;
    private EditText nicknameText;
    private EditText phoneNumberText;
    private EditText eMailText;

    private boolean isDataOk;
    Button submit_contact;

    private final static String phoneNumberRegex = "(\\+33|0)[1-9][0-9]{8}";
    private final static String firstnameRegex = "[A-Z][a-z]{2,15}[ -][A-Z][a-z]{2,15}|[A-Z][a-z]{2,15}";
    private final static String nameRegex = "[A-Z][a-z]{2,15}[ -][A-Z][a-z]{2,15}|[A-Z][a-z]{2,15}";
    private final static String mailRegex = "\\w+@.{1,10}\\.(com|fr|net|be|uk|org|us|gov|pro)";
    private final static String emptyRegex = ".{0}";
    private final static String orRegex = "|";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_edit);

        isDataOk = false;
        submit_contact = findViewById(R.id.submit_contact);

        picture = findViewById(R.id.picture);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_picture);
        picture.setImageBitmap(bitmap);

        initListeners();

        if (!getIntent().getBooleanExtra("isCreating", true)) {
            appendContactData();
        }

        picture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

                if (ContextCompat.checkSelfPermission(thisActivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                            Manifest.permission.READ_CONTACTS)) {
                        Toast.makeText(thisActivity, R.string.permissionsNotGranted, Toast.LENGTH_SHORT).show();
                    } else {
                        ActivityCompat.requestPermissions(thisActivity,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                } else {
                    setPicture();
                }
            }

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setPicture();
                }
                break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Colors colors = new Colors();

        getSupportActionBar().setBackgroundDrawable(colors.getActionBarColor());

        Window window = getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(ContextCompat.getColor(this, colors.getSatusBarColor()));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void onSubmitContact(View view) {
        if (isDataOk) {
            if (getIntent().getBooleanExtra("isCreating", true)) {
                onCreateContact();
            } else {
                onEditContact();
            }
            finish();
        }
    }

    private void onCreateContact() {
        Contact newContact = ContactBuilder.aContact()
                .withFirstName(firstName)
                .withLastName(lastName)
                .withNickname(nickname)
                .withPhoneNumber(PhoneNumberPrefix.removePrefix(phoneNumber))
                .withEMail(eMail)
                .withProfilePic(null)
                .withProfilePic(BitmapToBytes.getBytes(bitmap)).build();

        contactHelper.addContact(newContact);
    }

    private void onEditContact() {
        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        contact.setNickname(nickname);
        contact.setPhoneNumber(PhoneNumberPrefix.removePrefix(phoneNumber));
        contact.setEMail(eMail);
        contact.setPicture(BitmapToBytes.getBytes(bitmap));

        contactHelper.updateContact(contact);
    }

    private void appendContactData() {
        try {
            contact = contactHelper.getContactById((int) getIntent().getSerializableExtra("contactId"));
        } catch (Exception e) {
            Log.d("contactRetrieve", e.getMessage());
        }

        EditText firstName = findViewById(R.id.first_name);
        EditText lastName = findViewById(R.id.last_name);
        EditText nickname = findViewById(R.id.nickname);
        EditText phoneNumber = findViewById(R.id.phone_number);
        EditText eMail = findViewById(R.id.eMail);

        firstName.getText().clear();
        firstName.getText().append(contact.getFirstName());

        lastName.getText().clear();
        lastName.getText().append(contact.getLastName());

        nickname.getText().clear();
        nickname.getText().append(contact.getNickname());

        phoneNumber.getText().clear();
        phoneNumber.getText().append(contact.getPhoneNumber());

        eMail.getText().clear();
        eMail.getText().append(contact.getEMail());

        if (contact.getPicture() != null) {
            picture.setImageBitmap(BitmapToBytes.getImage(contact.getPicture()));
        }
    }

    public void checkAllData() {
        firstName = ((EditText) findViewById(R.id.first_name)).getText().toString();
        lastName = ((EditText) findViewById(R.id.last_name)).getText().toString();
        nickname = ((EditText) findViewById(R.id.nickname)).getText().toString();
        phoneNumber = ((EditText) findViewById(R.id.phone_number)).getText().toString();
        eMail = ((EditText) findViewById(R.id.eMail)).getText().toString();

        if (!phoneNumber.matches(phoneNumberRegex) |
                !firstName.matches(firstnameRegex) |
                !lastName.matches(nameRegex + orRegex + emptyRegex) |
                !nickname.matches(nameRegex + orRegex + emptyRegex) |
                !eMail.matches(mailRegex + orRegex + emptyRegex)) {
            submit_contact.setTextColor(Color.RED);
            isDataOk = false;
        }
        else {
            submit_contact.setTextColor(Color.BLACK);
            isDataOk = true;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_ID:
                if (resultCode != 0) {
                    bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                    picture.setImageBitmap(bitmap);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    public void setPicture() {
        startActivityForResult(ImagePicker.getPickImageIntent(App.getContext()), PICK_IMAGE_ID);
    }

    private void initListeners() {
        firstNameText = findViewById(R.id.first_name);
        lastNameText = findViewById(R.id.last_name);
        nicknameText = findViewById(R.id.nickname);
        phoneNumberText = findViewById(R.id.phone_number);
        eMailText = findViewById(R.id.eMail);

        firstNameText.addTextChangedListener(new GenericTextWatcher(this));
        lastNameText.addTextChangedListener(new GenericTextWatcher(this));
        nicknameText.addTextChangedListener(new GenericTextWatcher(this));
        phoneNumberText.addTextChangedListener(new GenericTextWatcher(this));
        eMailText.addTextChangedListener(new GenericTextWatcher(this));
    }
}
