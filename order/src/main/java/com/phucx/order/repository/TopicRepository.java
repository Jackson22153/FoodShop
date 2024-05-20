package com.phucx.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.order.model.Topic;


@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer>{
    
}
