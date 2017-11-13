package comp7506.gpassignment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Jamie on 10/11/2017.
 */

public class SpamActivity extends AppCompatActivity {
    Button btn_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spam);

        btn_close = (Button) findViewById(R.id.btn_close);

        //Finish the "SpamActivity" after clicking the "Close button"
        btn_close.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                SpamActivity.this.finish();
            }
        });
    }
}