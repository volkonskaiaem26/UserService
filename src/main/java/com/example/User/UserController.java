package com.example.User;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class UserController {

    List <User> users = new ArrayList<>();

    @GetMapping("users/{age}")
    public ResponseEntity <List<String>> getUsers(@PathVariable Integer age){
        List <String> username = new ArrayList<>();
        for(User user : users){
            if(Math.abs(user.getAge()-age)<=5){
                username.add(user.getUsername());
            }
        }
        return ResponseEntity.ok(username);
    }

    @PostMapping("users/create/{username}?{password}/{age}")
    public ResponseEntity<Void> createUser(@PathVariable String username, @PathVariable String password, @PathVariable Integer age,
                                           @RequestBody String repeatPassword){
        User newUser = new User(username,password,age);
        if(!Objects.equals(newUser.getPassword(), repeatPassword)){
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).build();
        }
        for(User user: users){
            if(Objects.equals(user.getUsername(), newUser.getUsername())){
                return ResponseEntity.status(HttpStatusCode.valueOf(409)).build();
            }
        }
        users.add(newUser);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("users/{id}")
    public ResponseEntity<String> getUsername(@PathVariable Integer id){
        if(id<0 || id>users.size()-1){
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).build();
        }
        return ResponseEntity.ok(users.get(id).getUsername());
    }

    @DeleteMapping("users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id){
        if(id<0 || id>users.size()-1){
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).build();
        }
        users.remove(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("users/{id}/{repeatPassword}")
    public ResponseEntity<Void> updateUser(@PathVariable Integer id,@PathVariable String repeatPassword, @RequestBody User newUser){
        if(id<0 || id>users.size()-1){
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).build();
        }
        if(!Objects.equals(users.get(id).getPassword(), repeatPassword)){
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).build();
        }
        users.set(id, newUser);
        return ResponseEntity.accepted().build();
    }

}
