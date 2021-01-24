package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
/*
this is user controller
 */
@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * The method to create a new user from given UserEntity object
     *
     * @param userEntity: object from which new user will be created
     * @return UserEntity object
     * @Author: M.DHANUSRI
     */
    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

    /**
     * The method to find existing user by user name
     *
     * @param userName : will be searched in database for existing user
     * @return UserEntity object if user with requested user name exists in database
     * @Author: M.DHANUSRI
     */
    public UserEntity getUserByUserName(final String userName) {
        try {
            return entityManager.createNamedQuery("userByUserName", UserEntity.class).setParameter("userName", userName).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * The method to find existing user by email id
     *
     * @param email: will be searched in database for existing user
     * @return UserEntity object if user with requested email id exists in database
     * @Author: M.DHANUSRI
     */
    public UserEntity getUserByEmail(final String email) {
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * The method to create auth token of user
     *
     * @param userAuthTokenEntity : create the user authentication token and stored in db
     * @return usertoken
     * @Author: M.DHANUSRI
     */
    public UserAuthTokenEntity createAuthToken(final UserAuthTokenEntity userAuthTokenEntity) {
        entityManager.persist(userAuthTokenEntity);
        return userAuthTokenEntity;
    }

    /**
     * The method to update user entity
     *
     * @param updatedUserEntity : update user entity after creating user auth token
     * @Author: M.DHANUSRI
     */
    public void updateUser(final UserEntity updatedUserEntity) {
        entityManager.merge(updatedUserEntity);
    }

    /**
     * The method to get the user access token
     *
     * @param accessToken: will be searched in database for existing user
     * @Author: M.DHANUSRI
     */
    public UserAuthTokenEntity getUserAuthToken(final String accessToken) {
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthTokenEntity.class).setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Fetch a single user by given id from the DB.
     *
     * @param userUuid Id of the user whose information is to be fetched.
     * @return User details if exist in the DB else null.
     */
    public UserEntity getUserById(final String userUuid) {
        try {
            return entityManager
                    .createNamedQuery("userByUserUuid", UserEntity.class)
                    .setParameter("uuid", userUuid)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Delete a user by given id from the DB.
     *
     * @param userUuid Id of the user whose information is to be fetched.
     * @return User details which is to be deleted if exist in the DB else null.
     */
    public UserEntity deleteUser(final String userUuid) {
        UserEntity deleteUser = getUserById(userUuid);
        if (deleteUser != null) {
            this.entityManager.remove(deleteUser);
        }
        return deleteUser;
    }
}
