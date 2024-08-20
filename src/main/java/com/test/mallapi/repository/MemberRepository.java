package com.test.mallapi.repository;

import com.test.mallapi.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;




public interface MemberRepository extends JpaRepository<Member, String> {
    
    //  조회 시에 권한의 목록까지 같이 로딩하도록 설정
    @EntityGraph(attributePaths = {"memberRoleList"})
    @Query("select m from Member m where m.email = :email")
    Member getWithRoles(@Param("email") String email);


    @EntityGraph(attributePaths = {"memberRoleList"})
    Page<Member> findAll(Pageable pageable);

}
