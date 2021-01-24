package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * The method to create a new user from given UserEntity object
     *
     * @param questionEntity: object from which new question will be created
     * @return UserEntity object
     * @Author: M.DHANUSRI
     */

    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    /**
     * The method to create a new user from given UserEntity object
     *
     * @return list of all questions..
     * @Author: M.DHANUSRI
     */

    public List<QuestionEntity> getAllQuestions() {
        try {
            return entityManager.createNamedQuery("allQuestions", QuestionEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Get the question for the given id.
     *
     * @param questionId id of the required question.
     * @return QuestionEntity if question with given id is found else null.
     * @Author: M.DHANUSRI
     */
    public QuestionEntity getQuestionById(final String questionId) {
        try {
            return entityManager
                    .createNamedQuery("getQuestionById", QuestionEntity.class)
                    .setParameter("uuid", questionId)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Update the question
     *
     * @param questionEntity question entity to be updated.
     * @Author: M.DHANUSRI
     */
    public void updateQuestion(QuestionEntity questionEntity) {
        try {
            entityManager.merge(questionEntity);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * Delete the question
     *
     * @param questionEntity question entity to be deleted.
     * @Author: M.DHANUSRI
     */
    public void deleteQuestion(QuestionEntity questionEntity) {
        entityManager.remove(questionEntity);
    }

    /**
     * Fetch all the questions from the DB.
     *
     * @param userId userId of the user whose list of asked questions has to be retrieved
     * @return List of QuestionEntity
     * @Author: M.DHANUSRI
     */
    public List<QuestionEntity> getAllQuestionsByUser(final UserEntity userId) {
        return entityManager
                .createNamedQuery("getQuestionByUser", QuestionEntity.class)
                .setParameter("user", userId)
                .getResultList();
    }

    /**
     * Method will get the question using the uuid entered by the user and return result
     * if no results exception will be thrown..
     * @param uuid  of the question
     * @Author: M.DHANUSRI
     * */
    public QuestionEntity getQuestionByUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("questionEntityByUuid", QuestionEntity.class).setParameter("uuid",uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
