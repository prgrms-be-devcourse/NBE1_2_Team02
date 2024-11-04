package com.example.book_your_seat.user.domain;

import com.example.book_your_seat.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String nickname;

    private String username;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @ElementCollection
    @CollectionTable(
            name = "address",
            joinColumns = @JoinColumn(name = "user_id")
    )
    private final List<Address> addressList = new ArrayList<>();


    public User(String nickname, String username, String email, String password) {
        this.nickname = nickname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.userRole = UserRole.USER;
    }

    public void changeRoleToAdmin() {
        this.userRole = UserRole.ADMIN;
    }

    public void addAddress(Address address) {
        this.addressList.add(address);
    }

}
