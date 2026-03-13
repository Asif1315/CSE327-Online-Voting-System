package org.example.onlinevotingsystem.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    public Notification(String message, User recipient, Poll relatedPoll, NotificationType type) {
        this.message = message;
        this.recipient = recipient;
        this.relatedPoll = relatedPoll;
        this.timestamp = LocalDateTime.now();
        this.type = type;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "is_read")
    private boolean read = false;

    @ManyToOne
    @JoinColumn(name = "nid")
    private User recipient;

    @ManyToOne
    @JoinColumn(name = "PollID")
    private Poll relatedPoll;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private NotificationType type;

    public enum NotificationType {
        POLL_CREATED, POLL_UPDATED, NEW_USER
    }



}
