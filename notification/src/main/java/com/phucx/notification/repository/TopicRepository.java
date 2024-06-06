package com.phucx.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.notification.model.Topic;


@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer>{
    
}
