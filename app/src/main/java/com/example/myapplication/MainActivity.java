package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button goto_todo_btn;
    Button goto_wakeup_btn;
    Button goto_beanshop_btn;
    //intent로 받아온 값
    ImageView bean_img;
    float previous_x = 0;
    float previous_y = 0;
    String selectedBeanId ;


    //리사이클러뷰
    private ArrayList<Beans> arrayList;
    private BeansAdapter beansAdapter;
    private RecyclerView recyclerView;
    private static final String HOST = "192.249.19.168";
    private static final String PORT = "80";
    //
    private ArrayList<Kongventory> kvArrayList;
    private KongventoryAdapter kongventoryAdapter;
    private RecyclerView kongventoryRv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.main_view);
        View main_view = (View)findViewById(R.id.main_view);
        Intent intent = getIntent();
        String profileImage = intent.getStringExtra("profile_image");
        String name=intent.getStringExtra("nickname");
        String id = intent.getStringExtra("kakao_id");
        ImageView bean_img = (ImageView)findViewById(R.id.bean_img);

        Log.d("profile, name, id", ""+profileImage+name+id);

        //이동하는 버튼들
        Button goto_wakeup_btn = (Button)findViewById(R.id.goto_wakeup_btn);
        Button goto_todo_btn = (Button)findViewById(R.id.goto_todo_btn);
        Button goto_beanshop_btn = (Button)findViewById(R.id.goto_beanshop_btn);
        Button congventory = (Button)findViewById(R.id.congventory);
        Button goto_meal_btn = (Button)findViewById(R.id.goto_meals_btn);

        DisplayMetrics dm =getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height =dm.heightPixels;
        Log.d("height", String.valueOf((height)));


        //콩상점 리사이클러뷰
        arrayList = new ArrayList<>();
        Drawable drawable = getResources().getDrawable(R.drawable.ic_launcher_foreground);
//        arrayList.add(new Beans("first","200",R.drawable.ic_launcher_background));
        arrayList.add(new Beans("아몬드","0",R.drawable.amond));
        arrayList.add(new Beans("병아리콩","0",R.drawable.byungarikong));
        arrayList.add(new Beans("땅콩","0",R.drawable.ddangkong));
        arrayList.add(new Beans("눕땅콩","0",R.drawable.ddangkong2));
        arrayList.add(new Beans("호두","0", R.drawable.hodu));
        arrayList.add(new Beans("무지개","0",R.drawable.muzigae));
        arrayList.add(new Beans("핑클","0",R.drawable.pinkkong));
        arrayList.add(new Beans("완두","0",R.drawable.wandos));



        //콩벤토리 리사이클러뷰
        kvArrayList = new ArrayList<>();
//       받아오기
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String uri = String.format("http://" + HOST + "/get_kongventory?user_id=" + id);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    Log.d("response", jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    Log.d("result", jsonArray.toString());
                    for (int i = 0 ; i<jsonArray.length();i++){
                      //  Log.d("array", jsonArray.get(i).toString());
                        JSONObject usage = jsonArray.getJSONObject(i);
                        Log.d("useage",usage.toString());
                        String name = usage.getString("beans_name");
                        String price = usage.getString("beans_price");
                        String img = usage.getString("beans_img");
                        int img_toInt = Integer.parseInt(img);
                        Kongventory addkong = new Kongventory(name, price, img_toInt);
                        kvArrayList.add(addkong);
                       // kongventoryAdapter.notifyDataSetChanged();

                    }
                   // String wakeup_time =  jsonArray.getJSONObject(0).getString("wakeup_time");

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


        //콩벤토리
        congventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog beanventory = new Dialog(MainActivity.this);
                beanventory.setContentView(R.layout.beanventory_dialog);
                WindowManager.LayoutParams wm = beanventory.getWindow().getAttributes();
                wm.copyFrom(beanventory.getWindow().getAttributes());
                wm.width=width;
                wm.height=height-300;
                kongventoryRv = beanventory.findViewById(R.id.kongventory_bean_rv);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
                kongventoryRv.setLayoutManager(gridLayoutManager);
                kongventoryAdapter = new KongventoryAdapter(id,getApplicationContext(),kvArrayList);
                kongventoryRv.setAdapter(kongventoryAdapter);
                Log.d("arraylist",String.valueOf(arrayList.size()));
                kongventoryAdapter.notifyDataSetChanged();
                beanventory.getWindow().setGravity(Gravity.BOTTOM);
                beanventory.getWindow().setDimAmount(0);
                beanventory.show();
            }
        });

        //하단 탭들
        goto_todo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, TodoActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("id",id);
                intent.putExtra("profileImage",profileImage);
                startActivity(intent);
            }
        });

        goto_wakeup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, WakeupActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        goto_beanshop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goto_beanshop_btn.setTag(id);
                Dialog shopDialog = new Dialog(MainActivity.this);
                shopDialog.setContentView(R.layout.bean_shop_dialog);
                //dialog 크기 바꾸기
                WindowManager.LayoutParams wm = shopDialog.getWindow().getAttributes();
                wm.copyFrom(shopDialog.getWindow().getAttributes());
                wm.width=width;
                wm.height=height-300;
                shopDialog.getWindow().setGravity(Gravity.BOTTOM);
                shopDialog.getWindow().setDimAmount(0);
                shopDialog.show();
                ImageView goto_main = shopDialog.findViewById(R.id.goto_main);
                //뒤로가기 버튼
                goto_main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shopDialog.dismiss();
                        finish();
                        startActivity(getIntent());
                    }

                });

                Log.d("리사이클러뷰","시작");
             //   리사이클러뷰
                recyclerView = (RecyclerView)shopDialog.findViewById(R.id.bean_rv);
                Log.d("리사이클러뷰",recyclerView.toString());
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(gridLayoutManager);
                beansAdapter = new BeansAdapter(id,getApplicationContext(),arrayList);

               // beansAdapter = new BeansAdapter(arrayList);
                recyclerView.setAdapter(beansAdapter);

               // 각각의 콩들 클릭 이벤트
//                CardView firstbean = shopDialog.findViewById(R.id.first_bean);
//                firstbean.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        selectedBeanId = "First";
//                        Log.d("firstbean", "firstbean");
//                        Dialog selectedDialog = new Dialog(shopDialog.getContext());
//                        selectedDialog.setContentView(R.layout.selected_bean_dialog);
//                        WindowManager.LayoutParams wm2 = selectedDialog.getWindow().getAttributes();
//                        wm2.copyFrom(selectedDialog.getWindow().getAttributes());
//                        wm2.width=wm.width;
//                        wm2.height= wm.height-100;
//                        selectedDialog.getWindow().setGravity(Gravity.BOTTOM);
//                        selectedDialog.getWindow().setDimAmount(0);
//                        selectedDialog.show();
//
//                        Button buy= selectedDialog.findViewById(R.id.buy);
//                        Button goto_back = selectedDialog.findViewById(R.id.goto_back);
//
//                        buy.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Toast.makeText(getApplication(), "구매완료", Toast.LENGTH_SHORT).show();
//                                selectedDialog.dismiss();
//                                shopDialog.dismiss();
//                                // layout param 생성
//                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT /* layout_width */, LinearLayout.LayoutParams.WRAP_CONTENT /* layout_height */, 1f /* layout_weight */);
//                                ImageView iv = new ImageView(getApplicationContext());  // 새로 추가할 imageView 생성
//                                iv.setImageResource(R.drawable.ic_launcher_foreground);  // imageView에 내용 추가
//                                linearLayout.addView(iv); // 기존 linearLayout에 imageView 추가
//                                //1. 돈 충분한지
//                                //2. 돈 있으면 테이블에 넣기
//                                //3. 테이블에 콩 넣기
//                                //4. 리사이클러뷰에 내 콩들 넣기
//                            }
//                        });
//
//                        goto_back.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                selectedDialog.dismiss();
//                            }
//                        });
//
//                    }
//                });
            }
        });


        goto_meal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MealsActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("id",id);
                intent.putExtra("profileImage",profileImage);
                startActivity(intent);
            }
        });

        //이동시키는거
        main_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
               Log.d("x좌표", String.valueOf(motionEvent.getX()));
                Log.d("y좌표",String.valueOf(motionEvent.getY()));
                bean_img.setX(motionEvent.getX());
                bean_img.setY(motionEvent.getY());
                return false;
            }
        });


    }

    public boolean onTouchEvent(MotionEvent event){
        switch(event.getAction()){
            /*누르기 시작하는 상태일 때*/
            case MotionEvent.ACTION_DOWN:
                break;

            /*터치하고 움직이는 상태일 때*/
            case MotionEvent.ACTION_MOVE:
                /*터치하고 있는 x,y 위치 인식*/
                int touch_x = (int)event.getX();
                int touch_y = (int)event.getY();

                ObjectAnimator smileX = ObjectAnimator.ofFloat(bean_img, "translationX", previous_x, touch_x);
                smileX.start();

                ObjectAnimator smileY = ObjectAnimator.ofFloat(bean_img, "translationY", previous_y, touch_x);
                smileY.start();

                /*50 밀리 초 동안 진동*/
                previous_x = touch_x;
                previous_y = touch_y;
                break;

            /*터치 후 손을 떼는 상태일 때*/
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;

    }


}