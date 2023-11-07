package comp5703.sydney.edu.au.learn.util;

import android.text.TextUtils;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FormValidator {

    public static boolean validateTextInputLayout(TextInputLayout inputLayout, String value, String errorMessage) {
        if (value.isEmpty()) {
            inputLayout.setError(errorMessage);
            return false;
        } else {
            inputLayout.setError(null);
            return true;
        }
    }

    public static boolean validateTextInputLayoutAsFloat(TextInputLayout inputLayout, String value, String errorMessageEmpty, String errorMessageInvalid) {
        if (value.isEmpty()) {
            inputLayout.setError(errorMessageEmpty);
            return false;
        } else {
            try {
                float parsedValue = Float.parseFloat(value);
                if (parsedValue <= 0) {
                    // 如果解析的值不是正数，则设置错误消息
                    inputLayout.setError(errorMessageInvalid);
                    return false;
                } else {
                    // 输入是非零的正浮点数
                    inputLayout.setError(null);
                    return true;
                }
            } catch (NumberFormatException e) {
                inputLayout.setError(errorMessageInvalid);
                return false;
            }
        }
    }


    public static boolean validateEmail(TextInputLayout inputLayout, String email) {
        if (!isValidEmail(email)) {
            inputLayout.setError("Invalid email address");
            return false;
        } else {
            inputLayout.setError(null);
            return true;
        }
    }



    public static boolean validatePassword(TextInputLayout inputLayout, String password) {

        if (!hasDigit(password)) {
            inputLayout.setError("Password must contain at least one digit.");

        } else if (!hasLetter(password)) {
            inputLayout.setError("Password must contain at least one letter.");

        } else if (!isLengthValid(password)) {
            inputLayout.setError("Password must be at least 6 characters long.");

        } else {
            inputLayout.setError(null);  // Clear the error
            return true;
        }

        return false;
    }


    public static boolean validateRepeatPassword(TextInputLayout inputLayout, String password, String password2) {
        if (!password.equals(password2)) {
            inputLayout.setError("two password is not same");
            return false;
        } else {
            inputLayout.setError(null);
            return true;
        }
    }

    private static boolean isValidEmail(String email) {
        // 邮箱校验逻辑
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private static boolean hasDigit(String value) {
        return !TextUtils.isEmpty(value) && value.matches(".*\\d.*");
    }

    private static boolean hasLetter(String value) {
        return !TextUtils.isEmpty(value) && value.matches(".*[a-zA-Z].*");
    }

    private static boolean isLengthValid(String value) {
        return value.length() >= 6;
    }





}

