package com.example.virtualvet.model;

import com.example.virtualvet.enums.Type;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
public class Message {


    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "message", length = 1000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Column(name = "create_time", nullable = false, updatable = false)
    @CreationTimestamp
    private OffsetDateTime createTime;

    public Message(String message, Type type) {
        this.message = message;
        this.type = type;
    }
}