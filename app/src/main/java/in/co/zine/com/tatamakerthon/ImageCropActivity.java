package in.co.zine.com.tatamakerthon;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class ImageCropActivity extends AppCompatActivity{
    ImageView compositeImageView;
    boolean crop;
    String imagepath;



    public final static int SOCKET_PORT = 8080;  // you may change this
    public static String FILE1;
    public static String FILE2;// you may change this

    @Override    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_crop_activity);




        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            crop = extras.getBoolean("crop");
            imagepath=extras.getString("path");
            FILE1=imagepath;
            Log.d("YourTag", imagepath);


        }
        int widthOfscreen = 0;
        int heightOfScreen = 0;

        DisplayMetrics dm = new DisplayMetrics();
        try {
            getWindowManager().getDefaultDisplay().getMetrics(dm);
        } catch (Exception ex) {
        }
        widthOfscreen = dm.widthPixels;
        heightOfScreen = dm.heightPixels;

        compositeImageView = findViewById(R.id.iv);


        File file=new File(imagepath);
        Bitmap bitmap2=BitmapFactory.decodeFile(file.getAbsolutePath());
       // Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),
        //        R.drawable.pk);

        Bitmap resultingImage = Bitmap.createBitmap(widthOfscreen,
                heightOfScreen, bitmap2.getConfig());

        Canvas canvas = new Canvas(resultingImage);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Path path = new Path();
        float xmin=CropView.points.get(0).x;
        float ymin=CropView.points.get(0).y;
        float xmax=CropView.points.get(0).x;
        float ymax=CropView.points.get(0).y;
        for (int i = 0; i < CropView.points.size(); i++) {

            if(CropView.points.get(i).x>xmax)
                xmax=CropView.points.get(i).x;
            if(CropView.points.get(i).x<xmin)
                xmin=CropView.points.get(i).x;
            if(CropView.points.get(i).y>ymax)
                ymax=CropView.points.get(i).y;
            if(CropView.points.get(i).y<ymin)
                ymin=CropView.points.get(i).y;

            //path.lineTo(CropView.points.get(i).x, CropView.points.get(i).y);
        }
        path.lineTo(xmin,ymin);
        path.lineTo(xmin,ymax);
        path.lineTo(xmax,ymax);
        path.lineTo(xmax,ymin);
        path.lineTo(xmin,ymin);
        canvas.drawPath(path, paint);
        if (crop) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        } else {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        }
        Rect dst=new Rect();
        dst.set(0,0,dm.widthPixels,dm.heightPixels);
        canvas.drawBitmap(bitmap2, null,dst, paint);
        compositeImageView.setImageBitmap(resultingImage);
        storeImage(resultingImage);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        switch (id)
        {
            case R.id.sub:
                try {
                    //flag=0;

                    new sendnow(this,FILE2).execute();
                   // if(flag==1)
                      //  Toast.makeText(this,"FILE SENT",Toast.LENGTH_SHORT).show();
                   // flag=0;
                    new receivenow(this,FILE2).execute();
                    //if(flag1==1)
                      //  Toast.makeText(this,"FILE RECIEVED",Toast.LENGTH_SHORT).show();




                }catch (Exception e)
                {
                    Toast.makeText(this,"FAILED",Toast.LENGTH_SHORT).show();
                    System.out.println(e.toString());
                }


        }


        return super.onOptionsItemSelected(item);


    }



    private void storeImage(Bitmap image) {
        File pictureFile = CameraUtils.getOutputMediaFile(MainActivity.MEDIA_TYPE_IMAGE);
        FILE2=pictureFile.getAbsolutePath();

        if (pictureFile == null) {

            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            CameraUtils.refreshGallery(getApplicationContext(),pictureFile.getAbsolutePath());
        } catch (IOException ignored) {

        }
    }



}