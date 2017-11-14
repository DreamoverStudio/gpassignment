package comp7506.gpassignment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by Jamie on 10/11/2017.
 */

public class SpamActivity extends AppCompatActivity {
    Button btn_close;
    ImageView img_warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spam);

        btn_close = (Button) findViewById(R.id.btn_close);
        img_warning = (ImageView) findViewById(R.id.image_warn);
        new DownloadImageTask(img_warning)
                .execute("http://uat.dreamover-studio.cn/comp7506/warning.png");

        //Finish the "SpamActivity" after clicking the "Close button"
        btn_close.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                SpamActivity.this.finish();
            }
        });
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}