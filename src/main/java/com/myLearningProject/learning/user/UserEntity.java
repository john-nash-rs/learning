package com.myLearningProject.learning.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Where(clause="is_deleted=0")
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String userName;
    private String emailId;
    private String password;
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
    private Date createdAt = new Date();
    private Date updatedAt;
}