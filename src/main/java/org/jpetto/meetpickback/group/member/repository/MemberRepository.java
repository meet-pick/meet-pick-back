package org.jpetto.meetpickback.group.member.repository;

import org.jpetto.meetpickback.account.account.entity.Account;
import org.jpetto.meetpickback.group.group.entity.Group;
import org.jpetto.meetpickback.group.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByMemberAccountAndGroup(Account account, Group group);
    Optional<Member> findByMemberAccountAndGroup(Account account, Group group);
}
