package org.jpetto.meetpickback.group.group.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.jpetto.meetpickback.global.jpa.BaseEntity;

@Entity
@Table(name = "moim_group") // postgreSQL은 group이 예약어로 존재
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Group extends BaseEntity {

}
