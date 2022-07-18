package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;
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

public class KongventoryAdapter extends RecyclerView.Adapter<KongventoryAdapter.CustomViewHolder> {
    private OnItemClick mCallback;
    private Context applicationContext;
    Context parentContext;
    String id ;
    int count = 1;
    private ArrayList<Kongventory> arrayList;
    private static final String HOST = "192.249.19.168";
    private static final String PORT = "80";
    View parentView;
    //생성자 선언
    public KongventoryAdapter(ArrayList<Kongventory> arrayList ) {
        this.arrayList = arrayList;
    }


    public KongventoryAdapter(String id, Context applicationContext, ArrayList<Kongventory> arrayList) {
        this.arrayList = arrayList;
        this.applicationContext = applicationContext;
        this.id=id;

    }


    @NonNull
    @Override
    //처음으로 생성될 때
    public KongventoryAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kongventory_beans_item,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        parentContext =parent.getContext();
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull KongventoryAdapter.CustomViewHolder holder, int position) {

        holder.beans_img = (ImageView)holder.itemView.findViewById(R.id.beans_img);
        holder.beans_name = (TextView)holder.itemView.findViewById(R.id.beans_name);
        holder.beans_price = (TextView)holder.itemView.findViewById(R.id.beans_price);

        holder.beans_img.setImageResource(arrayList.get(position).getBenas_img());

        holder.beans_name.setText(arrayList.get(position).getBeans_name());
        holder.beans_price.setText(arrayList.get(position).getBeans_price());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Activity activity = unwrap(parentContext);
//                FrameLayout linearLayout   =(FrameLayout)(activity).findViewById(R.id.main_view);
//                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(500,500 );
//                ImageView iv = new ImageView(applicationContext);  // 새로 추가할 imageView 생성
//                iv.setLayoutParams(layoutParams);

                Toast.makeText(view.getContext(), "Map으로 이동되었습니다. ", Toast.LENGTH_SHORT).show();
             //   iv.setImageResource(arrayList.get(position).getBenas_img());
                //id 부여
              //  linearLayout.addView(iv);
                RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                String uri = String.format("http://" + HOST + "/update_beans_state?user_id=" + id + "&beans_name=" + arrayList.get(position).getBeans_name());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            Log.d("jsonObject", jsonObject.toString());
                            remove(holder.getAdapterPosition());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
                requestQueue.add(stringRequest);

            }
        });

    }

    public void remove(int position){
        try{
            arrayList.remove(position);
            //새로고침
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
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
            this.beans_img = (ImageView)itemView.findViewById(R.id.selected_beans_img);
            this.beans_name = (TextView)itemView.findViewById(R.id.selected_bean_name);
            this.beans_price = (TextView)itemView.findViewById(R.id.selected_bean_price);
        }
    }
}

