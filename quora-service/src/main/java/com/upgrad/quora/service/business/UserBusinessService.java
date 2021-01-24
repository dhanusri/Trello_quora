package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class UserBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    /**
     * The method to create new user
     *
     * @param userEntity : object from which new user will be created
     * @return UserEntity object
     * @throws SignUpRestrictedException if validation for user details fails..
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {

        // Validate if requested user name is available
        UserEntity existingUser = userDao.getUserByUserName(userEntity.getUserName());
        if (existingUser != null) {
            throw new SignUpRestrictedException("SGR-001", "Try any other Username, this Username has already been taken");
        }

        // Validate if provided email id is available
        existingUser = userDao.getUserByEmail(userEntity.getEmail());
        if (existingUser != null) {
            throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");
        }

        // Assign default password if password is not provided
        String password = userEntity.getPassword();
        if (password == null) {
            userEntity.setPassword("quora@123");
        }

        // Encrypt salt and password
        String[] encryptedText = cryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptedText[0]);
        userEntity.setPassword(encryptedText[1]);

        return userDao.createUser(userEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthTokenEntity signin(final String username, final String password) throws AuthenticationFailedException {
        UserEntity userEntity = userDao.getUserByUserName(username);

        if (userEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This username does not exist");
        }
        final String encryptedPassword = cryptographyProvider.encrypt(password, userEntity.getSalt());
        if (encryptedPassword.equals(userEntity.getPassword())) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            UserAuthTokenEntity userAuthTokenEntity = new UserAuthTokenEntity();
            userAuthTokenEntity.setUser(userEntity);
            userAuthTokenEntity.setUserUuid(userEntity.getUuid());
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);

            userAuthTokenEntity.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));

            userAuthTokenEntity.setLoginAt(now);
            userAuthTokenEntity.setExpiresAt(expiresAt);

            userDao.createAuthToken(userAuthTokenEntity);

            userDao.updateUser(userEntity);
//            userEntity.setLastLoginAt(now);
            return userAuthTokenEntity;
        } else {
            throw new AuthenticationFailedException("ATH-002", "Password failed");
        }
    }

    // Logs out user if access token is valid, else throw authorization invalid exception.
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signout(final String authorizationToken) throws SignOutRestrictedException {

        UserAuthTokenEntity userAuthEntity = userDao.getUserAuthToken(authorizationToken);

        // Validate if user is signed in or not
        if (userAuthEntity == null) {
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
        }

        final ZonedDateTime now = ZonedDateTime.now();
        userAuthEntity.setLogoutAt(now);

        return userAuthEntity.getUser();
    }

}
