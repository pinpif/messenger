package myprojectmessenger.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private int age;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "status_id")
    private UserStatus status;

    @OneToMany(mappedBy = "author")
    private List<Chat> chatList;

    @OneToMany(mappedBy = "user")
    private List<BlockList> blockLists;
}
