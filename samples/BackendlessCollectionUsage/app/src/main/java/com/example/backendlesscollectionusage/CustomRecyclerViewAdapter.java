package com.example.backendlesscollectionusage;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.backendless.persistence.BackendlessDataCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.CarViewHolder> {
    private Collection<Car> scrCollection;
    private Iterator<Car> scrCollectionIterator;
    private Handler handler;

    private Activity activity;
    private int batchToShow;
    private int percentToLoad;
    private ArrayList<Car> innerList = new ArrayList<>();

    public CustomRecyclerViewAdapter(Activity activity, int batchToShow, int percentToLoad) {
        this.activity = activity;
        this.batchToShow = batchToShow;
        this.percentToLoad = percentToLoad;

        HandlerThread ht = new HandlerThread(this.getClass().getName());
        ht.start();
        this.handler = new Handler(ht.getLooper() );

        this.handler.post(() -> {
            BackendlessDataCollection<Car> carCollection = new BackendlessDataCollection<>(Car.class);
            CustomRecyclerViewAdapter.this.setScrCollection(carCollection);
            CustomRecyclerViewAdapter.this.setScrCollectionIterator(carCollection.iterator());
        });

        addObjectsToInnerList();
    }



    private void setScrCollection(Collection<Car> scrCollection) {
        this.scrCollection = scrCollection;
    }

    private void setScrCollectionIterator(Iterator<Car> scrCollectionIterator) {
        this.scrCollectionIterator = scrCollectionIterator;
    }

    private void addObjectsToInnerList() {
        this.handler.post(() -> {
                while (CustomRecyclerViewAdapter.this.scrCollectionIterator == null) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                int position = 0;
                while (CustomRecyclerViewAdapter.this.scrCollectionIterator.hasNext() && position < batchToShow) {
                    innerList.add(CustomRecyclerViewAdapter.this.scrCollectionIterator.next());
                    position++;
                }

                activity.runOnUiThread(CustomRecyclerViewAdapter.this::notifyDataSetChanged);
            }
        );
    }

    @Override
    public void onBindViewHolder(CarViewHolder carViewHolder, int position) {
        if (position == innerList.size() - this.batchToShow * this.percentToLoad / 100)
            addObjectsToInnerList();

        carViewHolder.setSomeCar(innerList.get(position));
        carViewHolder.bindData();
    }

    @Override
    public int getItemCount() {
        return innerList.size();
    }

    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item, parent, false);
        return new CarViewHolder(view);
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {

        Car someCar;
        TextView carBrandLabel;
        TextView carModelLabel;

        public Car getSomeCar() {
            return someCar;
        }

        public void setSomeCar(Car someCar) {
            this.someCar = someCar;
        }

        public CarViewHolder(View itemView) {
            super(itemView);
        }

        public void bindData() {
            if (carBrandLabel == null) {
                carBrandLabel = itemView.findViewById(R.id.carBrandLabel);
            }
            if (carModelLabel == null) {
                carModelLabel = itemView.findViewById(R.id.carModelLabel);
            }
            carBrandLabel.setText(someCar.getBrand());
            carModelLabel.setText(someCar.getModel());
        }
    }
}
