package myprojectmessenger.entity;

import javax.persistence.*;

@Entity
@Table(name = "message_state")
public class MessageState {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private boolean state;
    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
