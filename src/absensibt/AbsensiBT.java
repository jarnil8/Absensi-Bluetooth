package absensibt;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

public class AbsensiBT {
    public static ArrayList<Mahasiswa> daftarHadir;
    public static final Vector<RemoteDevice> devicesDiscovered = new Vector();
    public static void main(String[] args) throws BluetoothStateException, InterruptedException {
        final Object inquiryCompletedEvent = new Object();

        devicesDiscovered.clear();
        daftarHadir = new ArrayList<>();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("D:\\daftar-mhs.tsv");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = null;
            while((line = br.readLine()) != null) {
                String[] s = line.split("\t");
                daftarHadir.add(new Mahasiswa(s[2], s[1], s[0]));
            }
        } catch (FileNotFoundException ex) {
            System.err.println("File D:\\daftar-mhs.txt not found!");
        } catch (IOException ex) {
            System.err.println("IOException");
        }
        

        DiscoveryListener listener = new DiscoveryListener() {

            @Override
            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                System.out.println("Device " + btDevice.getBluetoothAddress() + " found");
                devicesDiscovered.addElement(btDevice);
                for(Mahasiswa mhs : daftarHadir) {
                    if (mhs.getBtAddress().equals(btDevice.getBluetoothAddress()))
                        mhs.setHadir(true);
                }
                try {
                    System.out.println("     name " + btDevice.getFriendlyName(false));
                } catch (IOException cantGetDeviceName) {
                    System.err.println("Can't get Device name");
                }
            }

            @Override
            public void inquiryCompleted(int discType) {
                //System.out.println("Device Inquiry completed!");
                synchronized(inquiryCompletedEvent){
                    inquiryCompletedEvent.notifyAll();
                }
            }

            @Override
            public void serviceSearchCompleted(int transID, int respCode) {
                
            }

            @Override
            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
            }
        };

        synchronized(inquiryCompletedEvent) {
            boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
            if (started) {
                System.out.println("wait for device inquiry to complete...");
                inquiryCompletedEvent.wait();
                System.out.println(devicesDiscovered.size() +  " device(s) found");
            }
        }
        Date d = new Date();
        System.out.printf("%s %te %<tm %<tY", "\n\nHasil absensi Bluetooth", d);
        System.out.println();
        for(Mahasiswa mhs : daftarHadir) {
            System.out.print(mhs.getNRP()+"\t"+mhs.getNama()+"\t");
            if (mhs.isHadir()) System.out.println("HADIR");
            else System.out.println("TIDAK HADIR");
        }
    }
}
