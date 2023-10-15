import java.util.concurrent.StructuredTaskScope;

public class Www {
    public static void main(String[] args) {
        try (StructuredTaskScope.ShutdownOnSuccess<String> task = new StructuredTaskScope.ShutdownOnSuccess<>()) {
            task.fork(() -> {
                Thread.sleep(100);
                return "#1";
            });

            task.fork(() -> {
                Thread.sleep(200);
                return "#2";
            });

            task.fork(() -> {
                Thread.sleep(100);
                return "#3";
            });

            task.join();

            System.out.println(task.result());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
