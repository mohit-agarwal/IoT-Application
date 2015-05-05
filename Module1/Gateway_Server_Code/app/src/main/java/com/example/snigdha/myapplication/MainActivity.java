package com.example.snigdha.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;











public class MainActivity extends ActionBarActivity
{
    static public  boolean syn=false;
    static Vector<String> all_10_records=new Vector<String>();
    static Map<Integer,Vector<String>> TrafficSensor=new HashMap<Integer,Vector<String>>();
    static Map<Integer,Vector<String>> PatientSensor=new HashMap<Integer,Vector<String>>();
    static Vector<String> t = new Vector<String>();
    static Vector<String> p = new Vector<String>();

    public static BluetoothAdapter mBluetoothAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                // Device does not support Bluetooth
                Toast.makeText(getApplicationContext(),"Device does not support Bluetooth",
                        Toast.LENGTH_LONG).show();

            }
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
                Toast.makeText(getApplicationContext(),"Bluetooth Enabled",
                        Toast.LENGTH_LONG).show();
            }
            Intent discoverableIntent = new
                    Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
            startActivity(discoverableIntent);

            listenc hear = new listenc();

            hear.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            ////////////////////////
         /*   while (true) {
                sendHeartbeat_to_FS nt = new sendHeartbeat_to_FS();
                nt.execute();
            Thread.sleep(5000);
            }*/
////////////////////////////
        }
        catch (Exception e)
        {
            System.out.println("Error in ");
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public synchronized void add( ){
        this.syn =!syn;
    }

    class sendHeartbeat_to_FS extends AsyncTask<String,String,String>
    {


        @Override
        protected String doInBackground(String... params)
        {
            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;
       /*
        OutputStream os = socket.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osw);

        String number = "2";

        String sendMessage = number + "\n";
        bw.write(sendMessage);
        bw.flush();
        System.out.println("Message sent to the server : "+sendMessage);

        //Get the return message from the server
        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String message = br.readLine();
        System.out.println("Message received from the server : " +message);
      */
            try {
                String GatewayID="100";
                System.out.println("Before connection to FS sending HEARTBEAT");

                socket = new Socket("10.1.36.163",8081);
                System.out.println(" ");
                System.out.println("After connection to FS");
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF("2,"+GatewayID.trim());
                dataOutputStream.flush();
                dataOutputStream.close();
                // System.out.println("Output received" + dataInputStream.readUTF());
                socket.close();
                //  textIn.setText(dataInputStream.readUTF());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return "pass";

        }
        @Override
        protected void onPreExecute()
        {

        }
        @Override
        protected void onPostExecute(String pram)
        {

        }
        @Override
        protected void onProgressUpdate(String...dharak)
        {
            //Call Publish Progress
        }

    }

    class send_data extends AsyncTask<String,String,String>
    {
        public  String send_data1(Vector<String> record)
        {
            System.out.println("***************SEND 20 RECORDS TO FILTER SERVER******************");
            String GatewayID="100";
            String timestamp=Long.toString(new Date().getTime());
            String rec="";
            for(int i=0;i<record.size();i++)
            {
                String []temp;
                temp=record.elementAt(i).split(",");


                if(temp[1].contains("traffic"))
                {
                    System.out.println("Traffic record send");
                    rec=rec+";"+temp[0]+","+temp[1]+","+temp[2]+","+temp[3]+","+temp[4]+","+temp[5];
                }
                else
                {
                    System.out.println("Patient record send");
                    rec=rec+";"+temp[0]+","+temp[1]+","+temp[2]+","+temp[3];
                }

            }
            String outData=GatewayID+","+timestamp+rec;
            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;

            try {
                System.out.print("Connecting to Filter Server...");
                socket = new Socket("10.1.36.163", 8081);
                System.out.println("Connection Done");

                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                dataOutputStream.writeUTF("3,"+outData.trim());
                dataOutputStream.flush();
                dataOutputStream.close();
                // System.out.println("Output received" + dataInputStream.readUTF());

                socket.close();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return outData;
        }
        @Override
        protected String doInBackground(String... params)
        {
            System.out.println("record size in function iss "+MainActivity.all_10_records.size());
            String out=send_data1(MainActivity.all_10_records);
            System.out.println("-----------------------------------final data is ---------------"+out);
            MainActivity.all_10_records.clear();
            return out;
        }



        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected void onPostExecute(String pram)
        {


        }
        @Override

        protected void onProgressUpdate(String...dharak)
        {
            //Call Publish Progress



        }

    }

    class listenc extends AsyncTask<String,String,String>
    {

        private  BluetoothSocket mmSocket;
        private  InputStream mmInStream;
        private  OutputStream mmOutStream;
        private  BluetoothServerSocket mmServerSocket;
        private  final UUID uid = UUID.fromString("0000110-0000-1000-8000-00805F9B34FB");
        public  void  heartbeat()
        {
            sendHeartbeat_to_FS heart = new sendHeartbeat_to_FS();
            heart.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }


        @Override
        protected String doInBackground(String... params)
        {
            BluetoothServerSocket tmp = null;
            try {
                tmp = MainActivity.mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("abc", uid);
            } catch (IOException e) {
                System.out.println("Connected and listening...");
                e.printStackTrace();
            }
            Vector<String> store_20_records=new Vector<String>();
            mmServerSocket = tmp;
            BluetoothSocket socket = null;

            try
            {
                //  LISTEN IN WHILE LOOP TILL A SOCKET IS CONNECTED
                while (true)
                {

                    socket = mmServerSocket.accept();
                    InputStream tmpIn = null;
                    OutputStream tmpOut = null;
                    // ACCEPT CONNECTION
                    if (socket != null)
                    {

                        tmpIn = socket.getInputStream();
                        tmpOut = socket.getOutputStream();
                        mmInStream = tmpIn;
                        mmOutStream = tmpOut;
                        mmServerSocket.close();
                        int records_to_send=0;
                        int heartbeat_time=0;
                        byte[] buffer = new byte[1024];
                        int bytes;


                        while (true)
                        {
                            bytes = mmInStream.read(buffer);
                            if(heartbeat_time==8)
                            {
                                //while (syn);
                                //heartbeat();
                                //add();

                                heartbeat_time=0;
                            }
                            records_to_send++;
                            heartbeat_time++;
                            System.out.println("Info received as : " + new String(buffer).trim());
                            String data=new String(buffer).trim();
                            store_20_records.add(data);
                            if(records_to_send==6)
                            {

                                for(int f=0;f<store_20_records.size();f++)
                                {
                                    MainActivity.all_10_records.add(store_20_records.elementAt(f));
                                }

                                System.out.println("***First 20 records bundled to send***"+store_20_records.size()+"____________"+MainActivity.all_10_records.size());
                             //   while(!syn);
                                send_data snd = new send_data();
                                snd.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                add();

                                records_to_send=0;
                                store_20_records.clear();
                            }
                        }
                    }
                }
            }catch(Exception e)
            {
                System.out.println("*Exception Error*");
                e.printStackTrace();
                listenc hear = new listenc();
                hear.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
            return "done";
        }
        @Override
        protected void onPreExecute()
        {

        }
        @Override
        protected void onPostExecute(String pram)
        {

        }
        @Override
        protected void onProgressUpdate(String...dharak)
        {
            //Call Publish Progress
        }

    }

}