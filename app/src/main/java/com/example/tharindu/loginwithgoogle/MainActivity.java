package com.example.tharindu.loginwithgoogle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {
    private LinearLayout prof_section;
    private Button signout;
    private SignInButton signin;
    private TextView viewname,viewemail;
    private ImageView prof_pic;
    private GoogleApiClient googleApiClient;
    private static final int rec_code=9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        signout=(Button)findViewById(R.id.signout);
        signin=(SignInButton)findViewById(R.id.signin);
        viewname=(TextView)findViewById(R.id.name);
        viewemail=(TextView)findViewById(R.id.email);
        prof_pic=(ImageView)findViewById(R.id.prof_pic);
        signin.setOnClickListener(this);
        signout.setOnClickListener(this);
        signout.setVisibility(View.GONE);
        prof_pic.setVisibility(View.GONE);
        viewemail.setVisibility(View.GONE);
        viewname.setVisibility(View.GONE);

        GoogleSignInOptions signInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signin:
                Signin();
                break;
            case R.id.signout:
                Signout();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private  void Signin(){
        Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,rec_code);
    }
    private  void Signout(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                UpdateUI(false);
            }
        });

    }
    private  void handleResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount signInAccount=result.getSignInAccount();
            String name=signInAccount.getDisplayName();
            String email=signInAccount.getEmail();
            String img_url=signInAccount.getPhotoUrl().toString();
            viewname.setText(name);
            viewemail.setText(email);
            Glide.with(this).load(img_url).into(prof_pic);
            UpdateUI(true);
        }
    }
    private  void UpdateUI(boolean isLogin){
        if(isLogin){
            signout.setVisibility(View.VISIBLE);
            prof_pic.setVisibility(View.VISIBLE);
            viewemail.setVisibility(View.VISIBLE);
            viewname.setVisibility(View.VISIBLE);
            signin.setVisibility(View.GONE);

        }else{
            signout.setVisibility(View.GONE);
            prof_pic.setVisibility(View.GONE);
            viewemail.setVisibility(View.GONE);
            viewname.setVisibility(View.GONE);
            signin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==rec_code){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }


}
