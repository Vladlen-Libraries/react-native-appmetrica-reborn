/*
 * Version for React Native
 * © 2020 YANDEX
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://yandex.com/legal/appmetrica_sdk_agreement/
 */

import {NativeModules, Platform} from 'react-native';

const {AppMetrica} = NativeModules;

class YandexMetrica {

  activate(config) {
    AppMetrica.activate(config);
  }

  initPush(token = ''){
    if(Platform.OS === 'android') {
      AppMetrica.initPush();
    }
    //iOS подключается нативно, отсюда не захотел работать(
  }

  getToken(){
    return AppMetrica.getToken();
  }

  reportUserProfile(config){
    AppMetrica.reportUserProfile(config);
  }

  // Android
  async getLibraryApiLevel() {
    return AppMetrica.getLibraryApiLevel();
  }

  async getLibraryVersion() {
    return AppMetrica.getLibraryVersion();
  }

  pauseSession() {
    AppMetrica.pauseSession();
  }

  reportAppOpen(deeplink) {
    AppMetrica.reportAppOpen(deeplink);
  }

  reportError(error, reason) {
    AppMetrica.reportError(error);
  }

  reportEvent(eventName, attributes) {
    AppMetrica.reportEvent(eventName, attributes);
  }

  reportReferralUrl(referralUrl) {
    AppMetrica.reportReferralUrl(referralUrl);
  }

  requestAppMetricaDeviceID(listener) {
    AppMetrica.requestAppMetricaDeviceID(listener);
  }

  resumeSession() {
    AppMetrica.resumeSession();
  }

  sendEventsBuffer() {
    AppMetrica.sendEventsBuffer();
  }

  setLocation(location) {
    AppMetrica.setLocation(location);
  }

  setLocationTracking(enabled) {
    AppMetrica.setLocationTracking(enabled);
  }

  setStatisticsSending(enabled) {
    AppMetrica.setStatisticsSending(enabled);
  }

  setUserProfileID(userProfileID) {
    AppMetrica.setUserProfileID(userProfileID);
  }
};

const yandexMetricaNext = new YandexMetrica()
export default yandexMetricaNext;
