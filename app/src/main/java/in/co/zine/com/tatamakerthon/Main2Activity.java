package in.co.zine.com.tatamakerthon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i=getIntent();
        Bundle b=i.getExtras();
        assert b != null;
        String tagged=b.getString("tagged");
        String matched=b.getString("matched");
        setContentView(R.layout.activity_main2);


        ImageView tag=findViewById(R.id.tagged);
        ImageView match=findViewById(R.id.matched);

        File imgFile = new File(tagged);
        File imgFile1=new File(matched);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            tag.setImageBitmap(myBitmap);

        }
        if(imgFile1.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile1.getAbsolutePath());
            match.setImageBitmap(myBitmap);

        }







    }


}
