package com.jasonpyau.repository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jasonpyau.Application;
import com.jasonpyau.entity.Blog;
import com.jasonpyau.entity.User;
import com.jasonpyau.util.Hash;

import jakarta.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes=Application.class)
public class BlogRepositoryTest {

    @Autowired
    private BlogRepository blogRepository;
    private Blog[] blogs;
    private final String hashedAddress = Hash.SHA256("localhost");
    private final User user = new User();

    @BeforeEach
    public void setUp() {
        user.setAddress(hashedAddress);
        int n = 6;
        blogs = new Blog[n];
        Long unixTime = System.currentTimeMillis()/1000;
        for (int i = 0; i < n; i++) {
            Blog blog = new Blog();
            blog.setTitle("test"+i);
            blog.setBody("this is the body of test"+i);
            blog.setDate("06/25/2023");
            blog.setLikeCount(0);
            blog.setViewCount(0L);
            blog.setUnixTime(unixTime-i);
            if (i%2 == 0) {
                blog.like(user);
            }
            blogs[i] = blog;
            blogRepository.save(blog);
        }
    }

    @AfterEach
    public void tearDown() {
        blogs = null;
        blogRepository.deleteAll();
    }

    @Test
    void testFindAllWithPaginationOrderedByUnixTime() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Blog> page = blogRepository.findLikedBlogsWithPaginationOrderedByUnixTime(pageable, hashedAddress);
        List<Blog> list = page.getContent();
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i).getTitle(), blogs[i*2].getTitle());
            assertEquals(list.get(i).getBody(), blogs[i*2].getBody());
            assertEquals(list.get(i).getDate(), blogs[i*2].getDate());
            assertEquals(list.get(i).getLikeCount(), blogs[i*2].getLikeCount());
            assertEquals(list.get(i).getViewCount(), blogs[i*2].getViewCount());
            assertEquals(list.get(i).getUnixTime(), blogs[i*2].getUnixTime());
        }
    }

    @Test
    void testFindLikedBlogsWithPaginationOrderedByUnixTime() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Blog> page = blogRepository.findAllWithPaginationOrderedByUnixTime(pageable);
        List<Blog> list = page.getContent();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).checkIsLikedByUser(user);
            assertEquals(list.get(i).getTitle(), blogs[i].getTitle());
            assertEquals(list.get(i).getBody(), blogs[i].getBody());
            assertEquals(list.get(i).getDate(), blogs[i].getDate());
            assertEquals(list.get(i).getLikeCount(), blogs[i].getLikeCount());
            assertEquals(list.get(i).getViewCount(), blogs[i].getViewCount());
            assertEquals(list.get(i).getUnixTime(), blogs[i].getUnixTime());
            assertEquals(list.get(i).getIsLikedByUser(), i%2 == 0);
        }
    }

}
