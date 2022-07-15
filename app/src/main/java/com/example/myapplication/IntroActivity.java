package com.example.myapplication;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.kakao.sdk.auth.AuthApiClient;
import com.kakao.sdk.common.model.KakaoSdkError;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



public class IntroActivity extends AppCompatActivity {
    private ImageView btn_login;
    private Account account;
    private static final String TAG="사용자";
    private static final String HOST = "192.249.19.168";
    private static final String PORT = "80";
    private static String user_id ;
    private static String name;
    private static String profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        btn_login=findViewById(R.id.btn_login);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(v.getContext())) {
                    UserApiClient.getInstance().loginWithKakaoTalk(v.getContext(), (oAuthToken, error) -> {
                        if (error != null) {
                            Log.e(TAG, "로그인 실패", error);
                        } else if (oAuthToken != null) {
                            Log.i(TAG, "로그인 성공(토큰) : " + oAuthToken.getAccessToken());
                            UserApiClient.getInstance().me((user, meError) -> {
                                if (meError != null) {
                                    Log.e(TAG, "사용자 정보 요청 실패", meError);
                                } else {
                                    System.out.println("로그인 완료");
                                    account = user.getKakaoAccount();
                                    Log.d("asdf", account.toString());
                                    user_id = String.valueOf(user.getId());
                                    name = account.getProfile().getNickname();
                                    profileImage = account.getProfile().getThumbnailImageUrl();
                                    RequestQueue requestQueue = Volley.newRequestQueue(IntroActivity.this);
                                    String uri = String.format("http://" + HOST + "/create_user?user_id=" + user_id + "&name=" + name + "&user_img=" + profileImage);
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener() {
                                        @Override
                                        public void onResponse(Object response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response.toString());
                                                Log.d("response", jsonObject.toString());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d("볼리에러", "에러");
                                        }
                                    });
                                    requestQueue.add(stringRequest);
                                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                                    intent.putExtra("profile_image", profileImage);
                                    intent.putExtra("nickname", name);
                                    intent.putExtra("kakao_id", String.valueOf(user.getId()));
                                    Log.d("send", "" + profileImage + name + user_id);
                                    startActivity(intent);
                                }
                                return null;
                            });
                        }
                        return null;
                    });
                }

                else {
                    UserApiClient.getInstance().loginWithKakaoAccount(v.getContext(), (oAuthToken, error) -> {
                        if (error != null) {
                            Log.e(TAG, "로그인 실패", error);
                            Log.e(TAG, "여기 오류 ", error);
                        } else if (oAuthToken != null) {
                            UserApiClient.getInstance().me((user, meError) -> {
                                if (meError != null) {
                                    Log.e(TAG, "사용자 정보 요청 실패", meError);
                                } else {
                                    System.out.println("로그인 완료");
                                    account = user.getKakaoAccount();
                                    Log.d("asdf", account.toString());
                                    user_id = String.valueOf(user.getId());
                                    name = account.getProfile().getNickname();
                                    profileImage = account.getProfile().getThumbnailImageUrl();
                                    RequestQueue requestQueue = Volley.newRequestQueue(IntroActivity.this);
                                    String uri = String.format("http://" + HOST + "/create_user?user_id=" + user_id + "&name=" + name + "&user_img=" + profileImage);
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener() {
                                        @Override
                                        public void onResponse(Object response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response.toString());
                                                Log.d("response", jsonObject.toString());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                Log.d("오류", "여긴가?");
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d("볼리에러", error.toString());
                                        }
                                    });
                                    requestQueue.add(stringRequest);
                                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                                    intent.putExtra("profile_image", profileImage);
                                    intent.putExtra("nickname", name);
                                    intent.putExtra("kakao_id", String.valueOf(user.getId()));
                                    Log.d("send", "" + profileImage + name + user_id);
                                    startActivity(intent);
                                }
                                return null;
                            });
                        }
                        return null;
                    });
                }
            }});
        }
}
//    public void startLoginActivity(){
//        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//        startActivity(intent);
//    }

//    public void startMapFragmentActivity(){
//        Intent intent = new Intent(MainActivity.this, MapFragmentActivity.class);
//        startActivity(intent);
//    }




