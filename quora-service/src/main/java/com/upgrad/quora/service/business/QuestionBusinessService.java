package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionBusinessService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserDao userDao;

    /**
     * Used to create a question in the Quora Application which will be shown to all the users.
     *
     * If the access token provided by the user does not exist in the database throw "AuthorizationFailedException" with the message code - 'ATHR-001' and message - 'User has not signed in'.
     * If the user has signed out, throw 'AuthorizationFailedException' with the message code- 'ATHR-002' and message -'User is signed out.Sign in first to post a question'.
     *
     * @Author:M.DHANUSRI
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(QuestionEntity questionEntity, final String authorizationToken) throws AuthorizationFailedException {
        UserAuthTokenEntity userAuthEntity = userDao.getUserAuthToken(authorizationToken);

        // Validate if user is signed in or not
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        // Validate if user has signed out
        if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
        }

        questionEntity.setUserEntity(userAuthEntity.getUser());
        return questionDao.createQuestion(questionEntity);
    }

    /**
     * Used to fetch all the questions that have been posted in the application by any user.
     *
     * If the access token provided by the user does not exist in the database throw 'AuthorizationFailedException' with the message code - 'ATHR-001' and message - 'User has not signed in'.
     * If the user has signed out, throw 'AuthorizationFailedException' with the message code-'ATHR-002' and message-'User is signed out.Sign in first to get all questions'.
     *
     * @Author:M.DHANUSRI
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestions(final String authorization) throws AuthorizationFailedException {
        UserAuthTokenEntity userAuthEntity = userDao.getUserAuthToken(authorization);

        // Validate if user is signed in or not
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        // Validate if user has signed out
        if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions");
        }

        return questionDao.getAllQuestions();
    }

    /**
     * * Edit the question
     *
     * @param authorization accessToken of the user for valid authentication.
     * @param questionId    id of the question to be edited.
     * @param content       new content for the existing question.
     * @return QuestionEntity
     * @throws AuthorizationFailedException ATHR-001 - if user token is not present in DB. ATHR-002 if
     *                                      the user has already signed out.
     * @throws InvalidQuestionException     if the question with id doesn't exist.
     * @Author:M.DHANUSRI
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity editQuestion(final String authorization, final String questionId, final String content) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthTokenEntity userAuthEntity = userDao.getUserAuthToken(authorization);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        // Validate if user has signed out
        if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit the question");
        }

        QuestionEntity questionEntity = questionDao.getQuestionById(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }

        if (!questionEntity.getUserEntity().getUuid().equals(userAuthEntity.getUser().getUuid())) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        }
        questionEntity.setContent(content);
        questionDao.updateQuestion(questionEntity);

        return questionEntity;
    }

    /**
     * * Delete the question
     *
     * @param authorization accessToken of the user for valid authentication.
     * @param questionId    id of the question to be edited.
     * @return QuestionEntity
     * @throws AuthorizationFailedException ATHR-001 - if user token is not present in DB. ATHR-002 if
     *                                      the user has already signed out.
     * @throws InvalidQuestionException     if the question with id doesn't exist.
     * @Author:M.DHANUSRI
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity deleteQuestion(final String authorization, final String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthTokenEntity userAuthEntity = userDao.getUserAuthToken(authorization);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        // Validate if user has signed out
        if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete a question");
        }

        QuestionEntity questionEntity = questionDao.getQuestionById(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }

        if (!questionEntity.getUserEntity().getUuid().equals(userAuthEntity.getUser().getUuid())
                || !userAuthEntity.getUser().getRole().equals("admin")) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");
        }

        questionDao.deleteQuestion(questionEntity);

        return questionEntity;

    }

    /**
     * Gets all the questions posted by a specific user.
     *
     * @param userId userId of the user whose posted questions have to be retrieved
     * @param accessToken accessToken of the user for valid authentication.
     * @return List of QuestionEntity
     * @throws AuthorizationFailedException ATHR-001 - if user token is not present in DB. ATHR-002 if
     *     the user has already signed out.
     * @throws UserNotFoundException  USR-001 - if user doesn't have any question
     * @Author:M.DHANUSRI
     */
    public List<QuestionEntity> getAllQuestionsByUser(final String userId, final String accessToken)
            throws AuthorizationFailedException, UserNotFoundException {
        UserAuthTokenEntity userAuthEntity = userDao.getUserAuthToken(accessToken);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException(
                    "ATHR-002",
                    "User is signed out.Sign in first to get all questions posted by a specific user");
        }
        UserEntity user = userDao.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(
                    "USR-001", "User with entered uuid whose question details are to be seen does not exist");
        }
        return questionDao.getAllQuestionsByUser(user);
    }

}

