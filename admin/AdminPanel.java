import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Scanner;

public class AdminPanel {

    private static Scanner scan = new Scanner(System.in);

    private static void getMessages(int pageSize, int pageNum) {
        try {
            Builder builder = HttpRequest.newBuilder();
            builder.uri(new URI(Constants.SERVER_URL+"/contact/get"));
            builder.header("Content-Type", "application/json");
            String body = "{\"password\": \""+Constants.APP_PASSWORD+"\"," +
                            "\"pageSize\": \""+pageSize+"\"," +
                            "\"pageNum\": \""+pageNum+"\"}";
            builder.method("GET", BodyPublishers.ofString(body));
            HttpRequest request = builder.build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200)
                throw new HttpStatusException(response.toString(), response.body());
            System.out.println("Messages: \n" + response.body());
        } catch (Exception e) {
            System.out.println("Error in sending HTTP Request:\n" + e);
        }
    }

    private static void updateLastUpdated() {
        try {
            Builder builder = HttpRequest.newBuilder();
            builder.uri(new URI(Constants.SERVER_URL+"/stats/update/last_updated"));
            builder.header("Content-Type", "application/json");
            builder.POST(BodyPublishers.ofString("{\"password\": \""+Constants.APP_PASSWORD+"\"}"));
            HttpRequest request = builder.build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200)
                throw new HttpStatusException(response.toString(), response.body());
            System.out.println("Success: \n" + response.body());
        } catch (Exception e) {
            System.out.println("Error in sending HTTP Request:\n" + e);
        }
    }

    private static void printGetMessagesMenu() {
        System.out.println("=======================");
        System.out.println("     MESSAGES MENU     ");
        System.out.println("=======================");
        System.out.print("Input a page size: ");
        int pageSize = scan.nextInt();
        scan.nextLine();
        System.out.println();
        System.out.print("Input a page number: ");
        int pageNum = scan.nextInt();
        scan.nextLine();
        System.out.println();
        while (true) {
            getMessages(pageSize, pageNum);
            System.out.println("0.) Return");
            System.out.println("1.) Next page");
            int input = scan.nextInt();
            scan.nextLine();
            if (input == 1) {
                pageNum++;
            } else {
                break;
            }
        }
    }
    private static void printContinue() {
        System.out.println("Press enter to continue.");
        scan.nextLine();
    }

    private static void printMainMenu() {
        System.out.println("=======================");
        System.out.println("       MAIN MENU       ");
        System.out.println("=======================");
        System.out.println("1.) Update Projects");
        System.out.println("2.) Get Messages");
        System.out.println("3.) Update Recently Updated");
        System.out.println("4.) Exit");
        int input = scan.nextInt();
        scan.nextLine();
        switch (input) {
            case 1:
                break;
            case 2:
                printGetMessagesMenu();
                printContinue();
                break;
            case 3:
                updateLastUpdated();
                printContinue();
                break;
            case 4:
                System.exit(0);
            default:
                System.out.println("Invalid input.");
                printContinue();            
        }
    }
    public static void main(String[] args) {
        while (true) {
            printMainMenu();
        }
    }
}

class HttpStatusException extends Exception {
    public HttpStatusException(String response, String body) {
        super("\n" + response + "\n" + body);
    }
}
