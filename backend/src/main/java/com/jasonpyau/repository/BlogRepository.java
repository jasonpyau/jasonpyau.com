package com.jasonpyau.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jasonpyau.entity.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    @Query(value = "SELECT * FROM blogs ORDER BY unix_time DESC", nativeQuery = true)
    public Page<Blog> findAllWithPaginationOrderedByUnixTime(Pageable pageable);
    @Query(value = "SELECT b.id, b.body, b.date, b.like_count, b.title, b.unix_time, b.view_count FROM blogs b "
            + "LEFT JOIN (blog_user b_u JOIN users u on u.id=b_u.user_id) ON b.id=b_u.blog_id WHERE u.address=:address "
            + "ORDER BY b.unix_time DESC", nativeQuery = true)
    public Page<Blog> findLikedBlogsWithPaginationOrderedByUnixTime(Pageable pageable, @Param("address") String address);
}
