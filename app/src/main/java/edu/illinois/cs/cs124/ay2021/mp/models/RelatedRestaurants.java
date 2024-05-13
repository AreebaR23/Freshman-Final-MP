package edu.illinois.cs.cs124.ay2021.mp.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.Comparator;

public class RelatedRestaurants {
  private final Map<String, Map<String, Integer>> restaurantRelationships = new HashMap<>();
  private final Map<String, Restaurant> restaurantMap  = new HashMap<>();
  private Set<String> getRes = new HashSet<>();

  public RelatedRestaurants(final List<Restaurant> restaurants, final List<Preference> preferences) {
    for (Restaurant r : restaurants) {
      getRes.add(r.getId());
    }
    for (Restaurant res: restaurants) {
      restaurantMap.put(res.getId(), res);
    }
    for (Preference p : preferences) {
      List<String> resIds = p.getRestaurantIDs();
      for (String r1 : resIds) {
        for (String r2 : resIds) {
          if (getRes.contains(r1) && getRes.contains(r2) && !r1.equals(r2)) {
            Map<String, Integer> inner = restaurantRelationships.get(r1);
            if (!restaurantRelationships.containsKey(r1)) {
              Map<String, Integer> temp = new HashMap<>();
              temp.put(r2, 1);
              restaurantRelationships.put(r1, temp);
            } else if (restaurantRelationships.containsKey(r1) && !inner.containsKey(r2)) {
              Map<String, Integer> temp2 = restaurantRelationships.get(r1);
              temp2.put(r2, 1);
              restaurantRelationships.put(r1, temp2);
            } else if (restaurantRelationships.containsKey(r1) && inner.containsKey(r2)) {
              Map<String, Integer> temp3 = restaurantRelationships.get(r1);
              temp3.put(r2, 1 + temp3.get(r2));
              restaurantRelationships.put(r1, temp3);
            }
          }
        }
      }
    }
  }
  public Map<String, Integer> getRelated(final String restaurantID) {
    Map<String, Integer> returnMap = restaurantRelationships.get(restaurantID);
    if (returnMap != null) {
      return returnMap;
    } else {
      return new HashMap<>();
    }
  }
  public List<Restaurant> getRelatedInOrder(final String restaurantID) {
    //Check RestaurantId
    //retrieve the related Restaurants
    //Convert the list of restaurant ids to a List of restaurants
    //Sort it properly: based on relationship strength and name
    if (restaurantID.length() <= 0 || restaurantID == null || !getRes.contains(restaurantID)) {
      throw new IllegalArgumentException();
    }
    Map<String, Integer> relatedRestaurantIds = getRelated(restaurantID);
    List<Restaurant> relres = new ArrayList<>();
    for (String res: relatedRestaurantIds.keySet()) {
      relres.add(restaurantMap.get(res));
    }
    Comparator<Restaurant> sortRelStrength = (r1, r2) -> {
      if ((relatedRestaurantIds.get(r1.getId()) - relatedRestaurantIds.get(r2.getId())) == 0) {
        return r1.getName().compareTo(r2.getName());
      } else {
        return relatedRestaurantIds.get(r2.getId()) - relatedRestaurantIds.get(r1.getId());
      }
    };
    Collections.sort(relres, sortRelStrength);
    return relres;
  }
  private String dummy;
  public Set<Restaurant> getConnectedTo(final String restaurantID) {
    if (restaurantID.length() <= 0 || restaurantID == null || !getRes.contains(restaurantID)) {
      throw new IllegalArgumentException();
    }
    Map<String, Integer> rels = getRelated(restaurantID);
    Set<String> seen = new HashSet<>();
    hopCount(restaurantID, seen, 2);
    seen.remove(restaurantID);
    Set<Restaurant> toReturn = new HashSet<>();
    for (String s: seen) {
      toReturn.add(restaurantMap.get(s));
    }
    return toReturn;
  }
  private void hopCount(final String node, final Set<String> seen, final int distance) {
    seen.add(node);
    if (distance == 0) {
      return;
    }
    Map<String, Integer> neighbors = getRelated(node);
    for (String neighbor: neighbors.keySet()) {
      if (neighbors.get(neighbor) > 1) {
        hopCount(neighbor, seen, distance - 1);
      }
    }
  }
}
