# react-native-appmetrica-reborn

## Fork with [Ecommerce Events](https://appmetrica.yandex.ru/docs/data-collection/about-ecommerce.html) support

WARNING!!!
ver 2.0 RN68 >=
ver 1.0.17 RN67 <=

React Native bridge to the [AppMetrica](https://appmetrica.yandex.com/) on both iOS and Android.
react-native-push-next library functionality is expanded [react-native-appmetrica](https://github.com/yandexmobile/react-native-appmetrica)

## Installation

`npm install react-native-appmetrica-reborn --save`

or

`yarn add react-native-appmetrica-reborn`

## Usage

```js
import AppMetrica from "react-native-appmetrica-reborn";

// Starts the statistics collection process.
AppMetrica.activate({
  apiKey: "...KEY...",
  sessionTimeout: 120,
  firstActivationAsUpdate: true,
});

// Sends a custom event message and additional parameters (optional).
AppMetrica.reportEvent("My event");
AppMetrica.reportEvent("My event", { foo: "bar" });

// Send a custom error event.
AppMetrica.reportError("My error");

// reportUserProfile
AppMetrica.activate({
  apiKey: "...KEY...",
  sessionTimeout: 120,
  firstActivationAsUpdate: true,
});
RNAppMetrica.setUserProfileID("id");
RNAppMetrica.reportUserProfile({
  name: "Andrey Bondarenko",
  floor: "male",
  age: 34,
  isNotification: true,
});
```

# SETTING PUSH SDK

## NEXT for Android

## Create file FirebaseMessagingMasterService.java in android/app/src/main/java/com/yourappname/FirebaseMessagingMasterService.java

```js
package com.your.app;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.yandex.metrica.push.firebase.MetricaMessagingService;
import androidx.annotation.NonNull;

public class FirebaseMessagingMasterService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage message) {
    super.onMessageReceived(message);
    new MetricaMessagingService().processPush(this, message);
}

@Override
public void onNewToken(@NonNull String token) {
    super.onNewToken(token);
    new MetricaMessagingService().processToken(this, token);
}
}
```

## Add to your app/build.gradle

```js
android {
    dexOptions {
        preDexLibraries false // ADD THIS
    }
    
    defaultConfig {
        ...
        multiDexEnabled true // ADD THIS
    }
    
    dependencies {
        //ADD THIS
        implementation 'com.android.support:multidex:1.0.0'
        implementation 'com.google.gms:google-services:4.3.3'
        implementation 'com.google.firebase:firebase-messaging:22.0.0'
        implementation "com.yandex.android:mobmetricapushlib:2.2.0"
        //
    }
}

```

## Add service to your Android Manifest

```js
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
          xmlns:tools="http://schemas.android.com/tools" // <= ADD THIS
          package="com.your.app">
<application>
  //ADD THIS
  <service
    android:name=".FirebaseMessagingMasterService"
    android:enabled="true"
    android:exported="false"
  >
    <intent-filter android:priority="100">
      <action android:name="com.google.firebase.MESSAGING_EVENT" />
    </intent-filter>
  </service>
  <service
    android:name="com.yandex.metrica.push.firebase.MetricaMessagingService"
    tools:node="remove"
  />
  //
</application>
</manifest>
```

## Silent Push Notifications for Android

### Create file SilentPushReceiver.java in android/app/src/main/java/com/yourappname/SilentPushReceiver.java

```js
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.yandex.metrica.push.YandexMetricaPush;

public class SilentPushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
    // TODO: This method is called when the BroadcastReceiver is receiving
    // an Intent broadcast.
    String payload = intent.getStringExtra(YandexMetricaPush.EXTRA_PAYLOAD);

    throw new UnsupportedOperationException("Not yet implemented");
    }
}
```

## Add to Android manifest

```js
<application>
  ...
 <receiver android:name=".SilentPushReceiver">
            <intent-filter>
                <!-- Receive silent push notifications. -->
                <action android:name="${applicationId}.action.ymp.SILENT_PUSH_RECEIVE"/>
            </intent-filter>
        </receiver>
```

# NEXT for iOS

#### Follow SDK integration on https://appmetrica.yandex.com/docs/mobile-sdk-dg/ios/ios-quickstart.html
