import firebase from 'react-native-firebase';
// Optional flow type
import type { RemoteMessage } from 'react-native-firebase';

   export default async (message: RemoteMessage) => {
   console.log('IM HERE REMOTE  ',message);

   return Promise.resolve();
} 

/* 

export default async (message) => {
    // handle your message

    console.log('IM HERE MAN BK');

    const channel = new firebase.notifications.Android.Channel('test-channel', 'Test Channel', firebase.notifications.Android.Importance.High)
    .setDescription('My apps test channel').enableLights(true).enableVibration(true).setVibrationPattern([0,1000]);

    firebase.notifications().android.createChannel(channel);

    const notification = new firebase.notifications.Notification()
    .setNotificationId(message.messageId)
    .setTitle(message.data.title)
    .setBody(message.data.body)
    .android.setChannelId('test-channel').android.vibrate([0,1000])
   // .android.setSmallIcon('ic_launcher')
    .android.setPriority(firebase.notifications.Android.Priority.High)
    .setSound('default');
  
    await firebase.notifications().displayNotification(notification);
    console.log('aquiiiiiiiiii   ',{message})
    return Promise.resolve();
  } */