package com.upgrad.quora.service.DAO;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDao {
    @PersistenceContext
    private EntityManager entityManager;
    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }
    public AnswerEntity getAnswerById(final String answerId) {
        try {
            return entityManager
                    .createNamedQuery("getAnswerByUuid", AnswerEntity.class)
                    .setParameter("uuid", answerId)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    public void updateAnswer(AnswerEntity answerEntity) {
        entityManager.merge(answerEntity);
    }
    public void performDeleteAnswer(final String answerId) {
        AnswerEntity answerEntity = getAnswerById(answerId);
        entityManager.remove(answerEntity);
    }
    public List<AnswerEntity> getAllAnswersToQuestion(final String questionId) {
        return entityManager
                .createNamedQuery("getAllAnswersToQuestion", AnswerEntity.class)
                .setParameter("uuid", questionId)
                .getResultList();
    }
}
