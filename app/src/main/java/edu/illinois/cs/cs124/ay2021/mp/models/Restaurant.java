package edu.illinois.cs.cs124.ay2021.mp.models;

import androidx.annotation.NonNull;
import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * Model storing information about a restaurant retrieved from the restaurant server.
 *
 * You will need to understand some of the code in this file and make changes starting with MP1.
 *
 * If your project builds successfully, you can safely ignore the warning about "Related problems" here.
 * It seems to be a bug in Android studio.
 */
@SuppressWarnings("unused")
public final class Restaurant implements SortedListAdapter.ViewModel {
  // Name of the restaurant
  private String name;

  // Getter for the name
  public String getName() {
    return name;
  }

  // what cuisine this restaurant serves
  private String cuisine;

  // Getter for the name
  public String getCuisine() {
    return cuisine;
  }

  public String getNandC() {
    return name + " " + cuisine; }

  private String id;

  public String getId() {
    return id;
  }

  // You will need to add more fields here...
  public static List<Restaurant> search(final List<Restaurant> restaurants, final String search) {
    if (restaurants == null || search == null) {
      throw new IllegalArgumentException();
    }

    List<Restaurant> toReturn = new ArrayList<>();
    Set<String> cuisines = new HashSet<>();
    String s = search.toLowerCase().trim();

    if (s.length() == 0) {
      toReturn.addAll(restaurants);
      return toReturn;
    }
    for (Restaurant r : restaurants) {
      cuisines.add(r.getCuisine());
    }

    if (cuisines.contains(s)) {
      for (Restaurant r : restaurants) {
        String c = r.getCuisine();
        if (c.equals(s)) {
          toReturn.add(r);
        }
      }
    } else {
      for (Restaurant r : restaurants) {
        String c = r.getCuisine().toLowerCase().trim();
        String n = r.getName().toLowerCase().trim();
        if (c.contains(s) || n.contains(s)) {
          toReturn.add(r);
        }
      }
    }
    return toReturn;
  }

  /*
   * The Jackson JSON serialization library we are using requires an empty constructor.
   * So don't remove this!
   */
  public Restaurant() {}

  /*
   * Function to compare Restaurant instances by name.
   * Currently this does not work, but you will need to implement it correctly for MP1.
   * Comparator is like Comparable, except it defines one possible ordering, not a canonical ordering for a class,
   * and so is implemented as a separate method rather than directly by the class as is done with Comparable.
   */
  public static final Comparator<Restaurant> SORT_BY_NAME =
      ((restaurant1, restaurant2) -> {
        return restaurant1.name.compareTo(restaurant2.name);
      });

  // You should not need to modify this code, which is used by the list adapter component
  @Override
  public <T> boolean isSameModelAs(@NonNull final T model) {
    return equals(model);
  }

  // You should not need to modify this code, which is used by the list adapter component
  @Override
  public <T> boolean isContentTheSameAs(@NonNull final T model) {
    return equals(model);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof Restaurant)) {
      return false;
    }
    Restaurant other = (Restaurant) o;
    return id.equals(other.id);
  }
}
