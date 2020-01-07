package a3.audientes.bluetooth;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import a3.audientes.R;
import a3.audientes.model.PopupManager;
import a3.audientes.view.activities.HearingProfile;


public class BluetoothPairingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private BluetoothAdapter bluetoothAdapter;
    private ListView lvNewDevices;
    private ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<>();
    PopupManager popupManager = PopupManager.getInstance();
    private Button connectToDevice;


    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action == null)
                return;

            if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                final String tag = "Bluetooth on/off change";

                if(state == BluetoothAdapter.STATE_TURNING_ON)
                    Log.i(tag, "turning on");
                if(state == BluetoothAdapter.STATE_ON)
                    Log.i(tag, "turned on");
                if(state == BluetoothAdapter.STATE_TURNING_OFF)
                    Log.i(tag, "turning off");
                if(state == BluetoothAdapter.STATE_OFF)
                    Log.i(tag, "turned off");
            }

            if(action.equals(BluetoothDevice.ACTION_FOUND)){
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if(device == null)
                    return;

                //Consider checking if device name is missing - we might not want those
                bluetoothDevices.add(device);
                Log.i("New device found", device.getName() + " " + device.getAddress());

                BluetoothDeviceListAdapter mDeviceListAdapter = new BluetoothDeviceListAdapter(context, R.layout.bluetooth_device_adapter_view, bluetoothDevices);
                lvNewDevices.setAdapter(mDeviceListAdapter);
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_pairing);

        lvNewDevices = findViewById(R.id.lvNewDevices);
        bluetoothDevices = new ArrayList<>();

        connectToDevice = findViewById(R.id.connectToDevice);
        connectToDevice.setOnClickListener(this);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        lvNewDevices.setOnItemClickListener(this);

        registerReceiver(broadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        registerReceiver(broadcastReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            //noBluetoothSupport();
            enableBluetooth(); // TODO: remove before submission
        }
        else if (!bluetoothAdapter.isEnabled())
            enableBluetooth();
        else {
            btnDiscover();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void btnDiscover() {
        System.out.println("Looking for bluetooth devices");

        if(bluetoothAdapter.isDiscovering())
            bluetoothAdapter.cancelDiscovery();

        int permission = PackageManager.PERMISSION_GRANTED;
        permission += checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        permission += checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permission != PackageManager.PERMISSION_GRANTED){
            String[] permissions = new String[2];
            permissions[0] = Manifest.permission.ACCESS_FINE_LOCATION;
            permissions[1] = Manifest.permission.ACCESS_COARSE_LOCATION;
            requestPermissions(permissions, 0);
        }

        bluetoothAdapter.startDiscovery();


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO: Create progress dialogue and properly wait
        bluetoothDevices.get(position).createBond();

        //Dummy wait
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Stackoverflow code snippet
        final Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        final ComponentName cn = new ComponentName("com.android.settings",
                "com.android.settings.bluetooth.BluetoothSettings");
        intent.setComponent(cn);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void enableBluetooth() {

        AlertDialog.Builder builderbluetooth = new AlertDialog.Builder(this);
        View bluetoothView = getLayoutInflater().inflate(R.layout.custom_popup_connect_bluetooth, null);
        Button buttonbluetooth =  bluetoothView.findViewById(R.id.button1);
        builderbluetooth.setView(bluetoothView);
        AlertDialog bluetoothDialog = builderbluetooth.create();

        buttonbluetooth.setOnClickListener(v12 -> {
            //bluetoothAdapter.enable();
            bluetoothDialog.dismiss();
        });
        bluetoothDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bluetoothDialog.show();
    }

    private void noBluetoothSupport() {
        AlertDialog.Builder builderbluetooth = new AlertDialog.Builder(this);
        View bluetoothView = getLayoutInflater().inflate(R.layout.custom_popup_no_bluetooth_support, null);
        builderbluetooth.setView(bluetoothView);
        AlertDialog bluetoothDialog = builderbluetooth.create();
        bluetoothDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bluetoothDialog.setCancelable(false);
        bluetoothDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == connectToDevice){
            // TODO: not sure how we pair with device, for now it redirects to hearingProfile
            startActivity(new Intent(this, HearingProfile.class));
            finish();
        }
    }

    /*
    private void connectToHearable() {
        AlertDialog.Builder builderHearable = new AlertDialog.Builder(this);
        View hearableView = getLayoutInflater().inflate(R.layout.custom_popup_connect_hearable, null);
        Button buttonHearable = hearableView.findViewById(R.id.button1);
        builderHearable.setView(hearableView);
        AlertDialog hearableDialog = builderHearable.create();

        buttonHearable.setOnClickListener(v1 -> {
            // TODO: if not possible to connect device via our app
            //   then redirect to pairing settings on phone
            Intent intent = new Intent(this, BluetoothPairingActivity.class);
            startActivity(intent);
        });
        hearableDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        hearableDialog.show();


        if(!popupManager.isHearable()){
            popupManager.setHearable(true);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    hearableDialog.show();
                }
            }, 1000 );

        }
    }
     */

}