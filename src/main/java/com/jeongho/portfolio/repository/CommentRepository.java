package com.jeongho.portfolio.repository;

import com.jeongho.portfolio.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
