package com.homework.mynoval;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.homework.mynoval.Bean.User;
import com.homework.mynoval.DataBaseManager.DataBase;

public class Login extends AppCompatActivity implements View.OnClickListener {


    TextView register, forepwd;
    EditText user, pwd;
    CheckBox rempwd, autologin;
    Button login;
    SharedPreferences share;

    DataBase dataBase;
    MyApplication app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        share = getSharedPreferences("share", MODE_PRIVATE);
        dataBase = new DataBase(this);
        app = MyApplication.getInstance();
        intiView();
        Log.d("TTT", "onClick: 123");
    }

    private void intiView() {
        register = findViewById(R.id.register);
        forepwd = findViewById(R.id.forgetbox);
        user = findViewById(R.id.userinput);
        pwd = findViewById(R.id.pwdinput);
        rempwd = findViewById(R.id.rempwd);
        autologin = findViewById(R.id.autologin);
        login = findViewById(R.id.login);

        user.setText(share.getString("account", ""));

        if (share.getBoolean("remem_pwd", false)){
            rempwd.setChecked(true);
            pwd.setText(share.getString("password", ""));
        }
        if (share.getBoolean("autologin", false)){
            autologin.setChecked(true);
            Intent intent = new Intent(this, Home.class);
            String u = share.getString("account", "");
            app.setUser_id(u);
            startActivity(intent);
        }
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forepwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.login) {
            // 登录
            String account = user.getText().toString();
            String password = pwd.getText().toString();
            if(account.equals("") || password.equals("")){
                ToastUtil.show(this, "输入的账号或密码为空");
                return ;
            }
            User u = new User(account, password);
            if(dataBase.isRight(u)) {
                app.setUser_id(u.getAccount());
                SharedPreferences.Editor editor = share.edit();
                if (rempwd.isChecked()) {
                    editor.putBoolean("remem_pwd", true);
                } else {
                    editor.putBoolean("remem_pwd", false);
                }
                if(autologin.isChecked()) {
                    editor.putBoolean("autologin", true);
                } else {
                    editor.putBoolean("autologin", false);
                }
                editor.putString("account", u.getAccount());
                editor.putString("password", u.getPassword());
                editor.apply();
                Intent intent = new Intent(this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                ToastUtil.show(this, "账号或密码错误!!!");
            }
        } else if (view.getId() == R.id.forgetbox) {
            // 忘记密码
            startActivity(new Intent(this, ForgetPassword.class));
        } else if (view.getId() == R.id.register) {
            // 注册
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        }
    }
}