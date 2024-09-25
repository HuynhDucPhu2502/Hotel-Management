package iuh.fit.utils;

import iuh.fit.models.enums.Gender;
import iuh.fit.models.enums.Position;

import java.time.LocalTime;

public class ConvertHelper {
    public static LocalTime localTimeConverter(String input) {
        if (!input.matches("((([0-1][0-9]|[2][0-2]):[0-5][0-9])|(23:00))"))
            throw new IllegalArgumentException(ErrorMessages.CONVERT_HELPER_INVALID_LOCALTIME);

        return LocalTime.parse(input);
    }

    public static Gender genderConverter(String input) {
        if (!input.matches("(FEMALE|MALE)"))
            throw new IllegalArgumentException(ErrorMessages.CONVERT_HELPER_INVALID_GENDER);

        return input.equalsIgnoreCase("FEMALE")
                ? Gender.FEMALE : Gender.MALE;
    }

    public static Position positionConverter(String input) {
        if (!input.matches("(MANAGER|RECEPTIONIST)"))
            throw new IllegalArgumentException(ErrorMessages.CONVERT_HELPER_INVALID_POSITION);

        return input.equalsIgnoreCase("MANAGER")
                ? Position.MANAGER : Position.RECEPTIONIST;
    }

}
