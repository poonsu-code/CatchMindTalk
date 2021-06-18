package messenger_project.sketchtalk.activity;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import messenger_project.sketchtalk.R;

public class SignUpActivity extends AppCompatActivity {
    public EditText userId, password, passwordCheck, nickname;
    public TextView passwordTxt, passwordCheckTxt;
    public String sUserId, sPassword, sPasswordCheck, sNickname, checkMode;
    String ServerURL ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ServerURL = getResources().getString(R.string.ServerUrl);

        userId = (EditText) findViewById(R.id.userIdInput_signup);
        password = (EditText) findViewById(R.id.passwordInput_signup);
        passwordCheck = (EditText) findViewById(R.id.passwordInput_signup_check);
        nickname = (EditText) findViewById(R.id.nickname_signup);
        passwordTxt = (TextView) findViewById(R.id.passwordTxt);
        passwordTxt.setTextColor(Color.rgb(255, 0, 0));
        passwordCheckTxt = (TextView) findViewById(R.id.passwordCheckTxt);

        sUserId = userId.getText().toString();
        sPassword = password.getText().toString();
        sPasswordCheck = passwordCheck.getText().toString();
        sNickname = nickname.getText().toString();
        checkMode = "id";

        final TextWatcher passwordTW = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                sPassword = password.getText().toString();
                sPasswordCheck = passwordCheck.getText().toString();

                if (!Pattern.matches("^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{8,12}$", sPassword)) {
                    passwordTxt.setText(R.string.passwordCondition);
                    passwordTxt.setTextColor(Color.rgb(255, 0, 0));
                } else {
                    passwordTxt.setText(R.string.usablePassword);
                    passwordTxt.setTextColor(Color.rgb(0, 0, 255));
                }

                if (passwordCheckTxt.getText().length() > 0) {
                    if (!sPassword.equals(sPasswordCheck)) {
                        passwordCheckTxt.setText(R.string.passwordIncorrect);
                        passwordCheckTxt.setTextColor(Color.rgb(255, 0, 0));
                    } else {
                        passwordCheckTxt.setText(R.string.passwordCorrect);
                        passwordCheckTxt.setTextColor(Color.rgb(0, 0, 255));
                    }
                }

            }


        };

        password.addTextChangedListener(passwordTW);

        final TextWatcher passwordCheckTW = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                sPassword = password.getText().toString();
                sPasswordCheck = passwordCheck.getText().toString();

                if (!sPassword.equals(sPasswordCheck)) {
                    passwordCheckTxt.setText(R.string.passwordIncorrect);
                    passwordCheckTxt.setTextColor(Color.rgb(255, 0, 0));
                } else {
                    passwordCheckTxt.setText(R.string.passwordCorrect);
                    passwordCheckTxt.setTextColor(Color.rgb(0, 0, 255));
                }


            }

        };

        passwordCheck.addTextChangedListener(passwordCheckTW);

    }

    public void complete(View view) {

        /* 버튼을 눌렀을 때 동작하는 소스 */
        sUserId = userId.getText().toString();
        sPassword = password.getText().toString();
        sPasswordCheck = passwordCheck.getText().toString();
        sNickname = nickname.getText().toString();
        String sTrim = nickname.getText().toString().replace(" ", "");


        if (sUserId.equals("") || sPassword.equals("") || sPasswordCheck.equals("") || sNickname.equals("")) {
            Toast.makeText(this, R.string.allItemCheck, Toast.LENGTH_SHORT).show();
            return;
        }


        if (!sUserId.matches("^[A-Za-z]+[a-zA-Z0-9]$")) {
            Toast.makeText(this, R.string.idCondition, Toast.LENGTH_SHORT).show();
            return;
        }

        if (sUserId.length() < 4) {
            Toast.makeText(this, R.string.idLengthConditionMin, Toast.LENGTH_SHORT).show();
            return;
        }

        if (sUserId.length() > 10) {
            Toast.makeText(this, R.string.idLengthConditionMax, Toast.LENGTH_SHORT).show();
            return;
        }


        if (!sPassword.equals(sPasswordCheck)) {
            Toast.makeText(this, R.string.passwordIncorrect, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Pattern.matches("^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{8,12}$", sPassword)) {
            Toast.makeText(this, R.string.passwordCondition, Toast.LENGTH_SHORT).show();
            return;
        }


        if (!sNickname.equals(sTrim)) {
            Toast.makeText(this, R.string.noSpaceNickname, Toast.LENGTH_SHORT).show();
            return;
        }

        if (sNickname.length() > 6) {
            Toast.makeText(this, R.string.NicknameLengthMax, Toast.LENGTH_SHORT).show();
            return;
        }


        registDB rdb = new registDB();
        rdb.execute();

    }

    public class registDB extends AsyncTask<Void, Integer, String> {

        String data = "";

        @Override
        protected String doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            String param = "userId=" + sUserId + "&password=" + sPassword + "&nickname=" + sNickname + "";
            try {
                /* 서버연결 */
                URL url = new URL(
                        ServerURL + "/addUser.php");
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

                /* 서버 -> 안드로이드 파라메터값 전달 */
                InputStream is = null;
                BufferedReader in = null;


                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();
                Log.e("RECV DATA", data);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            post_submit(s);

        }

    }

    public void post_submit(String data) {

        if (data.equals("complete")) {
            Toast.makeText(this, R.string.signUpComplete, Toast.LENGTH_SHORT).show();
            finish();
        } else if (data.equals("IdExist")) {
            Toast.makeText(this, R.string.IdAlreadyExist, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.NicknameAlreadyExist, Toast.LENGTH_SHORT).show();
        }
    }

    public void cancel(View view) {
        finish();
    }

    public void idCheck(View view) {
        sUserId = userId.getText().toString();
        checkMode = "id";

        if (sUserId.equals("")) {
            Toast.makeText(this, R.string.putIdPlease, Toast.LENGTH_SHORT).show();
            return;
        }


        if (!sUserId.matches("^[A-Za-z]+[a-zA-Z0-9]$")) {
            Toast.makeText(this, R.string.idCondition, Toast.LENGTH_SHORT).show();
            return;
        }

        if (sUserId.length() < 4) {
            Toast.makeText(this, R.string.idLengthConditionMin, Toast.LENGTH_SHORT).show();
            return;
        }

        if (sUserId.length() > 10) {
            Toast.makeText(this, R.string.idLengthConditionMax, Toast.LENGTH_SHORT).show();
            return;
        }

        checkDuplication cd = new checkDuplication();
        cd.execute();

    }

    public void nicknameCheck(View view) {
        sNickname = nickname.getText().toString();
        String sTrim = nickname.getText().toString().replace(" ", "");
        checkMode = "nickname";

        if (sNickname.equals("")) {
            Toast.makeText(this, R.string.putNicknamePlease, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!sNickname.equals(sTrim)) {
            Toast.makeText(this, R.string.noSpaceNickname, Toast.LENGTH_SHORT).show();
            return;
        }

        if (sNickname.length() > 10) {
            Toast.makeText(this, R.string.NicknameLengthMax, Toast.LENGTH_SHORT).show();
            return;
        }

        checkDuplication cd = new checkDuplication();
        cd.execute();

    }

    public class checkDuplication extends AsyncTask<Void, Integer, String> {

        String data = "";
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(SignUpActivity.this);

        @Override
        protected String doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            String param = "userId=" + sUserId + "&nickname=" + sNickname + "&mode=" + checkMode;
            try {
                /* 서버연결 */
                URL url = new URL(
                        ServerURL + "/duplicationCheck.php");
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

                /* 서버 -> 안드로이드 파라메터값 전달 */
                InputStream is = null;
                BufferedReader in = null;


                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();
                Log.e("RECV DATA", data);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("1")) {
                alertBuilder
                        .setTitle("알림")
                        .setMessage(R.string.IdAlreadyExist)
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            } else if (s.equals("2")) {
                alertBuilder
                        .setTitle("알림")
                        .setMessage(R.string.usableId)
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            } else if (s.equals("3")) {
                alertBuilder
                        .setTitle("알림")
                        .setMessage(R.string.NicknameAlreadyExist)
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            } else {
                alertBuilder
                        .setTitle("알림")
                        .setMessage(R.string.usableNickname)
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }

            AlertDialog dialog = alertBuilder.create();
            dialog.show();


        }

    }

}