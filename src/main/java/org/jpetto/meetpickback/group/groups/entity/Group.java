package org.jpetto.meetpickback.group.groups.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.jpetto.meetpickback.global.jpa.BaseEntity;
import org.jpetto.meetpickback.group.members.entity.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "moim_group") // postgreSQL은 group이 예약어로 존재
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Group extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member> members = new ArrayList<>();

    // members null 값 방지
    public List<Member> getMembers() {
        if (this.members == null) {
            this.members = new ArrayList<>();
        }
        return this.members;
    }
}
