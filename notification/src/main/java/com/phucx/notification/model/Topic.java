package com.phucx.notification.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Topics")
public class Topic {
    @Id
    private Integer topicID;
    private String topicName;
    public Topic(String topicName) {
        this.topicName = topicName;
    }
}
