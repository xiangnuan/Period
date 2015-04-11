package com.qinchu.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qinchu.app.R;
import com.qinchu.app.base.BaseActivity;
import com.qinchu.app.base.MessageToast;
import com.qinchu.app.db.UserProxy;
import com.qinchu.app.entity.User;
import com.qinchu.app.proxy.SettingProxy;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @InjectView(R.id.splashcontainer)
    View splashcontainer;
    @InjectView(R.id.logincontainer)
    View logincontainer;
    @InjectView(R.id.confirmlayout)
    View confirmlayout;

    @InjectView(R.id.usename)
    EditText usenameEditText;
    @InjectView(R.id.password)
    EditText passwordEditText;
    @InjectView(R.id.confirm)
    EditText confirmEditText;
    @InjectView(R.id.login)
    Button loginButton;


    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        logincontainer.setVisibility(View.GONE);
        splashcontainer.setVisibility(View.VISIBLE);
        confirmlayout.setVisibility(View.GONE);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startNextPage();
            }
        }, 1500);
    }

    private void startNextPage() {

        int uid = SettingProxy.getUid();
        if (uid == -1) {
            //退出登录或者没有登录
            splashcontainer.setVisibility(View.GONE);
            logincontainer.setVisibility(View.VISIBLE);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    @OnClick({R.id.loginpage, R.id.registerpage})
    public void switchPage(View v) {
        switch (v.getId()) {
            case R.id.loginpage:
                confirmlayout.setVisibility(View.GONE);
                loginButton.setText("登录");
                break;
            case R.id.registerpage:
                confirmlayout.setVisibility(View.VISIBLE);
                loginButton.setText("注册");
                break;
        }
    }

    @OnClick(R.id.login)
    public void login(View v) {
        //shown 表示现在确认密码显示,是注册状态
        String usename = usenameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirm = confirmEditText.getText().toString().trim();

        if (TextUtils.isEmpty(usename)) {
            MessageToast.show("请输入用户名", MessageToast.Style.ALERT);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            MessageToast.show("请输入密码", MessageToast.Style.ALERT);
            return;
        }

        if (confirmlayout.isShown()) {
            if (TextUtils.isEmpty(confirm)) {
                MessageToast.show("请确认密码", MessageToast.Style.ALERT);
                return;
            }

            if (!password.equals(confirm)) {
                MessageToast.show("两次密码不一致", MessageToast.Style.ALERT);
                return;
            }
            //uid 生成规则 usename + password的md5 取hashcode
            password = generate(password);
            int newUid = (usename + password).hashCode();
            User user = UserProxy.getUser(newUid);
            //说明已有这样一个账户
            if (user != null) {
                MessageToast.show("已有这样一个账户", MessageToast.Style.ALERT);
                return;
            }
            user = new User();
            user.setUid(newUid);
            user.setName(usename);
            user.setPassword(password);
            UserProxy.saveUser(user);
            //清空内容等待登录
            passwordEditText.setText("");
            confirmEditText.setText("");
            confirmlayout.setVisibility(View.GONE);
            loginButton.setText("登录");
        }else{
            password = generate(password);
            int newUid = (usename + password).hashCode();
            User user = UserProxy.getUser(newUid);
            //说明账户密码不正确或者用户不存在
            if (user == null) {
                MessageToast.show("账户密码不正确或者用户不存在", MessageToast.Style.ALERT);
                return;
            }
            SettingProxy.saveUid(newUid);
            //清空内容等待登录
            passwordEditText.setText("");
            confirmEditText.setText("");

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }

    }

    public String generate(String password) {
        byte[] md5 = getMD5(password.getBytes());
        BigInteger bi = (new BigInteger(md5)).abs();
        return bi.toString(36);
    }

    private byte[] getMD5(byte[] data) {
        byte[] hash = null;

        try {
            MessageDigest e = MessageDigest.getInstance("MD5");
            e.update(data);
            hash = e.digest();
        } catch (NoSuchAlgorithmException var4) {
            Log.e("getMD5", var4.getMessage());
        }

        return hash;
    }
}
