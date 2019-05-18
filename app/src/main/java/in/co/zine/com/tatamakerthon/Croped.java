package in.co.zine.com.tatamakerthon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Croped extends AppCompatActivity {



    @Override    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override    protected void onResume() {
        super.onResume();
        Intent i=getIntent();
        Bundle b=i.getExtras();
        String imagepath=b.getString("path");
        setContentView(new CropView(Croped.this,imagepath));
    }
}
