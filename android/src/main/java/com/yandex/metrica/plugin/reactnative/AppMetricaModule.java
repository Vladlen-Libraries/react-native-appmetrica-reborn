/*
 * Version for React Native
 * © 2020 YANDEX
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://yandex.com/legal/appmetrica_sdk_agreement/
 */

package com.yandex.metrica.plugin.reactnative;

import android.app.Activity;
import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.push.YandexMetricaPush;
import com.yandex.metrica.ecommerce.ECommerceAmount;
import com.yandex.metrica.ecommerce.ECommerceCartItem;
import com.yandex.metrica.ecommerce.ECommerceEvent;
import com.yandex.metrica.ecommerce.ECommerceOrder;
import com.yandex.metrica.ecommerce.ECommercePrice;
import com.yandex.metrica.ecommerce.ECommerceProduct;
import com.yandex.metrica.ecommerce.ECommerceReferrer;
import com.yandex.metrica.ecommerce.ECommerceScreen;

import java.util.ArrayList;
import java.lang.*;
import java.util.List;


public class AppMetricaModule extends ReactContextBaseJavaModule {

    private static final String TAG = "AppMetricaModule";

    private final ReactApplicationContext reactContext;

    public AppMetricaModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "AppMetrica";
    }

    @ReactMethod
    public void activate(ReadableMap configMap) {
        YandexMetrica.activate(reactContext, Utils.toYandexMetricaConfig(configMap));
        enableActivityAutoTracking();
    }

    private void enableActivityAutoTracking() {
        Activity activity = getCurrentActivity();
        if (activity != null) { // TODO: check
            YandexMetrica.enableActivityAutoTracking(activity.getApplication());
        } else {
            Log.w(TAG, "Activity is not attached");
        }
    }

    @ReactMethod
    public void initPush() {
        YandexMetricaPush.init(reactContext);
    }

    @ReactMethod
    public void getToken(Promise promise) {
        promise.resolve(YandexMetricaPush.getToken());
    }

    @ReactMethod
    public void reportUserProfile(ReadableMap configAttributes) {
        YandexMetrica.reportUserProfile(Utils.toYandexProfileConfig(configAttributes));
    }

    @ReactMethod
    public void getLibraryApiLevel(Promise promise) {
        promise.resolve(YandexMetrica.getLibraryApiLevel());
    }

    @ReactMethod
    public void getLibraryVersion(Promise promise) {
        promise.resolve(YandexMetrica.getLibraryVersion());
    }

    @ReactMethod
    public void pauseSession() {
        YandexMetrica.pauseSession(getCurrentActivity());
    }

    @ReactMethod
    public void reportAppOpen(String deeplink) {
        YandexMetrica.reportAppOpen(deeplink);
    }

    @ReactMethod
    public void reportError(String message) {
        try {
            Integer.valueOf("00xffWr0ng");
        } catch (Throwable error) {
            YandexMetrica.reportError(message, error);
        }
    }

    @ReactMethod
    public void reportEvent(String eventName, ReadableMap attributes) {
        if (attributes == null) {
            YandexMetrica.reportEvent(eventName);
        } else {
            YandexMetrica.reportEvent(eventName, attributes.toHashMap());
        }
    }

    @ReactMethod
    public void reportReferralUrl(String referralUrl) {
        YandexMetrica.reportReferralUrl(referralUrl);
    }

    @ReactMethod
    public void requestAppMetricaDeviceID(Callback listener) {
        YandexMetrica.requestAppMetricaDeviceID(new ReactNativeAppMetricaDeviceIDListener(listener));
    }

    @ReactMethod
    public void resumeSession() {
        YandexMetrica.resumeSession(getCurrentActivity());
    }

    @ReactMethod
    public void sendEventsBuffer() {
        YandexMetrica.sendEventsBuffer();
    }

    @ReactMethod
    public void setLocation(ReadableMap locationMap) {
        YandexMetrica.setLocation(Utils.toLocation(locationMap));
    }

    @ReactMethod
    public void setLocationTracking(boolean enabled) {
        YandexMetrica.setLocationTracking(enabled);
    }

    @ReactMethod
    public void setStatisticsSending(boolean enabled) {
        YandexMetrica.setStatisticsSending(reactContext, enabled);
    }

    @ReactMethod
    public void setUserProfileID(String userProfileID) {
        YandexMetrica.setUserProfileID(userProfileID);
    }

    public ECommerceScreen createScreen(ReadableMap params, List<String> categories) {
        return new ECommerceScreen().setName(params.getString("screenName")).setSearchQuery(params.getString("searchQuery")).setCategoriesPath(categories);
    }
    public ECommerceScreen createScreen(ReadableMap params) {
        return new ECommerceScreen().setName(params.getString("screenName")).setSearchQuery(params.getString("searchQuery"));
    }

    public ECommerceProduct createProduct(ReadableMap params) {
        ECommercePrice actualPrice = new ECommercePrice(new ECommerceAmount(Integer.parseInt(params.getString("price")), params.getString("currency")));
        return new ECommerceProduct(params.getString("sku")).setActualPrice(actualPrice).setName(params.getString("name"));
    }

    public ECommerceCartItem createCartItem(ReadableMap params) {
        ECommerceScreen screen = this.createScreen(params);
        ECommerceProduct product = this.createProduct(params);
        ECommercePrice actualPrice = new ECommercePrice(new ECommerceAmount(Integer.parseInt(params.getString("price")), params.getString("currency")));
        ECommerceReferrer referrer = new ECommerceReferrer().setScreen(screen);
        return new ECommerceCartItem(product, actualPrice, Integer.parseInt(params.getString("quantity"))).setReferrer(referrer);
    }

    @ReactMethod
    public void showScreen(ReadableMap params) {
        ECommerceScreen screen = this.createScreen(params);
        ECommerceEvent showScreenEvent = ECommerceEvent.showScreenEvent(screen);
        YandexMetrica.reportECommerce(showScreenEvent);
    }
    @ReactMethod
    public void showScreenWithCategories(ReadableMap params, ReadableArray categories) {
        List<String> _categories = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            _categories.add(categories.getString(i));
        }
        ECommerceScreen screen = this.createScreen(params, _categories);
        ECommerceEvent showScreenEvent = ECommerceEvent.showScreenEvent(screen);
        YandexMetrica.reportECommerce(showScreenEvent);
    }

    @ReactMethod
    public void showProductCard(ReadableMap params) {
        ECommerceScreen screen = this.createScreen(params);
        ECommerceProduct product = this.createProduct(params);
        ECommerceEvent showProductCardEvent = ECommerceEvent.showProductCardEvent(product, screen);
        YandexMetrica.reportECommerce(showProductCardEvent);
    }

    @ReactMethod
    public void addToCart(ReadableMap params) {
        ECommerceCartItem cartItem = this.createCartItem(params);
        ECommerceEvent addCartItemEvent = ECommerceEvent.addCartItemEvent(cartItem);
        YandexMetrica.reportECommerce(addCartItemEvent);
    }

    @ReactMethod
    public void removeFromCart(ReadableMap params) {
        ECommerceCartItem cartItem = this.createCartItem(params);
        ECommerceEvent removeCartItemEvent = ECommerceEvent.removeCartItemEvent(cartItem);
        YandexMetrica.reportECommerce(removeCartItemEvent);
    }

    @ReactMethod
    public void beginCheckout(ReadableArray products, String identifier) {
        ArrayList<ECommerceCartItem> cartItems = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            ReadableMap productData = products.getMap(i);
            cartItems.add(this.createCartItem(productData));
        }
        ECommerceOrder order = new ECommerceOrder(identifier, cartItems);
        ECommerceEvent beginCheckoutEvent = ECommerceEvent.beginCheckoutEvent(order);
        YandexMetrica.reportECommerce(beginCheckoutEvent);
    }

    @ReactMethod
    public void finishCheckout(ReadableArray products, String identifier) {
        ArrayList<ECommerceCartItem> cartItems = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            ReadableMap productData = products.getMap(i);
            cartItems.add(this.createCartItem(productData));
        }
        ECommerceOrder order = new ECommerceOrder(identifier, cartItems);
        ECommerceEvent purchaseEvent = ECommerceEvent.purchaseEvent(order);
        YandexMetrica.reportECommerce(purchaseEvent);
    }
}
