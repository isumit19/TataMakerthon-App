package in.co.zine.com.tatamakerthon;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static in.co.zine.com.tatamakerthon.MainActivity.IP;


public class sendnow extends AsyncTask {


    private Context context;
    private String FILE2;
    //ProgressDialog progressDialog;
    sendnow(Context context,String file2){

        this.FILE2=file2;
        this.context=context;
        //progressDialog=new ProgressDialog(context,R.style.MyAlertDiallogStyle);

    }

    @Override
    protected void onPreExecute() {
        //progressDialog.setMessage("Connecting");
        //progressDialog.show();

        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {

        //progressDialog.setMessage("Successfully Sent");
        //progressDialog.dismiss();
        Toast.makeText(context,"FILE SENT",Toast.LENGTH_SHORT).show();
        super.onPostExecute(o);
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        try {

            sendimage(FILE2);



        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void sendimage(String FILE_TO_SEND) throws IOException {



        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        ServerSocket servsock = null;
        Socket sock = null;
        while (true){
            sock = new Socket(IP, 9999);

            try {


                //progressDialog.setMessage("Sending Image");
                System.out.println("Accepted connection : " + sock);
                // send file
                File myFile = new File (FILE_TO_SEND);
                byte [] mybytearray  = new byte [(int)myFile.length()];
                fis = new FileInputStream(myFile);
                bis = new BufferedInputStream(fis);
                bis.read(mybytearray,0,mybytearray.length);
                os = sock.getOutputStream();
                System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
                os.write(mybytearray,0,mybytearray.length);




                System.out.println("Done.");

                break;
            }
            finally {
                if (bis != null) bis.close();
                if (os != null) os.close();
                if (sock!=null) sock.close();
                if (servsock != null) servsock.close();
            }

        }

        }
    }
