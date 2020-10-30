package com.tason.android;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;





import android.view.Window;
import android.view.WindowManager;
import com.felipecsl.gifimageview.library.GifImageView;



import org.apache.commons.io.IOUtils;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class SplashActivity extends AppCompatActivity {


    private GifImageView gifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.launch_screen);// added


       /* gifImageView = (GifImageView)findViewById(R.id.gifImageView);
        //Set GIFImageView resource


        try{
            InputStream inputStream = getAssets().open("InicioSplash.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);

            Log.d("TAMANIO ",""+bytes.length);
            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();
        }

        catch (IOException ex)
        {
            Log.d("errorsplas ",""+ex.getMessage());
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              SplashActivity.this.startActivity(new Intent(SplashActivity.this,MainActivity.class));
                SplashActivity.this.finish();
            }
        },8000); */// 3000 = 3seconds


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
