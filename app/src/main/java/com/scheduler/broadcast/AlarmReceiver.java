package com.scheduler.broadcast;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scheduler.R;
import com.scheduler.database.ReminderDAO;
import com.scheduler.database.ReminderRoomDB;
import com.scheduler.models.People;
import com.scheduler.models.Reminder;

import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String CHANNEL_ID = "scheduler_notification_channel_id";

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);
        long ID = intent.getLongExtra("ID",-1);
        if (ID == -1) { return; }
        ReminderRoomDB roomDB = Room.databaseBuilder(context,ReminderRoomDB.class,"ReminderDB").allowMainThreadQueries().build();
        ReminderDAO reminderDAO = roomDB.reminderDAO();
        Reminder reminder = reminderDAO.getReminderById(ID);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(reminder.getTitle())
                .setContentText(reminder.getDescription())
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setOngoing(reminder.getAllDay())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(reminder.getId(), builder.build());
        String message = (reminder.getTitle() + "\n\n" + reminder.getDescription()).trim();
        if (message.length() >= 160) {
            message = message.substring(0, 159);
        }
        List<People> selectedPeopleList = new Gson().fromJson(reminder.getPeopleJSON(), new TypeToken<List<People>>(){}.getType());
        for (int i = 0; i < selectedPeopleList.size(); i++) {
            sendSMS(context, selectedPeopleList.get(i).getNumber(), message);
        }
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void sendSMS(Context context, String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
        } catch (Exception ex) {
            Toast.makeText(context,ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

}