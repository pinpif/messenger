package myprojectmessenger.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "added_date")
    private Date addedDate;
    @Column(name = "session_id")
    private String sessionId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
