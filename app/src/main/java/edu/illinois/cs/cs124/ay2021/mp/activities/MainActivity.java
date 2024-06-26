package edu.illinois.cs.cs124.ay2021.mp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import edu.illinois.cs.cs124.ay2021.mp.R;
import edu.illinois.cs.cs124.ay2021.mp.adapters.RestaurantListAdapter;
import edu.illinois.cs.cs124.ay2021.mp.application.EatableApplication;
import edu.illinois.cs.cs124.ay2021.mp.databinding.ActivityMainBinding;
import edu.illinois.cs.cs124.ay2021.mp.models.Preference;
import edu.illinois.cs.cs124.ay2021.mp.models.Restaurant;
import java.util.ArrayList;
import java.util.List;

/*
 * App main activity.
 * Started when the app is launched, based on the configuration in the Android Manifest (AndroidManifest.xml).
 * Should display a sorted list of restaurants based on data retrieved from the server.
 *
 * You will need to understand some of the code here and make changes starting with MP1.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public final class MainActivity extends AppCompatActivity
    implements SearchView.OnQueryTextListener, RestaurantListAdapter.Listener {
  // You may find this useful when adding logging
  private static final String TAG = MainActivity.class.getSimpleName();

  // Binding to the layout defined in activity_main.xml
  private ActivityMainBinding binding;

  // List adapter for displaying the list of restaurants
  private RestaurantListAdapter listAdapter;

  // Reference to the persistent Application instance
  private EatableApplication application;

  /*
   * onCreate is the first method called when this activity is created.
   * Code here normally does a variety of setup tasks.
   */
  private List<Restaurant> l = new ArrayList<>();
  private List<Preference> p = new ArrayList<>();

  @Override
  protected void onCreate(final Bundle unused) {
    super.onCreate(unused);

    // Initialize our data binding
    // This allows us to more easily access elements of our layout
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

    // Create a new list adapter for restaurants and attach it to our app layout
    listAdapter = new RestaurantListAdapter(this, this);
    binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    binding.recyclerView.setAdapter(listAdapter);

    // Grab a reference to our application instance and use it to fetch the list of restaurants from
    // the server
    Log.d(TAG, "HelloWorld");
    application = (EatableApplication) getApplication();
    application
            .getClient()
            /*
             * What is passed to getRestaurants is a callback, which we'll discuss in more detail in the MP lessons.
             * Callbacks allow us to wait for something to complete and run code when it does.
             * In this case, once we retrieve a list of restaurants, we use it to update the contents of our list.
             */
            .getRestaurants(
                    (restaurants -> {
                      for (Restaurant r: restaurants) {
                        l.add(r);
                      }
                      listAdapter.edit().replaceAll(restaurants).commit();
                    }));
    application
            .getClient()
            /*
             * What is passed to getRestaurants is a callback, which we'll discuss in more detail in the MP lessons.
             * Callbacks allow us to wait for something to complete and run code when it does.
             * In this case, once we retrieve a list of restaurants, we use it to update the contents of our list.
             */
            .getPreferences(
                    (preferences -> {
                      for (Preference pref: preferences) {
                        p.add(pref);
                      }
                    }));
    // Bind to the search component so that we can receive events when the contents of the search
    // box change
    binding.search.setOnQueryTextListener(this);

    // Bind the toolbar that contains our search component
    setSupportActionBar(binding.toolbar);

    // Set the activity title
    setTitle("Find Restaurants");
  }

  public List<Preference> getP() {
    return p;
  }

  public List<Restaurant> getL() {
    return l;
  }
  /*
   * Called when the user changes the text in the search bar.
   * Eventually (MP1) we'll want to update the list of restaurants shown based on their input.
   */

  @Override
  public boolean onQueryTextChange(final String query) {
    listAdapter.edit().replaceAll(Restaurant.search(l, query)).commit();
    return true;
  }

  /*
   * Called when the user clicks on one of the restaurants in the list.
   * Eventually (MP2) we'll launch a new activity here so they can see the restaurant details.
                                                                                                          @Override
  public void onClicked(final Restaurant restaurant) {}

  /*
   * Called when the user submits their search query.
   * We update the list as the text changes, so don't need to do anything special here.
   */

  @Override
  public boolean onQueryTextSubmit(final String unused) {
    return true;
  }

  @Override
  public void onClicked(final Restaurant restaurant) {
    Intent startRestaurant = new Intent(this, RestaurantActivity.class);
    startRestaurant.putExtra("id", restaurant.getId());
    startActivity(startRestaurant);
  }
}
