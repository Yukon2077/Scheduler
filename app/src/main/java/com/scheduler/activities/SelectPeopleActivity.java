package com.scheduler.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scheduler.R;
import com.scheduler.adapters.PeopleAdapter;
import com.scheduler.models.People;

import java.util.ArrayList;
import java.util.List;

public class SelectPeopleActivity extends AppCompatActivity {

    public Cursor cursor;
    public List<People> contactsList = new ArrayList<>();
    RecyclerView recyclerView;
    static TextView selectedPeopleTextView;
    static PeopleAdapter peopleAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_people);

        selectedPeopleTextView = findViewById(R.id.textView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
            }
        } else {
            getContacts();
        }
    }

    @SuppressLint("Range")
    public void getContacts() {
        contactsList.clear();
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME);
        try {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                number = number.replaceAll("\\s", "");
                number = number.replaceAll("\\+91", "");
                People people = new People(name, number);
                if (!contactsList.contains(people)) {
                    contactsList.add(people);
                }
            }
        } finally {
            cursor.close();
        }
        peopleAdapter = new PeopleAdapter(contactsList);
        recyclerView.setAdapter(peopleAdapter);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContacts();
            } else {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setMessage("Read contact permission is needed to add contacts. Please go to settings and give permission.");
                alertBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        finish();
                    }
                });
                alertBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        finish();
                    }
                });
                alertBuilder.show();
            }
        }
    }

    public static void updateSelectedPeople() {
        List<People> selectedPeopleList = peopleAdapter.getSelectedPeople();
        String selectedPeopleText = "Selected people: ";
        for (int i = 0; i < selectedPeopleList.size(); i++) {
            if (i != 0 || i != selectedPeopleList.size() - 1) { selectedPeopleText += ", ";}
            selectedPeopleText += selectedPeopleList.get(i).getName();
        }
        selectedPeopleTextView.setText(selectedPeopleText);

    }


}