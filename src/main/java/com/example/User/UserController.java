package com.example.User;


import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;

import java.util.*;

@RestController
public class UserController {

    List <User> users = new ArrayList<>();
    UserRepository userRepository;


    @PostMapping("users")
    public ResponseEntity<Void> createUser(@RequestBody CreateUser newUser){
        if(!newUser.getPassword().equals(newUser.getRepeatPassword())){
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).build();
        }
        for(User user: users){
            if(!Objects.equals(user.getUsername(), newUser.getUsername())){
                return ResponseEntity.status(HttpStatusCode.valueOf(409)).build();
            }
        }
        User user = new User(newUser.getUsername(), newUser.getPassword(), newUser.getAge());
        users.add(user);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("users")
    public  ResponseEntity<List<UserInfo>> getUsers(){
        List <UserInfo> userList = new ArrayList<>();
        for(User user : users){
            userList.add(new UserInfo(user.getUsername(), user.getAge()));
        }
        return ResponseEntity.ok(userList);
    }

    @GetMapping("users/{id}")
    public ResponseEntity<UserInfo> getUser(@PathVariable("id") Integer id){
        if(id<0 || id>users.size()-1){
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).build();
        }
        UserInfo userInfo = new UserInfo(users.get(id).getUsername(), users.get(id).getAge());
        return ResponseEntity.ok(userInfo);
    }

    @DeleteMapping("users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable ("id") Integer id){
        if(id<0 || id>users.size()-1){
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).build();
        }
        users.remove(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("users/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("id") Integer id, @RequestBody CreateUser newUser){
        if(id<0 || id>users.size()-1){
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).build();
        }
        if(!newUser.getPassword().equals(newUser.getRepeatPassword())){
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).build();
        }
        User user = new User(newUser.getUsername(), newUser.getPassword(), newUser.getAge());
        users.set(id, user);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("users/{age}")
    public ResponseEntity <List<UserInfo>> getUsersAge(@PathVariable Integer age){
        List <UserInfo> userInfos = new ArrayList<>();
        for(User user : users){
            if(Math.abs(user.getAge()-age)<=5){
                userInfos.add(new UserInfo(user.getUsername(), user.getAge()));
            }
        }
        return ResponseEntity.ok(userInfos);
    }

    @GetMapping("users")
    public ResponseEntity <List<String>> getUsers(@RequestParam String sortBy,
                                                  @RequestParam String direction){
        String sortDirection;
        if(direction.equals("up")){
            sortDirection = "ASC";
        } else {
            sortDirection = "DESC";
        }
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        List <User> sortingUsers = userRepository.findAll(sort);
        List <String> username = new ArrayList<>();
        for(User user : sortingUsers){
            username.add(user.getUsername());
        }
        return ResponseEntity.ok(username);
    }


    @GetMapping("users")
    public  ResponseEntity<List<UserInfo>> getPagingUsers(@RequestParam(value = "numberPage", defaultValue = "0") Integer numberPage,
                                                          @RequestParam(value = "limitPage", defaultValue = "5") Integer limitPage){
        List <UserInfo> list = new ArrayList<>();
        int fromID = limitPage*numberPage;
        if(fromID>users.size() -1){
            return ResponseEntity.ok(new ArrayList<>());
        }
        List <User> subUser = users.subList(fromID, Math.max(users.size()-1, fromID+limitPage-1));
        for(User user:subUser){
            list.add(new UserInfo(user.getUsername(), user.getAge()));
        }
        return ResponseEntity.ok(list);
    }
}
