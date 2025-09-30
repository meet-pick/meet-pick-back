package org.jpetto.meetpickback.account.friends.repository;

import org.jpetto.meetpickback.account.accounts.entity.Account;
import org.jpetto.meetpickback.account.friends.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    @Query(""" 
            SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END
                        FROM Friend f
                        WHERE (f.account = :account AND f.friendAccount = :friend)
                        OR (f.account = :friend AND f.friendAccount = :account)
            """)
    boolean existsBetween(@Param("account") Account account,
                          @Param("friend") Account friend);

    @Query("""
            SELECT f
            FROM Friend f
            WHERE (f.account = :account OR f.friendAccount = :account)
            """)
    List<Friend> findAllFriendsOf(@Param("account") Account account);


}
