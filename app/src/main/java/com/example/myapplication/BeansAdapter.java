package com.example.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

public class BeansAdapter extends RecyclerView.Adapter<BeansAdapter.CustomViewHolder> {
    private Context applicationContext;
    ViewGroup viewGroup  ;
    private ArrayList<Beans> arrayList;
    private static final String HOST = "192.249.19.168";
    private static final String PORT = "80";
    private String id ;
    Context parentContext;
    //생성자 선언
    public BeansAdapter(ArrayList<Beans> arrayList ) {
        this.arrayList = arrayList;

    }

    public BeansAdapter(String id, Context applicationContext, ArrayList<Beans> arrayList) {
        this.arrayList = arrayList;
        this.applicationContext = applicationContext;
        this.id=id;
    }

    @NonNull
    @Override
    //처음으로 생성될 때
    public BeansAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.beans_item,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        parentContext =parent.getContext();
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull BeansAdapter.CustomViewHolder holder, int position) {
        Log.d("holder", "------ holder = " + (holder.beans_img));

        holder.beans_img = (ImageView)holder.itemView.findViewById(R.id.beans_img);
        holder.beans_name = (TextView)holder.itemView.findViewById(R.id.beans_name);
        holder.beans_price = (TextView)holder.itemView.findViewById(R.id.beans_price);

        holder.beans_img.setImageResource(arrayList.get(position).getBenas_img());
       // holder.beans_img.setImageResource(R.drawable.ic_launcher_foreground);
        holder.beans_name.setText(arrayList.get(position).getBeans_name());
        holder.beans_price.setText(arrayList.get(position).getBeans_price());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Activity activity = unwrap(parentContext);
                        Dialog selectedDialog = new Dialog(view.getContext());
                        selectedDialog.setContentView(R.layout.selected_bean_dialog);
                        WindowManager.LayoutParams wm2 = selectedDialog.getWindow().getAttributes();
                        wm2.copyFrom(selectedDialog.getWindow().getAttributes());
                        wm2.width=1200;
                        wm2.height= 2000;
                        selectedDialog.getWindow().setGravity(Gravity.BOTTOM);
                        selectedDialog.getWindow().setDimAmount(0);
                        selectedDialog.show();

                        //텍스트
                        TextView tv = (TextView)selectedDialog.findViewById(R.id.selected_bean_name);
                        TextView price = (TextView)selectedDialog.findViewById(R.id.selected_bean_price);
                        ImageView iv = (ImageView)selectedDialog.findViewById(R.id.selected_beans_img);

                        iv.setImageResource(arrayList.get(position).getBenas_img());

                        tv.setText(arrayList.get(position).getBeans_name());
                        price.setText(arrayList.get(position).getBeans_price());
                        //버튼
                        Button buy= selectedDialog.findViewById(R.id.buy);
                        Button goto_back = selectedDialog.findViewById(R.id.goto_back);

                        buy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(view.getContext(), "구매완료", Toast.LENGTH_SHORT).show();
                                selectedDialog.dismiss();
                                Log.d("id",id);
                                RequestQueue requestQueue = Volley.newRequestQueue(applicationContext);
                                String uri = String.format("http://" + HOST + "/add_beans?user_id=" + id + "&beans_name=" + arrayList.get(position).getBeans_name() + "&beans_price=" + arrayList.get(position).getBeans_price() +"&beans_img="+arrayList.get(position).getBenas_img());
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener() {
                                    @Override
                                    public void onResponse(Object response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response.toString());
                                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                                            String url = jsonArray.getJSONObject(0).getString("wakeup_img");
                                            String wakeup_time =  jsonArray.getJSONObject(0).getString("wakeup_time");
                                            Log.d("url", url);
                                            Log.d("wakeup_time", wakeup_time);
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


                            }
                        });

                        goto_back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                selectedDialog.dismiss();
                            }
                        });

                    }
                });
    }

    private static Activity unwrap(Context context) {
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }

        return (Activity) context;
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView beans_img;
        protected TextView beans_name;
        protected TextView beans_price;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.beans_img = (ImageView)itemView.findViewById(R.id.beans_img);
            this.beans_name = (TextView)itemView.findViewById(R.id.beans_name);
            this.beans_price = (TextView)itemView.findViewById(R.id.beans_price);
        }
    }
}
