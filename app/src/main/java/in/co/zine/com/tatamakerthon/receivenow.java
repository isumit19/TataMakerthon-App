package in.co.zine.com.tatamakerthon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static in.co.zine.com.tatamakerthon.ImageCropActivity.SOCKET_PORT;

public class receivenow extends AsyncTask {

    private static String
            FILE_TO_RECEIVED;
    private Context context;
    ProgressDialog progressDialog;

    private final static int FILE_SIZE = 6022386;

    private static int flag1=0;
    private String tagged;


    receivenow(Context context,String FILE2)
    {
        this.context=context;
        this.tagged=FILE2;
        progressDialog=new ProgressDialog(context,R.style.MyAlertDiallogStyle);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        flag1=0;
        try {

            receiveimage();



        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("Waiting for Connection to receive");
        progressDialog.show();



        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {
        progressDialog.dismiss();
        Toast.makeText(context,"FILE RECEIVED",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(context,Main2Activity.class);
        intent.putExtra("tagged",tagged);
        intent.putExtra("matched",FILE_TO_RECEIVED);
        context.startActivity(intent);
        ((Activity)context).finish();

        super.onPostExecute(o);
    }

    private void receiveimage() throws IOException {

        int c=0;



        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ServerSocket socket = null;
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                MainActivity.GALLERY_DIRECTORY_NAME);
        while(true)
        {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            //FILE_TO_RECEIVED = "tested_received"+c+".jpg";
            FILE_TO_RECEIVED=mediaStorageDir.getPath() + File.separator
                    + "matched.jpg";

            System.out.println("Waiting...");



            //System.out.println("jk"+socket.getInetAddress());
           // System.out.println(Inet4Address.getLocalHost().getHostAddress());
            try {
                //System.out.println(SOCKET_PORT);

                socket = new ServerSocket(SOCKET_PORT);
                Socket sock=socket.accept();
                System.out.println("Connecting...");
                progressDialog.setMessage("Connecting");
                // receive file
                byte [] mybytearray  = new byte [FILE_SIZE];
                InputStream is = sock.getInputStream();
                fos = new FileOutputStream(FILE_TO_RECEIVED);
                bos = new BufferedOutputStream(fos);
                bytesRead = is.read(mybytearray,0,mybytearray.length);
                current = bytesRead;

                do {
                    progressDialog.setMessage("Receiving");
                    bytesRead =
                            is.read(mybytearray, current, (mybytearray.length-current));
                    if(bytesRead >= 0) current += bytesRead;
                } while(bytesRead > -1);

                bos.write(mybytearray, 0 , current);
                bos.flush();
                System.out.println("File " + FILE_TO_RECEIVED
                        + " downloaded (" + current + " bytes read)");

                CameraUtils.refreshGallery(context,FILE_TO_RECEIVED);
                c=c+1;
                flag1=1;

               /* if(c>0) {
                    Intent intent=new Intent(context,Main2Activity.class);
                }*/
                    break;

            }
            finally {
                if (fos != null) fos.close();
                if (bos != null) bos.close();
                if (socket != null) socket.close();
            }
        }


    }
}
