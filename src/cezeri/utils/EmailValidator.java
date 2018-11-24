/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Java email validation program
 *
 * @author pankaj
 *
 */
public class EmailValidator {
    // Email Regex java

    private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

    // static Pattern object, since pattern is fixed
    private static Pattern pattern=Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

    // non-static Matcher object because it's created from the input String
    private static Matcher matcher;

    public static boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
