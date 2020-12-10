package messenger_project.catchmindtalk.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import messenger_project.catchmindtalk.R;


public class ProfileChangeActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profile_change);
        TextView PFATV = (TextView)findViewById(R.id.PickFromAlbumTV);
        TextView TPTV = (TextView)findViewById(R.id.TakePhotoTV);
        TextView DITV = (TextView)findViewById(R.id.DefaultImageTV);

        PFATV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();

                resultIntent.putExtra("IC","album");

                setResult(RESULT_OK,resultIntent);

                finish();
            }
        });

        TPTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();

                resultIntent.putExtra("IC","camera");

                setResult(RESULT_OK,resultIntent);

                finish();
            }
        });

        DITV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();

                resultIntent.putExtra("IC","default");

                setResult(RESULT_OK,resultIntent);

                finish();
            }
        });

    }
}

