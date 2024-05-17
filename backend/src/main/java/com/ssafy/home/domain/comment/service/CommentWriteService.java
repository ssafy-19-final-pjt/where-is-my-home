package com.ssafy.home.domain.comment.service;

import com.ssafy.home.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentWriteService {
    private final CommentRepository commentRepository;
}
