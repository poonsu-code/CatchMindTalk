package messenger_project.sketchtalk.activity;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import messenger_project.sketchtalk.MyDatabaseOpenHelper;
import messenger_project.sketchtalk.R;

public class AddFriendActivity extends AppCompatActivity {

    Toolbar toolbar;

    Button idAddbtn;

    FrameLayout idFL;
    String idData;
    EditText idEdit;
    View noDataId;
    TextView noDataTVId;

    View friendViewId;

    ImageView profileViewId;
    TextView nameViewId;
    TextView messageViewId;
    LinearLayout sectionLinearId;
    public MyDatabaseOpenHelper db;
    public SharedPreferences mPref;
    String myId;
    ArrayList<String> friendList = new ArrayList<String>();

    String ServerURL ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        toolbar = (Toolbar) findViewById(R.id.toolbarAddFriend);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("친구추가");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db = new MyDatabaseOpenHelper(this,"catchMindTalk",null,1);
        mPref = getSharedPreferences("login",MODE_PRIVATE);
        myId = mPref.getString("userId","아이디없음");

        ServerURL = getResources().getString(R.string.ServerUrl);

        Cursor cursor = db.getFriendList();

        while(cursor.moveToNext()) {

            friendList.add(cursor.getString(0));

        }


        idEdit = (EditText) findViewById(R.id.idfindedit);

        noDataId = getLayoutInflater().inflate(R.layout.nodata,null);

        noDataTVId = (TextView)noDataId.findViewById(R.id.nodatatxt);

        friendViewId = getLayoutInflater().inflate(R.layout.friendlist_item,null);

        profileViewId = (ImageView) friendViewId.findViewById(R.id.profileImage);
        nameViewId = (TextView) friendViewId.findViewById(R.id.nickname);
        messageViewId = (TextView) friendViewId.findViewById(R.id.profileMessage);
        sectionLinearId = (LinearLayout) friendViewId.findViewById(R.id.sectionHeader);
        sectionLinearId.setVisibility(View.GONE);


        idAddbtn = (Button) findViewById(R.id.idaddbtn);

        idFL = (FrameLayout) findViewById(R.id.idFL);

    }


    public void idSearch(View v){

        FindThread ft = new FindThread(idEdit.getText().toString(),"id");
        ft.start();

        try{
            ft.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        idFL.removeAllViewsInLayout();

        if(idData.equals("아이디")){
            noDataTVId.setText("검색결과가 없습니다");
            idFL.addView(noDataTVId);
            idAddbtn.setVisibility(View.INVISIBLE);

        }else{


            try {
                JSONObject jobject = new JSONObject(idData);

                String userId = jobject.getString("friendId");
                String nickname = jobject.getString("nickname");
                String profileMessage = jobject.getString("profileMessage");
                String profileImageUpateTime = jobject.getString("profileImageUpdateTime");

                if(friendList.contains(userId)){
                    noDataTVId.setText("이미 친구입니다");
                    idFL.addView(noDataId);
                    idAddbtn.setVisibility(View.INVISIBLE);
                    return;
                }

                if(userId.equals(myId)){
                    noDataTVId.setText("본인아이디입니다");
                    idFL.addView(noDataId);
                    idAddbtn.setVisibility(View.INVISIBLE);
                    return;
                }

                nameViewId.setText(nickname);
                messageViewId.setText(profileMessage);

                Glide.with(this).load(ServerURL + "/profile_image/"+userId+".png")
                        .error(R.drawable.default_profile_image)
                        .signature(new ObjectKey(String.valueOf(profileImageUpateTime)))
                        .into(profileViewId);

                idFL.addView(friendViewId);
                idAddbtn.setVisibility(View.VISIBLE);

            }catch (JSONException e){
                e.printStackTrace();
            }

        }

    }

    public void idAddBtn(View v){

        try {
            JSONObject jobject = new JSONObject(idData);

            String userId = jobject.getString("friendId");
            String nickname = jobject.getString("nickname");
            String profileMessage = jobject.getString("profileMessage");
            String profileImageUpdateTime = jobject.getString("profileImageUpdateTime");

            if(friendList.contains(userId)){
                idFL.removeAllViewsInLayout();
                noDataTVId.setText("이미 친구입니다");
                idFL.addView(noDataTVId);
                idAddbtn.setVisibility(View.INVISIBLE);
                return;
            }

            AddThread at = new AddThread(userId);
            at.start();

            db.insertFriendList(userId, nickname, profileMessage, profileImageUpdateTime, 0,0,0);
            friendList.add(userId);

            idFL.removeAllViewsInLayout();
            idAddbtn.setVisibility(View.INVISIBLE);

        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    public class FindThread extends Thread {

        String sdata;
        String sMode;

        public FindThread(String userData,String mode) {
            this.sdata = userData;
            this.sMode = mode;
        }


        @Override
        public void run() {

            String data="";

            /* 인풋 파라메터값 생성 */
            String param = "userData="+ sdata + "&mode=" +sMode +"";
            try {
                /* 서버연결 */
                URL url = new URL(ServerURL + "/findFriend.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                InputStream is = null;
                BufferedReader in = null;

                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ( ( line = in.readLine() ) != null )
                {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();
                Log.d("FindThread Result",data.toString());

                if(sMode.equals("id")) {
                    idData = data;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    public class AddThread extends Thread {

        String sUserId;

        public AddThread(String userId) {
            this.sUserId = userId;
        }


        @Override
        public void run() {

            String data="";

            /* 인풋 파라메터값 생성 */
            String param = "userId="+ myId + "&friendId=" + sUserId ;

            try {
                /* 서버연결 */
                URL url = new URL(ServerURL + "/addFriend.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                InputStream is = null;
                BufferedReader in = null;

                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ( ( line = in.readLine() ) != null )
                {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();
                Log.d("AddThread Result",data.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
