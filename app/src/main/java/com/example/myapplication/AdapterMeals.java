package com.example.myapplication;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
public class AdapterMeals extends RecyclerView.Adapter<AdapterMeals.mealViewHolder> {
    private ArrayList<Meals> meals = new ArrayList<>();
    public AdapterMeals(ArrayList<Meals> mealsArrayList){
        meals = mealsArrayList;
    }
    @NonNull
    @Override
    public mealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meals, parent, false);
        return new mealViewHolder(view);
    }
    @Override
    public void onBindViewHolder(mealViewHolder holder, int position){
        holder.onBind(meals.get(position));
    }
    @Override
    public int getItemCount(){
        return meals.size();
    }
    public static class mealViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView time;
        TextView name;
        TextView comment;
        public mealViewHolder(View itemview){
            super(itemview);
            name = (TextView) itemview.findViewById(R.id.name);
            time = (TextView) itemview.findViewById(R.id.time);
            image = (ImageView) itemview.findViewById(R.id.image);
            comment =(TextView) itemview.findViewById(R.id.comment);
        }
        void onBind(Meals meal){
            name.setText(meal.getName());
            time.setText(meal.getTime());
            image.setImageURI(Uri.parse(meal.getImageurl()));
            comment.setText(meal.getComment());
        }
    }
}