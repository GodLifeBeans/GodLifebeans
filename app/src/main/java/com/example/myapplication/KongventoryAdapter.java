package com.example.myapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class KongventoryAdapter extends RecyclerView.Adapter<KongventoryAdapter.CustomViewHolder> {
    ViewGroup viewGroup  ;
    private ArrayList<Kongventory> arrayList;
    private static final String HOST = "192.249.19.168";
    private static final String PORT = "80";
    //생성자 선언
    public KongventoryAdapter(ArrayList<Kongventory> arrayList ) {
        this.arrayList = arrayList;

    }
    @NonNull
    @Override
    //처음으로 생성될 때
    public KongventoryAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kongventory_beans_item,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull KongventoryAdapter.CustomViewHolder holder, int position) {
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
//                RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
//                String uri = String.format("http://" + HOST + "/create_user?user_id=" + user_id + "&name=" + name + "&user_img=" + profileImage);
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener() {
//                    @Override
//                    public void onResponse(Object response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response.toString());
//                            JSONArray jsonArray = jsonObject.getJSONArray("result");
//                            String url = jsonArray.getJSONObject(0).getString("wakeup_img");
//                            String wakeup_time =  jsonArray.getJSONObject(0).getString("wakeup_time");
//                            Log.d("url", url);
//                            Log.d("wakeup_time", wakeup_time);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.d("오류", "여긴가?");
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("볼리에러", error.toString());
//                    }
//                });
//                requestQueue.add(stringRequest);


//               View view1=  findParentViewHolderItemView(view);
//                LinearLayout linearLayoH_PARENT /* layout_width */, LinearLayout.LayoutParams.WRAP_CONTENT /* layout_height */, 1f /* layout_weight */);
//                ImageView iv = new ImageView(view.getContext());  // 새로 추가할 imageView 생성
//                iv.setImageResource(R.drawable.ic_launcher_foreground);  // imageView에 내용 추가
//                linearLayout.addView(iv); // 기존 linearLayout에 imageView 추가
//                Dialog selectedDialog = new Dialog(view.getContext());
//                selectedDialog.setContentView(R.layout.selected_bean_dialog);
//                WindowManager.LayoutParams wm2 = selectedDialog.getWindow().getAttributes();
//                wm2.copyFrom(selectedDialog.getWindow().getAttributes());
//                wm2.width=1200;
//                wm2.height= 2000;
//                selectedDialog.getWindow().setGravity(Gravity.BOTTOM);
//                selectedDialog.getWindow().setDimAmount(0);
//                selectedDialog.show();
//
//                //텍스트
//                TextView tv = (TextView)selectedDialog.findViewById(R.id.selected_bean_name);
//                TextView price = (TextView)selectedDialog.findViewById(R.id.selected_bean_price);
//                ImageView img = (ImageView)selectedDialog.findViewById(R.id.selected_beans_img);
//                img.setImageResource(arrayList.get(position).getBenas_img());
//                tv.setText(arrayList.get(position).getBeans_name());
//                price.setText(arrayList.get(position).getBeans_price());

            }
        });

    }

    public static View findParentViewHolderItemView(View v) {
        final ViewParent parent = v.getParent();
        if (parent instanceof RecyclerView) {
            // returns the passed instance if the parent is RecyclerView
            return v;
        } else if (parent instanceof View) {
            // check the parent view recursively
            return findParentViewHolderItemView((View) parent);
        } else {
            return null;
        }
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
