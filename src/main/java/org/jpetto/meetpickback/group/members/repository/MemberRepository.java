package org.jpetto.meetpickback.group.members.repository;

import org.jpetto.meetpickback.account.accounts.entity.Account;
import org.jpetto.meetpickback.group.groups.entity.Group;
import org.jpetto.meetpickback.group.members.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByMemberAccountAndGroup(Account account, Group group);
    Optional<Member> findByMemberAccountAndGroup(Account account, Group group);
}
