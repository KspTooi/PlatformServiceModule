package com.ksptooi.psm.shell;

import com.ksptooi.guice.annotations.Unit;
import com.ksptooi.psm.mapper.UsersMapper;
import com.ksptooi.psm.modes.UserVo;
import jakarta.inject.Inject;
import org.apache.sshd.server.auth.AsyncAuthException;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.session.ServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.downgoon.snowflake.Snowflake;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Unit
public class PSMPasswordAuthenticator implements PasswordAuthenticator {

    private final static Logger log = LoggerFactory.getLogger(PSMPasswordAuthenticator.class);

    @Inject
    private UsersMapper userMapper;

    @Inject
    private Snowflake snowflake;

    @Override
    public boolean authenticate(String username, String password, ServerSession session) throws PasswordChangeRequiredException, AsyncAuthException {
        try {
            // Check if there are any users in the database
            int userCount = userMapper.count(null);

            if (userCount < 1) {
                // Create default user
                String defaultUsername = "default";
                String randomPassword = generatePassword();

                // Insert default user into the database
                UserVo defaultUser = new UserVo();
                defaultUser.setUid(snowflake.nextId());
                defaultUser.setAccount(defaultUsername);
                defaultUser.setPassword(randomPassword);
                defaultUser.setStatus(0); // Active
                defaultUser.setLastLoginTime(new Date());
                defaultUser.setCreateTime(new Date());
                userMapper.insert(defaultUser);

                // Display default user and password in terminal
                log.info("已创建默认用户:{} 密码:{}",defaultUsername,randomPassword);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // Authenticate user
        // Implementation of authentication logic here
        return true;
    }

    @Override
    public boolean handleClientPasswordChangeRequest(ServerSession session, String username, String oldPassword, String newPassword) {
        return PasswordAuthenticator.super.handleClientPasswordChangeRequest(session, username, oldPassword, newPassword);
    }


    private String generatePassword() {
        String uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String symbols = "!@#$%^&*()-_+=<>?";

        String allChars = uppercaseLetters + lowercaseLetters + numbers + symbols;

        StringBuilder password = new StringBuilder();

        Random random = new Random();

        // Ensure password has at least one uppercase letter, lowercase letter, number, and symbol
        password.append(uppercaseLetters.charAt(random.nextInt(uppercaseLetters.length())));
        password.append(lowercaseLetters.charAt(random.nextInt(lowercaseLetters.length())));
        password.append(numbers.charAt(random.nextInt(numbers.length())));
        password.append(symbols.charAt(random.nextInt(symbols.length())));

        // Generate remaining characters for password
        for (int i = 4; i < 12; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        // Shuffle the password to make it more random
        List<Character> charList = password.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        Collections.shuffle(charList);
        StringBuilder shuffledPassword = new StringBuilder();
        for (Character character : charList) {
            shuffledPassword.append(character);
        }

        return shuffledPassword.toString();
    }

}

