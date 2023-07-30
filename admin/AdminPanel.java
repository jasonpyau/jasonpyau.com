import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class AdminPanel {

    private static Scanner scan = new Scanner(System.in);

    private static void getMessages(int pageSize, int pageNum) {
        apiCall("/contact/get?pageSize="+pageSize+"&pageNum="+pageNum, "{ }", "GET", false);
    }

    private static void deleteMessage(int id) {
        apiCall("/contact/delete/"+id, "{ }", "DELETE", true);
    }

    private static void updateLastUpdated(boolean showConfirmation) {
        apiCall("/stats/update/last_updated", "{ }", "PATCH", showConfirmation);
    }

    private static void newProject() {
        String body = getProjectBody();
        apiCall("/projects/new", body, "POST", true);
        updateLastUpdated(false);
    }

    private static void updateProject() {
        System.out.println("Input the id of the project you'd like to update:");
        int id = scan.nextInt();
        scan.nextLine();
        System.out.println("You may leave blank any fields you don't want to update.");
        String body = getProjectBody();
        apiCall("/projects/update/"+id, body, "PATCH", true);
        updateLastUpdated(false);
    }

    private static void deleteProject() {
        System.out.println("Input the id of the project you'd like to delete:");
        int id = scan.nextInt();
        scan.nextLine();
        apiCall("/projects/delete/"+id, "{ }", "DELETE", true);
        updateLastUpdated(false);
    }

    private static void getProjects() {
        apiCall("/projects/get", "{ }", "GET", false);
    }

    private static void newProjectSkill() {
        System.out.println("Input id of the project you'd like to add a skill to:");
        int id = Integer.parseInt(scan.nextLine());
        System.out.println("Input the name of the skill:");
        String skillName = URLEncoder.encode(scan.nextLine(), StandardCharsets.UTF_8);
        apiCall(String.format("/projects/%d/skills/new?skillName=%s", id, skillName), "{ }", "POST", true);
    }

    private static void deleteProjectSkill() {
        System.out.println("Input id of the project you'd like to remove a skill from:");
        int id = Integer.parseInt(scan.nextLine());
        System.out.println("Input the name of the skill:");
        String skillName = URLEncoder.encode(scan.nextLine(), StandardCharsets.UTF_8);
        apiCall(String.format("/projects/%d/skills/delete?skillName=%s", id, skillName), "{ }", "DELETE", true);
    }

    private static void newSkill() {
        System.out.println("Input name of the skill:");
        String name = scan.nextLine();
        System.out.println("Loading valid types for a skill...");
        apiCall("/skills/valid_types", "{ }", "GET", false);
        System.out.println("These are the valid types. Input the type of skill:");
        String type = scan.nextLine();
        System.out.println("Input the Simple Icons slug for the skill (not required). Read more about it here: https://www.npmjs.com/package/simple-icons https://github.com/simple-icons/simple-icons/blob/develop/slugs.md");
        String simpleIconsIconSlug = scan.nextLine();
        String body = "{\"name\": \""+name+"\"," +
                        "\"type\": \""+type+"\"," +
                        "\"simpleIconsIconSlug\": \""+simpleIconsIconSlug+"\"}";
        apiCall("/skills/new", body, "POST", true);
        updateLastUpdated(false);
        
    }

    private static void deleteSkill() {
        System.out.println("Input name of the skill:");
        String name = URLEncoder.encode(scan.nextLine(), StandardCharsets.UTF_8).replace("+", "%20");
        apiCall(String.format("/skills/delete/%s", name), "{ }", "DELETE", true);
        updateLastUpdated(false);
    }

    private static void viewSkills() {
        apiCall("/skills/get", "{ }", "GET", false);
    }

    private static void updateAboutMe() {
        System.out.println("Input text:");
        String text = scan.nextLine();
        String body = "{\"text\": \""+text+"\"}";
        apiCall("/about_me/update", body, "PUT", true);
        updateLastUpdated(false);
    }

    private static void getAboutMe() {
        apiCall("/about_me/get", "{ }", "GET", false);
    }

    private static void newBlog() {
        System.out.println("Input title of blog:");
        String title = scan.nextLine();
        System.out.println("Input description of blog:");
        String description = scan.nextLine();
        System.out.println("Input body of blog:");
        String body = scan.nextLine();
        String apiBody = "{\"title\": \""+title+"\"," +
                        "\"description\": \""+description+"\"," +
                        "\"body\": \""+body+"\"}";
        apiCall("/blogs/new", apiBody, "POST", true);
    }

    private static void deleteBlog() {
        System.out.println("Input id of the blog you'd like to delete:");
        String id = scan.nextLine();
        apiCall("/blogs/delete/"+id, "{ }", "DELETE", true);
    }

    private static void getBlogs() {
        System.out.println("You may leave blank any field for the default value.");
        StringBuilder sb = new StringBuilder();
        String in;
        System.out.println("Input a page size: ");
        in = scan.nextLine();
        sb.append("pageSize="+Integer.parseInt((in.isBlank()) ? "5" : in));
        System.out.println("Input a page number: ");
        in = scan.nextLine();
        Integer pageNum = Integer.parseInt((in.isBlank()) ? "0" : in);
        System.out.println("Order by: [unix_time, title, like_count, view_count]");
        in = scan.nextLine();
        sb.append("&orderBy="+((in.isBlank()) ? "unix_time" : in));
        System.out.println("Ascending? [true/false]");
        in = scan.nextLine();
        sb.append("&ascending="+((in.isBlank()) ? "false" : in));
        System.out.println("Show liked only? [true/false]");
        in = scan.nextLine();
        sb.append("&liked="+((in.isBlank()) ? "false" : in));
        System.out.println("Search for substring:");
        in = URLEncoder.encode(scan.nextLine(), StandardCharsets.UTF_8);
        sb.append("&search="+in);
        sb.append("&pageNum="+pageNum);
        while (true) {
            apiCall("/blogs/get/page?"+sb.toString(), "{ }", "GET", false);
            System.out.println("0.) Return");
            System.out.println("1.) Next page");
            int input = scan.nextInt();
            scan.nextLine();
            if (input == 1) {
                pageNum++;
                sb.setLength(sb.lastIndexOf("&pageNum="));
                sb.append("&pageNum="+pageNum);
            } else {
                break;
            }
        }
    }

    private static void shutDownServer() {
        apiCall("/shut_down", "{ }", "DELETE", true);
    }

    private static void apiCall(String endpoint, String body, String method, boolean showConfirmation) {
        if (showConfirmation) {
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
                String json = JsonFormat.prettyPrintJSON(response.body());
                System.out.println("Success: \n" + json);
            }
        } catch (Exception e) {
            System.out.println("Error in sending HTTP Request:\n" + e);
        }
    }

    private static void printBlogsMenu() {
        System.out.println("=======================");
        System.out.println("      BLOGS MENU       ");
        System.out.println("=======================");
        System.out.println("1.) New Blog");
        System.out.println("2.) Delete Blog");
        System.out.println("3.) Get Blogs");
        System.out.println("4.) Back");
        int input = scan.nextInt();
        scan.nextLine();
        switch (input) {
            case 1:
                newBlog();
                break;
            case 2:
                deleteBlog();
                break;
            case 3:
                getBlogs();
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid input.");
                printContinue();            
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
        System.out.println("5.) Add Skill to Project");
        System.out.println("6.) Delete Skill from Project");
        System.out.println("7.) Back");
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
                newProjectSkill();
                break;
            case 6:
                deleteProjectSkill();
                break;
            case 7:
                return;
            default:
                System.out.println("Invalid input.");
                printContinue();            
        }
    }

    private static void printSkillsMenu() {
        System.out.println("=======================");
        System.out.println("      SKILLS MENU      ");
        System.out.println("=======================");
        System.out.println("1.) New Skill");
        System.out.println("2.) Delete Skill");
        System.out.println("3.) View Skills");
        System.out.println("4.) Back");
        int input = scan.nextInt();
        scan.nextLine();
        switch (input) {
            case 1:
                newSkill();
                break;
            case 2:
                deleteSkill();
                break;
            case 3:
                viewSkills();
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid input.");
                printContinue();            
        }
    }

    private static void printAboutMeMenu() {
        System.out.println("=======================");
        System.out.println("     ABOUT ME MENU     ");
        System.out.println("=======================");
        System.out.println("1.) Update About Me");
        System.out.println("2.) Get About Me");
        System.out.println("3.) Back");
        int input = scan.nextInt();
        scan.nextLine();
        switch (input) {
            case 1:
                updateAboutMe();
                break;
            case 2:
                getAboutMe();
                break;
            case 3:
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
        System.out.println("1.) Blogs Menu");
        System.out.println("2.) Projects Menu");
        System.out.println("3.) Skills Menu");
        System.out.println("4.) About Me Menu");
        System.out.println("5.) Get Messages");
        System.out.println("6.) Delete Message");
        System.out.println("7.) Update Last Updated");
        System.out.println("8.) Shut down Server");
        System.out.println("9.) Exit");
        int input = scan.nextInt();
        scan.nextLine();
        switch (input) {
            case 1:
                printBlogsMenu();
                printContinue();;
                break;
            case 2:
                printProjectsMenu();
                printContinue();
                break;
            case 3:
                printSkillsMenu();
                printContinue();
                break;
            case 4:
                printAboutMeMenu();
                printContinue();
                break;
            case 5:
                printGetMessagesMenu();
                printContinue();
                break;
            case 6:
                printDeleteMessagesMenu();
                printContinue();
                break;
            case 7:
                updateLastUpdated(true);
                printContinue();
                break;
            case 8:
                shutDownServer();
                printContinue();
                break;
            case 9:
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
        super("\n" + response + "\n" + JsonFormat.prettyPrintJSON(body));
    }
}

// All credit to https://stackoverflow.com/a/49564514, with small modifications and improvements.
class JsonFormat {
    
    private static final String indent = "  ";

    private JsonFormat() {};

    public static String prettyPrintJSON(String unformattedJsonString) {
        StringBuilder sb = new StringBuilder();
        int indentLevel = 0;
        boolean inQuote = false;
        for (char ch : unformattedJsonString.toCharArray()) {
            switch (ch) {
                case '"':
                    // switch the quoting status
                    inQuote = !inQuote;
                    sb.append(ch);
                    break;
                case ' ':
                    // For space: ignore the space if it is not being quoted.
                    if (inQuote) {
                        sb.append(ch);
                    }
                    break;
                case '{':
                case '[':
                    sb.append(ch);
                    // Starting a new block: increase the indent level
                    if (!inQuote) {
                        indentLevel++;
                        sb.append("\n"+indent.repeat(indentLevel));
                    }
                    break;
                case '}':
                case ']':
                    // Ending a new block; decrese the indent level
                    if (!inQuote) {
                        indentLevel--;
                        sb.append("\n"+indent.repeat(indentLevel));
                    }
                    sb.append(ch);
                    break;
                case ',':
                    // Ending a json item; create a new line after
                    sb.append(ch);
                    if (!inQuote) {
                        sb.append("\n"+indent.repeat(indentLevel));
                    }
                    break;
                default:
                    sb.append(ch);
            }
        }
        return sb.toString();
    }
}