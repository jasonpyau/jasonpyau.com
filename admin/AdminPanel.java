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
        apiCall("/contact/get?pageSize="+pageSize+"&pageNum="+pageNum, "{ }", "GET");
    }

    private static void deleteMessage(int id) {
        apiCall("/contact/delete/"+id, "{ }", "DELETE");
    }

    private static void updateLastUpdated() {
        apiCall("/stats/update/last_updated", "{ }", "POST");
    }

    private static void newProject() {
        String body = getProjectBody();
        apiCall("/projects/new", body, "PUT");
    }

    private static void updateProject() {
        System.out.println("Input the id of the project you'd like to update:");
        int id = scan.nextInt();
        scan.nextLine();
        System.out.println("You may leave blank any fields you don't want to update.");
        String body = getProjectBody();
        apiCall("/projects/update/"+id, body, "PUT");
    }

    private static void deleteProject() {
        System.out.println("Input the id of the project you'd like to delete:");
        int id = scan.nextInt();
        scan.nextLine();
        apiCall("/projects/delete/"+id, "{ }", "DELETE");
    }

    private static void getProjects() {
        apiCall("/projects/get", "{ }", "GET");
    }

    private static void apiCall(String endpoint, String body, String method) {
        System.out.println("This will send a "+method+" request with the body: \n");
        System.out.println(body);
        System.out.println("\nto "+endpoint+".");
        System.out.println("Confirm ('Y'/'N'):");
        String input = scan.nextLine();
        switch (input) {
            case "Y":
            case "y":
                break;
            case "N":
            case "n":
            default:
                System.out.println("Exited.");
                return;
        }
        try {
            Builder builder = HttpRequest.newBuilder();
            builder.uri(new URI(Constants.SERVER_URL+endpoint));
            builder.header("Content-Type", "application/json");
            builder.header("Authorization", Constants.APP_PASSWORD);
            builder.method(method, BodyPublishers.ofString(body));
            HttpRequest request = builder.build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new HttpStatusException(response.toString(), response.body());
            } else {
                System.out.println("Success: \n" + response.body());
            }
        } catch (Exception e) {
            System.out.println("Error in sending HTTP Request:\n" + e);
        }
    }

    private static void printProjectsMenu() {
        System.out.println("=======================");
        System.out.println("     PROJECTS MENU     ");
        System.out.println("=======================");
        System.out.println("1.) New Project");
        System.out.println("2.) Update Project");
        System.out.println("3.) Delete Project");
        System.out.println("4.) View Projects");
        System.out.println("5.) Back");
        int input = scan.nextInt();
        scan.nextLine();
        switch (input) {
            case 1:
                newProject();
                break;
            case 2:
                updateProject();
                break;
            case 3:
                deleteProject();
                break;
            case 4:
                getProjects();
                break;
            case 5:
                return;
            default:
                System.out.println("Invalid input.");
                printContinue();            
        }
    }

    private static String getProjectBody() {
        StringBuilder sb = new StringBuilder();
        String input;
        System.out.println("Input name of project:");
        input = scan.nextLine();
        sb.append("{\"name\": " + ((!input.isBlank()) ? "\""+input+"\"" : "null") + ", ");
        System.out.println("Input description of project:");
        input = scan.nextLine();
        sb.append("\"description\": " + ((!input.isBlank()) ? "\""+input+"\"" : "null") + ", ");
        System.out.println("Input startDate ('MM/YYYY') of project");
        input = scan.nextLine();
        sb.append("\"startDate\": " + ((!input.isBlank()) ? "\""+input+"\"" : "null") + ", ");
        System.out.println("Input endDate ('MM/YYYY') of project");
        input = scan.nextLine();
        sb.append("\"endDate\": " + ((!input.isBlank()) ? "\""+input+"\"" : "null") + ", ");
        System.out.println("Input technologies, seperated by the enter key, ending with a blank line.");
        sb.append("\"technologies\": ");
        StringBuilder techSb = new StringBuilder();
        techSb.append("[");
        boolean hasTech = false;
        while (true) {
            input = scan.nextLine();
            if (input.isBlank())
                break;
            techSb.append("\""+input+"\",");
            hasTech = true;
            
        }
        if (hasTech) {
            techSb.setLength(techSb.lastIndexOf(","));
            techSb.append("], ");
            sb.append(techSb.toString());
        } else {
            sb.append("null, ");
        }
        System.out.println("Input link to the project:");
        input = scan.nextLine();
        sb.append("\"link\": " + ((!input.isBlank()) ? "\""+input+"\"" : "null") + "} ");
        sb.append("}");
        return sb.toString();
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

    private static void printDeleteMessagesMenu() {
        System.out.println("=======================");
        System.out.println("     MESSAGES MENU     ");
        System.out.println("=======================");
        System.out.print("Input id of the message you would like to delete: ");
        int id = scan.nextInt();
        scan.nextLine();
        System.out.println();
        deleteMessage(id);
    }

    private static void printContinue() {
        System.out.println("Press enter to continue.");
        scan.nextLine();
    }

    private static void printMainMenu() {
        System.out.println("=======================");
        System.out.println("       MAIN MENU       ");
        System.out.println("=======================");
        System.out.println("1.) Projects Menu");
        System.out.println("2.) Get Messages");
        System.out.println("3.) Delete Message");
        System.out.println("4.) Update Recently Updated");
        System.out.println("5.) Exit");
        int input = scan.nextInt();
        scan.nextLine();
        switch (input) {
            case 1:
                printProjectsMenu();
                printContinue();
                break;
            case 2:
                printGetMessagesMenu();
                printContinue();
                break;
            case 3:
                printDeleteMessagesMenu();
                printContinue();
                break;
            case 4:
                updateLastUpdated();
                printContinue();
                break;
            case 5:
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