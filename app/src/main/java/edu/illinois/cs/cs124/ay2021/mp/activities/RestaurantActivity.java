package edu.illinois.cs.cs124.ay2021.mp.activities;

import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import edu.illinois.cs.cs124.ay2021.mp.R;
import edu.illinois.cs.cs124.ay2021.mp.application.EatableApplication;
import edu.illinois.cs.cs124.ay2021.mp.databinding.ActivityRestaurantBinding;
import edu.illinois.cs.cs124.ay2021.mp.models.RelatedRestaurants;
import edu.illinois.cs.cs124.ay2021.mp.models.Restaurant;
import edu.illinois.cs.cs124.ay2021.mp.network.Client;

//MP2; Part 3 New activity to display restaurant details
public class RestaurantActivity  extends AppCompatActivity {

    // Binding to the layout defined in activity_main.xml
  private ActivityRestaurantBinding binding;
  private EatableApplication application;
  private Client c;
  @Override
  protected void onCreate(@Nullable final Bundle unsused) {
    super.onCreate(unsused);

    Intent startedIntent = getIntent();
    String id = startedIntent.getStringExtra("id");
    application = (EatableApplication) getApplication();
    Restaurant restaurant = application.getClient().getRestaurantForID(id);
    c = application.getClient();
    RelatedRestaurants rr = new RelatedRestaurants(c.getRes(), c.getPref());
    String val;
    int size;
    if (rr.getRelatedInOrder(restaurant.getId()).size() != 0)  {
      val = rr.getRelatedInOrder(restaurant.getId()).get(0).getName();
      size = rr.getConnectedTo(restaurant.getId()).size();
    } else {
      val = "";
      size = 0;
    }
    binding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant);
    binding.name.setText(restaurant.getName());
    binding.cuisine.setText(restaurant.getCuisine());
    binding.related.setText(val);
    binding.connected.setText(String.valueOf(size));
  }
}
