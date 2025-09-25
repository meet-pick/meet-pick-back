package org.jpetto.meetpickback.group.group.repository;

import org.jpetto.meetpickback.group.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

}
