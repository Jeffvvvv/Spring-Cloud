package com.example.accountservice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
public class AccountController {
    //Autowired 如果用在这的话，当程序跑起来创建AccountController的时候
    // SpringFramework会自动inject AccountRespository到AccountController
    //
    @Autowired
    private AccountRepository accountRespoity;



    //Post Request : /accounts: create a new account
    //RequestBody needs to provide a new account?
    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public ResponseEntity<Account> create(
            @RequestBody(required = false) Account account) {

        accountRespoity.save(account);

        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    //Get Request: /accounts: return all the accounts and corresponding info
    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public ResponseEntity<List<Account>> getAll(){
        List<Account> accounts = new ArrayList<>();

        accountRespoity.findAll().iterator().forEachRemaining(account -> accounts.add(account));

        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    //Get Request: /accounts/{id}: return an account by id
    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.GET)
    public ResponseEntity<Account> get(
            @PathVariable("id") String id){
        Account account = null;

        Optional<Account> optional = accountRespoity.findById(Long.parseLong(id));

        if(!optional.isPresent())
            return new ResponseEntity<>(account, HttpStatus.NOT_FOUND);

        account = optional.get();

        return new ResponseEntity<>(account, HttpStatus.OK);
    }


    //Get Request: /account/find : return an account by first name and last name
    @RequestMapping(value = "/accounts/find", method = RequestMethod.GET)
    public ResponseEntity<List<Account>> findByName(
            @RequestParam(value = "firstname", defaultValue = "") String firstName,
            @RequestParam(value = "lastname", defaultValue = "") String lastName){

        List<Account> accounts = new ArrayList<>();

        if(firstName == "" && lastName == "")
            return new ResponseEntity<>(accounts, HttpStatus.BAD_REQUEST);

        if(firstName == "")
            accountRespoity.findByLastName(lastName).iterator().forEachRemaining(account -> accounts.add(account));
        else if(lastName == "")
            accountRespoity.findByFirstName(firstName).iterator().forEachRemaining(account -> accounts.add(account));
        else
            accountRespoity.findByFirstNameAndLastName(firstName, lastName).iterator().forEachRemaining(account -> accounts.add(account));

        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }


}
