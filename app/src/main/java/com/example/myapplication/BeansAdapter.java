package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
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

import java.util.ArrayList;

public class BeansAdapter extends RecyclerView.Adapter<BeansAdapter.CustomViewHolder> {
    private Context applicationContext;
    ViewGroup viewGroup  ;
    private ArrayList<Beans> arrayList;
    private String id ;
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

//                                데베에 올려버리기
//
//
                                 //layout param 생성
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT /* layout_width */, LinearLayout.LayoutParams.WRAP_CONTENT /* layout_height */, 1f /* layout_weight */);
                                ImageView iv = new ImageView(view.getContext());  // 새로 추가할 imageView 생성
                                iv.setImageResource(R.drawable.ic_launcher_foreground);  // imageView에 내용 추가
                               // LinearLayout linearLayout = v.
                               // linearLayout.addView(iv); // 기존 linearLayout에 imageView 추가
                                //1. 돈 충분한지
                                //2. 돈 있으면 테이블에 넣기
                                //3. 테이블에 콩 넣기
                                //4. 리사이클러뷰에 내 콩들 넣기
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
