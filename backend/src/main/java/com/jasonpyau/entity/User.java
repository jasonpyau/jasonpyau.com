package com.jasonpyau.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "address", unique = true)
    private String address;
    @Column(name = "liked_blogs")
    @ManyToMany(mappedBy = "likedUsers", fetch = FetchType.LAZY)
    private Set<Blog> likedBlogs = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof User) || this.address == null) {
            return false;
        }
        User other = (User)o;
        return this.address.equals(other.address);
    }
    
    @Override
    public int hashCode() {
        if (address == null) {
            return 0;
        }
        return address.hashCode();
    }
}
