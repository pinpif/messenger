package myprojectmessenger;

import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;

public class AuthorizationUtil {

    public static String[] parseUsernamePassword(String authorization) {
        byte[] usernamePasswordByteArray = Base64Utils.decodeFromString(authorization.substring("Basic ".length()));
        String usernamePassword = new String(usernamePasswordByteArray, StandardCharsets.UTF_8);
        return usernamePassword.split(":");
    }
}
