package com.example.webapp.service.impl;


import com.example.webapp.DTO.QnaDTO;
import com.example.webapp.DTO.request.QnaAnswerRequestDTO;
import com.example.webapp.DTO.request.QnaRequestDTO;
import com.example.webapp.common.annotations.InjectUserEntity;
import com.example.webapp.common.context.UserContext;
import com.example.webapp.entity.Qna;
import com.example.webapp.entity.Role;
import com.example.webapp.entity.User;
import com.example.webapp.repository.QnaRepository;
import com.example.webapp.service.QnaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QnaServiceImpl implements QnaService {

    private final QnaRepository qnaRepository;

    @Override
    @InjectUserEntity
    public QnaDTO saveQna(QnaRequestDTO request) {

        User user = UserContext.getCurrentUser();

        Qna qna = Qna.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .owner(user)
                .build();
        qnaRepository.save(qna);

        return QnaDTO.from(qna);

    }

    @Override
    @InjectUserEntity
    @Transactional
    public Page<QnaDTO> getQnaList(Pageable pageable) {

        User user = UserContext.getCurrentUser();

        Page<Qna> result;

        if(user.getRole().isAdmin()){
            result = qnaRepository.findAll(pageable);
        }
        else {
            result = qnaRepository.findAllByOwner(user, pageable);
        }

        return result.map(QnaDTO :: from);
    }

    @Override
    @InjectUserEntity
    public QnaDTO getQna(Long id){

        User user = UserContext.getCurrentUser();

        Qna qna = qnaRepository.findByIdWithOwner(id)
                .orElseThrow(()-> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        if(qna.getOwner()!=user||!user.getRole().isAdmin()){
            throw new AccessDeniedException("접근 권한이 없는 게시글 입니다.");
        }

        return QnaDTO.from(qna);
    }

    @Override
    @Transactional
    @InjectUserEntity
    public QnaDTO answerQna(Long id, QnaAnswerRequestDTO request) {

        User user = UserContext.getCurrentUser();

        if(user.getRole()!= Role.ADMIN){
            throw new AccessDeniedException("권한이 부족합니다.");
        }

        Qna qna = qnaRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException(
                        "게시글을 찾을 수 없습니다."
                ));

        qna.setAnswer(request.getAnswer());
        qna.setIsAnswered(true);

        return QnaDTO.from(qna);
    }

    @Override
    @InjectUserEntity
    @Transactional
    public boolean deleteQna(Long id){
        User user = UserContext.getCurrentUser();

        Qna qna = qnaRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException(
                        "게시글을 찾을 수 없습니다."
                ));

        if(qna.getOwner()!=user||!user.getRole().isAdmin()){
            throw new AccessDeniedException("권한이 부족합니다.");
        }

        qnaRepository.deleteById(id);

        return true;
    }
}
