package com.myLearningProject.learning.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.myLearningProject.learning.user.UserEntity;
import com.myLearningProject.learning.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class GuavaCache {

    @Autowired
     UserRepository userRepository;

    LoadingCache<Long, Optional<UserEntity>> loadingCacheByUserId;

    LoadingCache<String, List<UserEntity>> loadingCacheByUserName;

    public GuavaCache(){
       this.loadingCacheByUserId =  CacheBuilder
               .newBuilder()
               .maximumSize(1000)
                .expireAfterAccess(20, TimeUnit.MINUTES)
                .build(
                        new CacheLoader<Long, Optional<UserEntity>>() {
                            public Optional<UserEntity> load(Long id) throws Exception {
                                System.out.println("...Returning object from Database....for key " + id);
                                return userRepository.findById(id);
                            }
                        }
                );

       this.loadingCacheByUserName = CacheBuilder
               .newBuilder()
               .maximumSize(2)
               .expireAfterAccess(20, TimeUnit.MINUTES)
               .build(
                       new CacheLoader<String, List<UserEntity>>() {
                           @Override
                           public List<UserEntity> load(String userName) throws Exception {
                               System.out.println("...Returning object from Database....for key " + userName);
                               Iterable<UserEntity> allUsers =  userRepository.findAll();
                               List<UserEntity> matchedUsers = new ArrayList<>();
                               for(UserEntity user : allUsers){
                                   System.out.println(" Printing users " + user);
                                   if(user.getUserName() != null && user.getUserName().equals(userName)){
                                       System.out.println(" Printing not null users " + user);
                                       matchedUsers.add(user);
                                   }
                               }
                               return matchedUsers;
                           }
                       }
               );
    }


    public Optional<UserEntity> findByUserId(Long userId) throws Exception{
        return loadingCacheByUserId.get(userId);
    }

    public List<UserEntity> findByUserName(String userName) throws Exception{
        return loadingCacheByUserName.get(userName);
    }
}
