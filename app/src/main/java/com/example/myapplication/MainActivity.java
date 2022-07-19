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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements OnItemClick {
    ImageView goto_todo_btn;
    ImageView goto_wakeup_btn;
    ImageView goto_beanshop_btn;
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
    float x = 100;
    float y =100;
    int count =1;
    private ImageView iviv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.main_view);
        View main_view = (View)findViewById(R.id.main_view);
        Intent intent = getIntent();
        String profileImage = intent.getStringExtra("profile_image");
        String name=intent.getStringExtra("nickname");
        String id = intent.getStringExtra("kakao_id");

        //my_Stamp
        Button my_stamp = (Button)findViewById(R.id.my_stamp);


        Log.d("profile, name, id", ""+profileImage+name+id);

        //이동하는 버튼들
        Button goto_wakeup_btn = (Button)findViewById(R.id.goto_wakeup_btn);
        Button goto_todo_btn = (Button)findViewById(R.id.goto_todo_btn);
        Button goto_beanshop_btn = (Button)findViewById(R.id.goto_beanshop_btn);
        Button congventory = (Button)findViewById(R.id.congventory);
        Button goto_meal_btn = (Button)findViewById(R.id.goto_meals_btn);
        Button goto_friends = (Button)findViewById(R.id.goto_friends);

        goto_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FriendsActivity.class);
                startActivity(intent);
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                       int id =  view.getId();
                       Log.d("id", String.valueOf(id));

                       // Toast.makeText(MainActivity.this, "Onclick호출",Toast.LENGTH_SHORT).show();
                      //  Log.d("x좌표", String.valueOf(motionEvent.getX()));
                      //  Log.d("y좌표",String.valueOf(motionEvent.getY()));
//                bean_img.setX(motionEvent.getX());
//                bean_img.setY(motionEvent.getY());
                        return false;
                    }
            });
            }
        };



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
//     콩벤토리  받아오기
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



        //맵 받아오기
        RequestQueue requestQueue1 = Volley.newRequestQueue(MainActivity.this);
        String uri1 = String.format("http://" + HOST + "/get_map_kongs?user_id=" + id);
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, uri1, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    Log.d("response", jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    Log.d("result", jsonArray.toString());
                    Log.d("result size", String.valueOf(jsonArray.length()));
                    for (int i = 0 ; i<jsonArray.length();i++){
                        //beanid 부여
                        count=  jsonArray.getJSONObject(i).getInt("beans_id");
                        Log.d("count",String.valueOf(count));
                        String name =  jsonArray.getJSONObject(i).getString("beans_img");
                        ImageView iv = new ImageView(getApplicationContext());  // 새로 추가할 imageView 생성
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(500,500 );
                        iv.setLayoutParams(layoutParams);
                        iv.setImageResource(Integer.parseInt(name));

                        //id부여
                        iv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                iviv = iv;
                            }
                        });
                        if (x !=0 && y !=0) {
                            x= (float) jsonArray.getJSONObject(i).getDouble("location_x");
                            y= (float) jsonArray.getJSONObject(i).getDouble("location_y");
                        }
                        else{
                            x=x+100;
                            y=y+100;
                        }
                        iv.setX(x);
                        iv.setY(y);
                        iv.setId(count);
                        iv.getId();
                        frameLayout.addView(iv);
                    }
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
        requestQueue1.add(stringRequest1);

//        ImageView img = (ImageView)frameLayout.findViewById(23);


        //콩벤토리
        congventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog beanventory = new Dialog(MainActivity.this);
                beanventory.setContentView(R.layout.beanventory_dialog);
                WindowManager.LayoutParams wm = beanventory.getWindow().getAttributes();
                wm.copyFrom(beanventory.getWindow().getAttributes());
                ImageView goto_main = (ImageView) beanventory.findViewById(R.id.goto_main);
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
                goto_main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        beanventory.dismiss();
                        finish();
                        startActivity(intent);
                    }
                });
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

                if(iviv != null) {
                    iviv.setX(motionEvent.getX() - iviv.getWidth() / 2);
                    iviv.setY(motionEvent.getY() - iviv.getHeight() / 2);

                    float location_x = motionEvent.getX() - iviv.getWidth() / 2;
                    float location_y = motionEvent.getY() - iviv.getHeight() / 2;
                    Log.d("id", String.valueOf(iviv.getId()));
                    Log.d("id, + 위치 ",location_x+"위치"+location_y);

                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    String uri1 = String.format("http://" + HOST + "/set_beans_location?beans_id=" + iviv.getId() + "&location_x=" + location_x + "&location_y=" + location_y);
                    StringRequest stringRequest1 = new StringRequest(Request.Method.POST, uri1, new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.toString());
                                Log.d("response", jsonObject.toString());
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                Log.d("result", jsonArray.toString());
                                Log.d("result size", String.valueOf(jsonArray.length()));

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
                    requestQueue.add(stringRequest1);
                }

                iviv = null;
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

                ObjectAnimator smileX = ObjectAnimator. ofFloat(bean_img, "translationX", previous_x, touch_x);
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


    @Override
    public void onClick(String value) {

    }
}