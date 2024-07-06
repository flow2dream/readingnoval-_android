package com.homework.mynoval;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.homework.mynoval.Bean.User;
import com.homework.mynoval.DataBaseManager.DataBase;

import java.util.Random;

public class Register extends AppCompatActivity implements View.OnClickListener {
    EditText userinput, firstpwd, secondpwd, verify;

    Button getcode, confirm, back;

    String verifycode = "";

    DataBase dataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    dataBase = new DataBase(this);
        initView();
    }

    private void initView() {
        userinput = findViewById(R.id.account);
        firstpwd = findViewById(R.id.firstpwd);
        secondpwd = findViewById(R.id.secondpwd);
        verify = findViewById(R.id.codeinput);

        getcode = findViewById(R.id.getCode);
        confirm = findViewById(R.id.register);
        back = findViewById(R.id.back);
        getcode.setOnClickListener(this);
        confirm.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.getCode) {
          // 获取验证码
            getVerifycode();
            ToastUtil.show(this, "验证码为:"+verifycode);
        } else if (view.getId() == R.id.register) {
            // 注册

            String account = userinput.getText().toString();

            String first = firstpwd.getText().toString();
            String second = secondpwd.getText().toString();
            String code = verify.getText().toString();

            if(account.length()>6 || account.length()<4) {
                ToastUtil.show(this, "输入用户名格式错误!!!");
                return ;
            }
            if(first.length()>10 || first.length()<4 || second.length()>10 || second.length()<4) {
                ToastUtil.show(this, "输入密码格式错误!!!");
                return ;
            }
            if(!first.equals(second)) {
                ToastUtil.show(this, "两次密码不一样！！！");
                return ;
            }
            if(!code.equals(verifycode)) {
                ToastUtil.show(this, "输入验证码有误!!!");
                return ;
            }
            User u = new User(account, first);
            if(dataBase.addUser(u)) {
                ToastUtil.show(this, "注册成功");
                finish();
            } else {
                ToastUtil.show(this, "数据库操作失败");
            }
        } else if (view.getId() == R.id.back) {
            // 返回
            finish();
        }
    }
    public void getVerifycode() {
        verifycode = "";
        Random random = new Random();                          //定义一个随机生成数技术，用来生成随机数

        for (int i = 0; i < 4; i++) {                       //循环5次每次生成一位，5位验证码
            int a = random.nextInt(3);    //验证码包括数字、大小写字母组成
            switch(a) {      //a:    0       1       2
                case 0:    //      数字   小写字母   大写字母
                    char s = (char) (random.nextInt(26) + 65);
                    verifycode = verifycode + s;
                    break;
                case 1:
                    char s1 = (char) (random.nextInt(26) + 97);
                    verifycode = verifycode + s1;
                    break;
                case 2:
                    int s2 = random.nextInt(10);
                    verifycode = verifycode + s2;
                    break;
            }
        }
    }
}