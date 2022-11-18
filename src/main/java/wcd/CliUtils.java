package wcd;

public class CliUtils {

    public static void write(String line) {
        System.out.println(line);
    }

    public static String read(String description) {
        CliUtils.write(description);
        return System.console().readLine();
    }

    public static String readPassword(String description) {
        CliUtils.write(description);
        return String.valueOf(System.console().readPassword());
    }

    public static String read(String description, String defaultValue) {
        var input = CliUtils.read(description);
        return (input == null || input.isBlank()) ? defaultValue : input;
    }

}
