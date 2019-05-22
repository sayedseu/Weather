package com.example.weather.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.weather.R;
import com.example.weather.activity.activity.DetailsActivity;
import com.example.weather.activity.model.CurrentWeather;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private static final String BASE_URL = "http://openweathermap.org/img/w/";
    private List<com.example.weather.activity.model.List> lists;
    private Context context;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<com.example.weather.activity.model.List> lists) {
        this.lists = lists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.single_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        try {
            com.example.weather.activity.model.List list = lists.get(i);
            Double temp = (list.getMain().getTemp()) - 273.15;
            Glide.with(context).load(BASE_URL + list.getWeather().get(0).getIcon() + ".png")
                    .into(myViewHolder.iconIV);
            myViewHolder.cityNameTV.setText(list.getName());
            myViewHolder.temperatureTV.setText(new DecimalFormat("##.##").format(temp) + " \u2103");
            myViewHolder.weatherConditionTV.setText(list.getWeather().get(0).getMain());

        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.cityNameID)
        TextView cityNameTV;
        @BindView(R.id.tempID)
        TextView temperatureTV;
        @BindView(R.id.conditionID)
        TextView weatherConditionTV;
        @BindView(R.id.iconID)
        ImageView iconIV;

        private MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, DetailsActivity.class);
            com.example.weather.activity.model.List list = lists.get(getAdapterPosition());
            CurrentWeather currentWeather = new CurrentWeather(list.getCoord()
                    , list.getWeather(), list.getMain(), list.getWind(), list.getName());
            intent.putExtra(DetailsActivity.KEY, currentWeather);
            context.startActivity(intent);
        }
    }
}
