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
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "chat_id", nullable = false)
    private List<Message> messages;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    // TODO: czy po usunięciu peta powinny usuwać się też chaty? Jak na moje to nie
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    // TODO: dlaczego bez tej linijki usuwanie użytkownika skutkuje usunięciem też wszytskiuch wiadomości z nim powiązanych?
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
        if (messages.stream().anyMatch(foundMessage -> Objects.equals(foundMessage.getId(), message.getId()))) {
            return false;
        }
        messages.add(message);
        return true;
    }

    public String getResponse() {
        Message response = new Message(getResponse(), Type.RESPONSE);
        messages.add(response);
        return new StubVetResponder().ask(); //TODO: how to use VetResponder?
    }

}