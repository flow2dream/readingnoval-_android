package com.homework.mynoval;

import android.os.Bundle;
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

public class ForgetPassword extends AppCompatActivity implements View.OnClickListener {
    EditText userinput, firstpwd, secondpwd, verify;

    Button getcode, confirm, back;

    String verifycode = "";

    DataBase dataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dataBase = new DataBase(this);
        initView();
    }

    private void initView() {
        userinput = findViewById(R.id.username);
        firstpwd = findViewById(R.id.firstPWD);
        secondpwd = findViewById(R.id.secondPWD);
        verify = findViewById(R.id.code);

        getcode = findViewById(R.id.gaincode);
        confirm = findViewById(R.id.confirm);
        back = findViewById(R.id.backLogin);
        getcode.setOnClickListener(this);
        confirm.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.gaincode) {
            // 获取验证码
            getVerifycode();
            ToastUtil.show(this, "验证码为:"+verifycode);
        } else if (view.getId() == R.id.confirm) {
            // 确定
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

            if(dataBase.updatePassword(account, first)) {
                ToastUtil.show(this, "密码修改成功");
                finish();
            } else {
                ToastUtil.show(this, "密码修改失败");
            }
        } else if (view.getId() == R.id.backLogin) {
            // 返回
            finish();
        }
    }
    public void getVerifycode() {
        verifycode = "";
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int a = random.nextInt(3);
            switch(a) {
                case 0:
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