package com.phucx.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.account.model.Topic;


@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer>{
    
}
