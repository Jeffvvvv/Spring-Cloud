package com.example.locationservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class DriverController {

    //这个类是用来处理所有关于Driver信息的rest API接口
    //要实现这三个request的APT接口
    //Get /drivers, return all the drivers information
    //Post /drivers, create a new driver and automatically increase the id
    //Get /drivers/{id}, return a driver information by ID


    //这个是用来定义Driver ID的，一旦定义了之后，就不能改了
    private final AtomicLong counter = new AtomicLong();

    //另外，用一个hashmap来存drivers的信息
    private static Map<String, Driver> drivers = new HashMap<>();

    //留一个疑问，如何去parse parameters，如果request里面提供了FirstName和LastName
    @RequestMapping(value = "/drivers", method = RequestMethod.POST)
    public ResponseEntity<Driver> create(
            @RequestBody(required = false) Driver driver)
    {
        driver = new Driver();
        long id = counter.incrementAndGet();
        driver.setId(id);

        drivers.put(String.valueOf(id), driver);
        return new ResponseEntity<>(driver, HttpStatus.CREATED);
    }


    @RequestMapping(value = "/drivers", method = RequestMethod.GET)
    public ResponseEntity<List<Driver>> getAll()
    {
        return new ResponseEntity<>(new ArrayList<>(drivers.values()), HttpStatus.OK);
    }

    @RequestMapping(value = "/drivers/{id}", method = RequestMethod.GET)
    public ResponseEntity<Driver> get(@PathVariable("id") String id)
    {
        Driver driver = null;
        if(!drivers.containsKey(id))
            return new ResponseEntity<>(driver, HttpStatus.BAD_REQUEST);

        driver = drivers.get(id);
        return new ResponseEntity<>(driver, HttpStatus.OK);
    }

    public static boolean isDriverValid(String id){return drivers.containsKey(id);}
}
