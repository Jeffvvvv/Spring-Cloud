package com.example.locationservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class LocationServiceController {

    //used for randomly define geoLocation
    private Random random = new Random();

    private Map<String, DriverLocations> locationsMap = new HashMap<>();

    @RequestMapping(value = "/drivers/{id}/locations", method = RequestMethod.POST)
    public ResponseEntity<Location> create(
            @PathVariable("id") String id,
            @RequestBody(required = false) Location inputLocation) {
        Location location;
        if (inputLocation == null) {
            location = new Location(random.nextInt(90), random.nextInt(90));
        } else {
            location = new Location(inputLocation.getLatitude(), inputLocation.getLongitude());
        }

        if (!locationsMap.containsKey(id)) {
            locationsMap.put(id, new DriverLocations(id));
        }

        locationsMap.get(id).addLocation(location);
        return new ResponseEntity<>(location, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/drivers/{id}/locations", method = RequestMethod.GET)
    public ResponseEntity<List<Location>> getAll(
            @PathVariable("id") String id) {
        if (!DriverController.isDriverValid(id))
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

        DriverLocations driverlocation = locationsMap.get(id);

        if (driverlocation == null)
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);

        return new ResponseEntity<>(driverlocation.getAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "drivers/{id}/locations/{locationId}", method = RequestMethod.GET)
    public ResponseEntity<Location> get(
            @PathVariable("id") String id,
            @PathVariable("locationId") String locationId) {
        Location location = null;

        if (!locationsMap.containsKey(id))
            return new ResponseEntity<>(location, HttpStatus.BAD_REQUEST);

        DriverLocations driverLocation = locationsMap.get(id);

        if (driverLocation.getLocation(Long.parseLong(locationId)) == null)
            return new ResponseEntity<>(location, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(driverLocation.getLocation(Long.parseLong(locationId)), HttpStatus.OK);
    }

    @RequestMapping(value = "drivers/{id}/locations/current", method = RequestMethod.GET)
    public ResponseEntity<Location> get(@PathVariable("id") String id) {
        Location location = null;
        if (!locationsMap.containsKey(id))
            return new ResponseEntity<>(location, HttpStatus.BAD_REQUEST);

        DriverLocations driverlocation = locationsMap.get(id);

        return new ResponseEntity<>(driverlocation.getLastLocation(), HttpStatus.OK);
    }

    @RequestMapping(value = "drivers/{id}/locations/{locationId}", method = RequestMethod.PUT)
    public ResponseEntity<Location> update(
            @RequestBody Location inputLocation,
            @PathVariable("id") String id,
            @PathVariable("locationId") String locationId) {
        Location location = null;

        if (!locationsMap.containsKey(id))
            return new ResponseEntity<>(location, HttpStatus.BAD_REQUEST);

        if (locationsMap.get(id).updateLocation(Long.parseLong(locationId), inputLocation))
            return new ResponseEntity<>(locationsMap.get(id).getLocation(Long.parseLong(locationId)), HttpStatus.OK);
        else
            return new ResponseEntity<>(location, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "drivers/{id}/locations/{locationId}", method = RequestMethod.DELETE)
    public ResponseEntity<Location> delete(
            @PathVariable("id") String id,
            @PathVariable("locationId") String locationId) {
        return this.deleteHelper(id, locationId);
    }

    private ResponseEntity<Location> deleteHelper(String id, String locationId) {
        Location location = null;

        if (!locationsMap.containsKey(id)) {
            return new ResponseEntity<>(location, HttpStatus.BAD_REQUEST);
        }

        DriverLocations driverlocation = locationsMap.get(id);

        if (driverlocation.deleteLocation(Long.parseLong(locationId)))
            return new ResponseEntity<>(location, HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(location, HttpStatus.BAD_REQUEST);
    }
}
