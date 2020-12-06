package com.myLearningProject.learning.user;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "capAm")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    private LoadingCache<Long, Optional<UserEntity>> cache;

    public UserController() {
        this.cache = CacheBuilder.newBuilder()
                .build(new CacheLoader<Long, Optional<UserEntity>>() {
                    @Override public Optional<UserEntity> load(Long key) throws Exception {
                        System.out.println("...Returning object from Database....for key "+key);
                        return userRepository.findById(key);
                    }
                });
    }

    @RequestMapping(method= RequestMethod.POST,path = "/user")
    public ResponseEntity save(@RequestBody UserEntity user) throws Exception {
        UserEntity response = userRepository.save(user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(method= RequestMethod.GET,path = "/user/{userId}")
    public ResponseEntity find(@PathVariable Long userId) throws Exception {
        Optional<UserEntity> response = cache.get(userId);
        if(response.isPresent())
            return new ResponseEntity<>(response.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}