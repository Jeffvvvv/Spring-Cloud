package com.example.locationservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class DriverLocations {
    private long currentId;
    private Map<Long, Location> locations;
    private String DriverId;

    //constructor
    public DriverLocations(String DriverId)
    {
        this.DriverId = DriverId;
        locations = new HashMap<Long, Location>();
        currentId = 0;
    }

    public void addLocation(Location location)
    {
        long id = ++currentId;
        location.setId(id);
        locations.put(id, location);
    }

    public List<Location> getAll()
    {
        return new ArrayList<Location>(locations.values());
    }

    public Location getLastLocation()
    {
        return locations.get(currentId);
    }

    public Location getLocation(long locationId)
    {
        if(!locations.containsKey(locationId))
            return null;

        return locations.get(locationId);
    }

    public boolean updateLocation(long locationId, Location location)
    {
        if(!locations.containsKey(locationId))
            return false;

        Location targetLocation = locations.get(locationId);
        targetLocation.setLatitude(location.getLatitude());
        targetLocation.setLongitude(location.getLongitude());
        return true;
    }

    public boolean deleteLocation(long locationId)
    {
        if(!locations.containsKey(locationId))
            return false;

        locations.remove(locationId);
        return true;
    }
}
