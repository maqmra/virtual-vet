package com.example.virtualvet;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "chats")
@Getter
@Setter
public class Chat {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "chat_id", nullable = false)
    private List<Message> messages;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Chat(User user, Pet pet) {
        this.user = user;
        this.pet = pet;
        this.messages = new ArrayList<>();
    }

    public Chat() {
        this.messages = new ArrayList<>();
    }

    public boolean addMessage(Message message) {
        if (messageExist(message)) {
            return false;
        }
        messages.add(message);
        return true;
    }

    private boolean messageExist(Message message) {
        if (message.getId() == null) {
            return false;
        }
        return messages.stream().anyMatch(foundMessage -> Objects.equals(foundMessage.getId(), message.getId()));
    }
}
