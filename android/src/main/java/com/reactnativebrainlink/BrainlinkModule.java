package com.reactnativebrainlink;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactContext;

import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.boby.bluetoothconnect.LinkManager;
import com.boby.bluetoothconnect.bean.BrainWave;
import com.boby.bluetoothconnect.bean.Gravity;
import com.boby.bluetoothconnect.classic.bean.BlueConnectDevice;
import com.boby.bluetoothconnect.classic.listener.EEGPowerDataListener;
import com.boby.bluetoothconnect.classic.listener.OnConnectListener;

@ReactModule(name = BrainlinkModule.NAME)
public class BrainlinkModule extends ReactContextBaseJavaModule {
  public static final String NAME = "Brainlink";
  private static ReactApplicationContext reactContext;

  private BluetoothAdapter mBluetoothAdapter;
  private String connection = "Waiting!";
  private String devices = "BrainLink,BrainLink_Lite,BrainLink_Pro,Brainlink_Pro";
  private OnConnectListener onConnectListener;
  private EEGPowerDataListener eegPowerDataListener;
  private LinkManager bluemanage = null;
  private boolean isScanStarted = false;

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  public BrainlinkModule(ReactApplicationContext context) {
    super(context);
    reactContext = context;

    Timer timer = new Timer();
    TimerTask tt = new TimerTask() {
      public void run() {
        WritableMap params = Arguments.createMap();
        params.putString("state", connection);
        sendEvent(reactContext, "Connection", params);
      }
    };
    timer.schedule(tt, 1000, 2000);

  }

  private void sendEvent(ReactContext reactContext,
                         String eventName,
                         @Nullable Object params) {
    reactContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit(eventName, params);
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    return constants;
  }


  private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();
      // When discovery finds a device
      if (BluetoothDevice.ACTION_FOUND.equals(action)) {
        // Get the BluetoothDevice object from the Intent
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        // update to UI
        WritableMap params = Arguments.createMap();

        params.putString("name", device.getName());
        params.putString("address", device.getAddress());
        sendEvent(reactContext, "addBluetoothDevice", params);

      }
    }
  };

  @ReactMethod
  public void isBluetoothOn(Callback callback) {
    try {
      mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
      if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
        callback.invoke(false);
      } else {
        callback.invoke(true);

      }
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }

  @ReactMethod
  public void scanBluetoothDevices() {

    if (mBluetoothAdapter == null) {
      mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    if (mBluetoothAdapter.isDiscovering()) {
      mBluetoothAdapter.cancelDiscovery();
    }

    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
    WritableArray writeAbleArray = Arguments.createArray();
    for (BluetoothDevice device : pairedDevices) {
      WritableMap deviceMap = Arguments.createMap();

      deviceMap.putString("name", device.getName());
      deviceMap.putString("address", device.getAddress());
      writeAbleArray.pushMap(deviceMap);

    }
    sendEvent(reactContext, "bluetoothDeviceList", writeAbleArray);

    //register the receiver for scanning
    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    reactContext.registerReceiver(mReceiver, filter);

    mBluetoothAdapter.startDiscovery();
  }

  @ReactMethod
    public void setDevice(String device) {
    if (device != "") {
      devices = device;
    }
  }

  @ReactMethod
  public void start() {
    if (!isScanStarted) {
      initBlueManager();
    }
  }

  @ReactMethod
  public void stop() {
    if (bluemanage != null) {
      bluemanage.close();
    }
  }

  public void initBlueManager() {

    onConnectListener = new OnConnectListener() {

      @Override
      public void onConnectionLost(BlueConnectDevice blueConnectDevice) {
        final String mac = blueConnectDevice.getAddress();
        connection = "Connection Lost!";
      }

      @Override
      public void onConnectStart(BlueConnectDevice blueConnectDevice) {
        connection = "Waiting!";
      }

      @Override
      public void onConnectting(BlueConnectDevice blueConnectDevice) {
        connection = "Connecting...";
      }

      @Override
      public void onConnectFailed(BlueConnectDevice blueConnectDevice) {
        connection = "Failed!";
      }

      @Override
      public void onConnectSuccess(final BlueConnectDevice blueConnectDevice) {
        connection = "Connected!";
      }

      @Override
      public void onError(Exception e) {
        e.printStackTrace();
        connection = "Error!";
      }
    };


    eegPowerDataListener = new EEGPowerDataListener() {

      @Override
      public void onBrainWavedata(final String mac, final BrainWave power) {
        connection = "Connected!";

        WritableMap params = Arguments.createMap();
        params.putString("Connection", connection);
        params.putInt("signal", power.signal);
        params.putInt("att", power.att);
        params.putInt("med", power.med);
        params.putInt("ap", power.ap);
        params.putInt("batteryCapacity", power.batteryCapacity);
        params.putInt("hardwareVersion", power.hardwareversion);
        params.putInt("grind", power.grind);


        params.putInt("delta", power.delta);
        params.putInt("theta", power.theta);
        params.putInt("lowAlpha", power.lowAlpha);
        params.putInt("highAlpha", power.highAlpha);
        params.putInt("lowBeta", power.lowBeta);
        params.putInt("highBeta", power.highBeta);
        params.putInt("lowGamma", power.lowGamma);
        params.putInt("middleGamma", power.middleGamma);
        sendEvent(reactContext, "BRAIN_WAVE", params);

      }

      @Override
      public void onRawData(String mac, int raw) {
        WritableMap params = Arguments.createMap();
        params.putInt("data", raw);
        sendEvent(reactContext, "RAW", params);
      }

      @Override
      public void onGravity(String mac, Gravity gravity) {
        WritableMap params = Arguments.createMap();

        params.putInt("x", gravity.X);
        params.putInt("y", gravity.Y);
        params.putInt("z", gravity.Z);
        sendEvent(reactContext, "GRAVITY", params);

      }

    };

    bluemanage = LinkManager.init(reactContext);
    bluemanage.setEegPowerDataListener(eegPowerDataListener);
    bluemanage.setOnConnectListener(onConnectListener);
    bluemanage.setDebug(true);
    startScan();
  }

  void startScan() {

    bluemanage.setMaxConnectSize(1);
    bluemanage.setConnectType(LinkManager.ConnectType.ALLDEVICE);
    bluemanage.setWhiteList(devices);
    bluemanage.startScan();
    isScanStarted = true;
  }
}
