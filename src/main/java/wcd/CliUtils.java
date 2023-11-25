package wcd;

public class CliUtils {

    public static void write(String line) {
        System.out.println(line);
    }

    public static void printHelp() {
        CliUtils.write("usage: java -jar file/to/this.jar username apiKey sync-path [optional: collections]");
        CliUtils.write("e.G. java -jar file/to/this.jar foobar Password123 /home/foobar/ -> sync all collections into folder 'foobar'");
    }
}
