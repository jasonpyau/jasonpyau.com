import java.io.FileInputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class AdminPanel {

    private static final Properties properties = new Properties();
    private static final Scanner scan = new Scanner(System.in);

    private static void getMetadata() {
        apiCall("/metadata/get", "{ }", "GET", false);
    }

    private static void updateLastUpdated(boolean showConfirmation) {
        if (!showConfirmation) {
            System.out.println("Updating lastUpdated in the metadata...");
        }
        apiCall("/metadata/update/last_updated", "{ }", "PATCH", showConfirmation);
    }

    private static void updateMetadata() {
        StringBuilder sb = new StringBuilder();
        String input;
        System.out.println("You may leave blank any fields you don't want to update.");
        System.out.println("Input your name:");
        input = scan.nextLine();
        sb.append("{\"name\": " + ((!input.isBlank()) ? "\""+input+"\"" : "null") + ", ");
        System.out.println("Input a link to an square image that will be used as the website icon:");
        input = scan.nextLine();
        sb.append("\"iconLink\": " + ((!input.isBlank()) ? "\""+input+"\"" : "null") + ", ");
        System.out.println("Input a description for the website that will be used for SEO (shown in Google/Bing search results):");
        input = scan.nextLine();
        sb.append("\"description\": " + ((!input.isBlank()) ? "\""+input+"\"" : "null") + ", ");
        System.out.println("Input keywords for the website that will be used for SEO (a comma-separated list of phrases):");
        input = scan.nextLine();
        sb.append("\"keywords\": " + ((!input.isBlank()) ? "\""+input+"\"" : "null") + "} ");
        boolean success = apiCall("/metadata/update", sb.toString(), "PATCH", true);
        if (success) {
            updateLastUpdated(false);
        }
    }

    private static void newExperience() {
        String body = getExperienceBody();
        boolean success = apiCall("/experiences/new", body, "POST", true);
        if (success) {
            updateLastUpdated(false);
        }
    }

    private static void updateExperience() {
        System.out.println("Input the id of the experience you'd like to update:");
        String id = scan.nextLine();
        System.out.println("You may leave blank any fields you don't want to update.");
        System.out.println("You may input \"ERASE!!!\" to erase the current value for any optional fields.");
        String body = getExperienceBody();
        boolean success = apiCall("/experiences/update/"+id, body, "PATCH", true);
        if (success) {
            updateLastUpdated(false);
        }
    }

    private static void deleteExperience() {
        System.out.println("Input the id of the experience you'd like to delete:");
        String id = scan.nextLine();
        boolean success = apiCall("/experiences/delete/"+id, "{ }", "DELETE", true);
        if (success) {
            updateLastUpdated(false);
        }
    }

    private static void getExperiences() {
        apiCall("/experiences/get", "{ }", "GET", false);
    }

    private static void newExperienceSkill() {
        System.out.println("Input id of the experience you'd like to add a skill to:");
        String id = scan.nextLine();
        System.out.println("Input the name of the skill:");
        String skillName = URLEncoder.encode(scan.nextLine(), StandardCharsets.UTF_8);
        apiCall(String.format("/experiences/%s/skills/new?skillName=%s", id, skillName), "{ }", "POST", true);
    }

    private static void deleteExperienceSkill() {
        System.out.println("Input id of the experience you'd like to remove a skill from:");
        String id = scan.nextLine();
        System.out.println("Input the name of the skill:");
        String skillName = URLEncoder.encode(scan.nextLine(), StandardCharsets.UTF_8);
        apiCall(String.format("/experiences/%s/skills/delete?skillName=%s", id, skillName), "{ }", "DELETE", true);
    }

    private static void newProject() {
        String body = getProjectBody();
        boolean success = apiCall("/projects/new", body, "POST", true);
        if (success) {
            updateLastUpdated(false);
        }
    }

    private static void updateProject() {
        System.out.println("Input the id of the project you'd like to update:");
        String id = scan.nextLine();
        System.out.println("You may leave blank any fields you don't want to update.");
        String body = getProjectBody();
        boolean success = apiCall("/projects/update/"+id, body, "PATCH", true);
        if (success) {
            updateLastUpdated(false);
        }
    }

    private static void deleteProject() {
        System.out.println("Input the id of the project you'd like to delete:");
        String id = scan.nextLine();
        boolean success = apiCall("/projects/delete/"+id, "{ }", "DELETE", true);
        if (success) {
            updateLastUpdated(false);
        }
    }

    private static void getProjects() {
        apiCall("/projects/get", "{ }", "GET", false);
    }

    private static void newProjectSkill() {
        System.out.println("Input id of the project you'd like to add a skill to:");
        String id = scan.nextLine();
        System.out.println("Input the name of the skill:");
        String skillName = URLEncoder.encode(scan.nextLine(), StandardCharsets.UTF_8);
        apiCall(String.format("/projects/%s/skills/new?skillName=%s", id, skillName), "{ }", "POST", true);
    }

    private static void deleteProjectSkill() {
        System.out.println("Input id of the project you'd like to remove a skill from:");
        String id = scan.nextLine();
        System.out.println("Input the name of the skill:");
        String skillName = URLEncoder.encode(scan.nextLine(), StandardCharsets.UTF_8);
        apiCall(String.format("/projects/%s/skills/delete?skillName=%s", id, skillName), "{ }", "DELETE", true);
    }

    private static void newSkill() {
        System.out.println("Input name of the skill:");
        String name = scan.nextLine();
        System.out.println("Loading valid types for a skill...");
        apiCall("/skills/valid_types", "{ }", "GET", false);
        System.out.println("These are the valid types. Input the type of skill:");
        String type = scan.nextLine();
        System.out.println("Input a link that provides more info on this skill (optional):");
        String link = scan.nextLine();
        System.out.println("Input the Simple Icons slug for the skill (optional). See here:\n" +
                            "https://github.com/simple-icons/simple-icons/blob/master/slugs.md\n\n" +
                            "Additionally, icons for Microsoft technologies and Java were removed in Simple Icons version >= 7.0.0. You may also use:\n" +
                            "https://github.com/simple-icons/simple-icons/blob/6.23.0/slugs.md\n");
        String simpleIconsIconSlug = scan.nextLine();
        String body = "{\"name\": \""+name+"\"," +
                        "\"type\": \""+type+"\"," +
                        "\"link\": \""+link+"\"," +
                        "\"simpleIconsIconSlug\": \""+simpleIconsIconSlug+"\"}";
        boolean success = apiCall("/skills/new", body, "POST", true);
        if (success) {
            updateLastUpdated(false);
        }
    }

    private static void updateSkill() {
        StringBuilder sb = new StringBuilder();
        String input;
        System.out.println("Input name of the skill you'd like to update:");
        input = scan.nextLine();
        sb.append("{\"name\": "+"\""+input+"\", ");
        System.out.println("You may leave blank any fields you don't want to update.");
        System.out.println("You may input \"ERASE!!!\" to erase the current value for any optional fields.");
        System.out.println("Loading valid types for a skill...");
        apiCall("/skills/valid_types", "{ }", "GET", false);
        System.out.println("These are the valid types. Input the type of skill:");
        input = scan.nextLine();
        sb.append("\"type\": " + ((!input.isBlank()) ? "\""+input+"\"" : "null") + ", ");
        System.out.println("Input a link that provides more info on this skill (optional):");
        input = scan.nextLine();
        sb.append("\"link\": " + ((!input.isBlank()) ? input.equals("ERASE!!!") ? "\"\"" : "\""+input+"\"" : "null") + ", ");
        System.out.println("Input the Simple Icons slug for the skill (optional). See here:\n" +
                            "https://github.com/simple-icons/simple-icons/blob/master/slugs.md\n\n" +
                            "Additionally, icons for Microsoft technologies and Java were removed in Simple Icons version >= 7.0.0. You may also use:\n" +
                            "https://github.com/simple-icons/simple-icons/blob/6.23.0/slugs.md\n");
        input = scan.nextLine();
        sb.append("\"simpleIconsIconSlug\": " + ((!input.isBlank()) ? input.equals("ERASE!!!") ? "\"\"" : "\""+input+"\"" : "null") + "} ");
        boolean success = apiCall("/skills/update", sb.toString(), "PATCH", true);
        if (success) {
            updateLastUpdated(false);
        }
    }

    private static void deleteSkill() {
        System.out.println("Input name of the skill:");
        String name = URLEncoder.encode(scan.nextLine(), StandardCharsets.UTF_8).replace("+", "%20");
        boolean success = apiCall(String.format("/skills/delete/%s", name), "{ }", "DELETE", true);
        if (success) {
            updateLastUpdated(false);
        }
    }

    private static void viewSkills() {
        apiCall("/skills/get", "{ }", "GET", false);
    }

    private static void updateAboutMe() {
        System.out.println("Input text:");
        String text = scan.nextLine();
        String body = "{\"text\": \""+text+"\"}";
        boolean success = apiCall("/about_me/update", body, "PUT", true);
        if (success) {
            updateLastUpdated(false);
        }
    }

    private static void getAboutMe() {
        apiCall("/about_me/get", "{ }", "GET", false);
    }

    private static void newLink() {
        System.out.println("Input the display name of the link:");
        String name = scan.nextLine();
        System.out.println("Input the href of the link:");
        String href = scan.nextLine();
        System.out.println("Input the Simple Icons slug for the link (optional). See here:\n" +
                            "https://github.com/simple-icons/simple-icons/blob/master/slugs.md\n\n" +
                            "Additionally, icons for Microsoft technologies (e.g. LinkedIn) were removed in Simple Icons version >= 7.0.0. You may also use:\n" +
                            "https://github.com/simple-icons/simple-icons/blob/6.23.0/slugs.md\n");
        String simpleIconsIconSlug = scan.nextLine();
        System.out.println("Input the hex fill for the simpleIconsIconSlug given (optional).\n" +
                            "If this value is not given and simpleIconsIconSlug was given, the hex value used will be from Simple Icons.");
        String hexFill = scan.nextLine();
        String body = "{\"name\": \""+name+"\"," +
                        "\"href\": \""+href+"\"," +
                        "\"simpleIconsIconSlug\": \""+simpleIconsIconSlug+"\"," +
                        "\"hexFill\": \""+hexFill+"\"}";
        boolean success = apiCall("/links/new", body, "POST", true);
        if (success) {
            updateLastUpdated(false);
        }
    }

    private static void updateLink() {
        StringBuilder sb = new StringBuilder();
        System.out.println("Input the id of the link you'd like to update:");
        String id = scan.nextLine(), input;
        System.out.println("You may leave blank any fields you don't want to update.");
        System.out.println("You may input \"ERASE!!!\" to erase the current value for any optional fields.");
        System.out.println("Input the display name of the link:");
        input = scan.nextLine();
        sb.append("{\"name\": " + ((!input.isBlank()) ? "\""+input+"\"" : "null") + ", ");
        System.out.println("Input the href of the link:");
        input = scan.nextLine();
        sb.append("\"href\": " + ((!input.isBlank()) ? "\""+input+"\"" : "null") + ", ");
        System.out.println("Input the Simple Icons slug for the link (optional). See here:\n" +
                            "https://github.com/simple-icons/simple-icons/blob/master/slugs.md\n\n" +
                            "Additionally, icons for Microsoft technologies (e.g. LinkedIn) were removed in Simple Icons version >= 7.0.0. You may also use:\n" +
                            "https://github.com/simple-icons/simple-icons/blob/6.23.0/slugs.md\n");
        input = scan.nextLine();
        sb.append("\"simpleIconsIconSlug\": " + ((!input.isBlank()) ? input.equals("ERASE!!!") ? "\"\"" : "\""+input+"\"" : "null") + ", ");
        System.out.println("Input the hex fill for the simpleIconsIconSlug given (optional).\n" +
                            "If this value is not given and simpleIconsIconSlug was given, the hex value used will be from Simple Icons.");
        input = scan.nextLine();
        sb.append("\"hexFill\": " + ((!input.isBlank()) ? input.equals("ERASE!!!") ? "\"\"" : "\""+input+"\"" : "null") + "} ");
        boolean success = apiCall("/links/update/"+id, sb.toString(), "PATCH", true);
        if (success) {
            updateLastUpdated(false);
        }
    }

    private static void moveLinkToTop() {
        System.out.println("Input the id of the link you'd like to move to the top:");
        String id = scan.nextLine();
        boolean success = apiCall("/links/move_to_top/"+id, "{ }", "PATCH", true);
        if (success) {
            updateLastUpdated(false);
        }
    }

    private static void getLinks() {
        apiCall("/links/get", "{ }", "GET", false);
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
        sb.append("pageSize="+((in.isBlank()) ? "5" : in));
        System.out.println("Input a page number: ");
        in = scan.nextLine();
        int pageNum = (in.isBlank() || !in.matches("\\d+")) ? 0 : Integer.parseInt(in);
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
            String input = scan.nextLine();
            if (input.equals("1")) {
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

    private static boolean apiCall(String endpoint, String body, String method, boolean showConfirmation) {
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
                    return false;
            }
        }
        try {
            Builder builder = HttpRequest.newBuilder();
            builder.uri(new URI(properties.getProperty("SERVER_URL")+endpoint));
            builder.header("Content-Type", "application/json");
            builder.header("Authorization", properties.getProperty("ADMIN_PANEL_PASSWORD"));
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
            return false;
        }
        return true;
    }

    private static void printBlogsMenu() {
        while (true) {
            System.out.println("=======================");
            System.out.println("      BLOGS MENU       ");
            System.out.println("=======================");
            System.out.println("1.) New Blog");
            System.out.println("2.) Delete Blog");
            System.out.println("3.) Get Blogs");
            System.out.println("4.) Back");
            String input = scan.nextLine();
            switch (input) {
                case "1":
                    newBlog();
                    break;
                case "2":
                    deleteBlog();
                    break;
                case "3":
                    getBlogs();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid input.");           
            }
            printContinue(); 
        }
    }

    private static void printProjectsMenu() {
        while (true) {
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
            String input = scan.nextLine();
            switch (input) {
                case "1":
                    newProject();
                    break;
                case "2":
                    updateProject();
                    break;
                case "3":
                    deleteProject();
                    break;
                case "4":
                    getProjects();
                    break;
                case "5":
                    newProjectSkill();
                    break;
                case "6":
                    deleteProjectSkill();
                    break;
                case "7":
                    return;
                default:
                    System.out.println("Invalid input.");
            }
            printContinue();
        }
    }

    private static void printExperiencesMenu() {
        while (true) {
            System.out.println("=======================");
            System.out.println("     EXPERIENCES MENU     ");
            System.out.println("=======================");
            System.out.println("1.) New Experience");
            System.out.println("2.) Update Experience");
            System.out.println("3.) Delete Experience");
            System.out.println("4.) View Experiences");
            System.out.println("5.) Add Skill to Experience");
            System.out.println("6.) Delete Skill from Experience");
            System.out.println("7.) Back");
            String input = scan.nextLine();
            switch (input) {
                case "1":
                    newExperience();
                    break;
                case "2":
                    updateExperience();
                    break;
                case "3":
                    deleteExperience();
                    break;
                case "4":
                    getExperiences();
                    break;
                case "5":
                    newExperienceSkill();
                    break;
                case "6":
                    deleteExperienceSkill();
                    break;
                case "7":
                    return;
                default:
                    System.out.println("Invald input.");
            }
            printContinue();
        }
    }

    private static void printSkillsMenu() {
        while (true) {
            System.out.println("=======================");
            System.out.println("      SKILLS MENU      ");
            System.out.println("=======================");
            System.out.println("1.) New Skill");
            System.out.println("2.) Update Skill");
            System.out.println("3.) Delete Skill");
            System.out.println("4.) View Skills");
            System.out.println("5.) Back");
            String input = scan.nextLine();
            switch (input) {
                case "1":
                    newSkill();
                    break;
                case "2":
                    updateSkill();
                    break;
                case "3":
                    deleteSkill();
                    break;
                case "4":
                    viewSkills();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid input.");
            }
            printContinue();
        }
    }

    private static void printAboutMeMenu() {
        while (true) {
            System.out.println("=======================");
            System.out.println("     ABOUT ME MENU     ");
            System.out.println("=======================");
            System.out.println("1.) Update About Me");
            System.out.println("2.) Get About Me");
            System.out.println("3.) Back");
            String input = scan.nextLine();
            switch (input) {
                case "1":
                    updateAboutMe();
                    break;
                case "2":
                    getAboutMe();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid input.");
            }
            printContinue();
        }
    }

    private static void printMetadataMenu() {
        while (true) {
            System.out.println("=======================");
            System.out.println("     METADATA MENU     ");
            System.out.println("=======================");
            System.out.println("1.) Get Metadata");
            System.out.println("2.) Update Metadata");
            System.out.println("3.) Update Last Updated");
            System.out.println("4.) Back");
            String input = scan.nextLine();
            switch (input) {
                case "1":
                    getMetadata();
                    break;
                case "2":
                    updateMetadata();
                    break;
                case "3":
                    updateLastUpdated(true);
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid input.");
            }
            printContinue();
        }
    }

    private static String getExperienceBody() {
        StringBuilder sb = new StringBuilder();
        String input;
        System.out.println("Input experience position:");
        input = scan.nextLine();
        sb.append("{\"position\": " + ((!input.isBlank()) ? "\""+input+"\"" : "null") + ", ");
        System.out.println("Input experience company:");
        input = scan.nextLine();
        sb.append("\"company\": " + ((!input.isBlank()) ? "\""+input+"\"" : "null") + ", ");
        System.out.println("Input experience location:");
        input = scan.nextLine();
        sb.append("\"location\": " + ((!input.isBlank()) ? "\""+input+"\"" : "null") + ", ");
        System.out.println("Input startDate ('MM/YYYY') of experience");
        input = scan.nextLine();
        sb.append("\"startDate\": " + ((!input.isBlank()) ? "\""+input+"\"" : "null") + ", ");
        System.out.println("Is the experience currently being worked on? [true/false]");
        input = scan.nextLine();
        sb.append("\"present\": " + ((!input.isBlank()) ? "\""+input+"\"" : "null") + ", ");
        if (input.equals("true")) {
            // Currently, endDate is a required field even if present = true. 
            // The server has logic to always update the endDate to the current month. 
            input = "12/2099";
        } else {
            System.out.println("Input endDate ('MM/YYYY') of experience");
            input = scan.nextLine();
        }
        sb.append("\"endDate\": " + ((!input.isBlank()) ? "\""+input+"\"" : "null") + ", ");
        System.out.println("Input experience body (description):");
        input = scan.nextLine();
        sb.append("\"body\": " + ((!input.isBlank()) ? "\""+input+"\"" : "null") + ", ");
        System.out.println("Input experience logo link:");
        input = scan.nextLine();
        sb.append("\"logoLink\": " + ((!input.isBlank()) ? "\""+input+"\"" : "null") + ", ");
        System.out.println("Input experience company link (optional):");
        input = scan.nextLine();
        sb.append("\"companyLink\": " + ((!input.isBlank()) ? input.equals("ERASE!!!") ? "\"\"" : "\""+input+"\"" : "null") + "} ");
        return sb.toString();
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
        System.out.println("Is the project currently being worked on? [true/false]");
        input = scan.nextLine();
        sb.append("\"present\": " + ((!input.isBlank()) ? "\""+input+"\"" : "null") + ", ");
        if (input.equals("true")) {
            // Currently, endDate is a required field even if present = true. 
            // The server has logic to always update the endDate to the current month. 
            input = "12/2099";
        } else {
            System.out.println("Input endDate ('MM/YYYY') of project");
            input = scan.nextLine();
        }
        sb.append("\"endDate\": " + ((!input.isBlank()) ? "\""+input+"\"" : "null") + ", ");
        System.out.println("Input link to the project:");
        input = scan.nextLine();
        sb.append("\"link\": " + ((!input.isBlank()) ? "\""+input+"\"" : "null") + "} ");
        return sb.toString();
    }

    private static void printLinksMenu() {
        while (true) {
            System.out.println("=======================");
            System.out.println("       LINKS MENU      ");
            System.out.println("=======================");
            System.out.println("1.) New Link");
            System.out.println("2.) Update Link");
            System.out.println("3.) Move Link to Top");
            System.out.println("5.) Get all Links in Order");
            System.out.println("6.) Back");
            String input = scan.nextLine();
            switch (input) {
                case "1":
                    newLink();
                    break;
                case "2":
                    updateLink();
                    break;
                case "3":
                    moveLinkToTop();
                    break;
                case "5":
                    getLinks();
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid input.");
            }
            printContinue();
        }
    }

    private static void printMessagesMenu() {
        while (true) {
            System.out.println("=======================");
            System.out.println("     MESSAGES MENU     ");
            System.out.println("=======================");
            System.out.println("1.)  Get Messages");
            System.out.println("2.)  Delete Messages");
            System.out.println("3.)  Back");
            String input = scan.nextLine();
            switch (input) {
                case "1":
                    getMessages();
                    break;
                case "2":
                    deleteMessages();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid input.");
            }
            printContinue();
        }
    }

    private static void getMessages() {
        System.out.println("You may leave blank any field for the default value.");
        System.out.print("Input a page size: ");
        String pageSize = scan.nextLine();
        pageSize = (pageSize.isBlank()) ? "5" : pageSize; 
        System.out.println();
        System.out.print("Input a page number: ");
        String pageNumInput = scan.nextLine();
        int pageNum = (pageNumInput.isBlank() || !pageNumInput.matches("\\d+")) ? 0 : Integer.parseInt(pageNumInput);
        System.out.println();
        while (true) {
            apiCall("/contact/get?pageSize="+pageSize+"&pageNum="+pageNum, "{ }", "GET", false);
            System.out.println("0.) Return");
            System.out.println("1.) Next page");
            String input = scan.nextLine();
            if (input.equals("1")) {
                pageNum++;
            } else {
                break;
            }
        }
    }

    private static void deleteMessages() {
        System.out.print("Input id of the message you would like to delete: ");
        String id = scan.nextLine();
        apiCall("/contact/delete/"+id, "{ }", "DELETE", true);
    }

    private static void printContinue() {
        System.out.println("Press enter to continue.");
        scan.nextLine();
    }

    private static void printMainMenu() {
        System.out.println("=======================");
        System.out.println("       MAIN MENU       ");
        System.out.println("=======================");
        System.out.println("1.)  Blogs Menu");
        System.out.println("2.)  Experiences Menu");
        System.out.println("3.)  Projects Menu");
        System.out.println("4.)  Skills Menu");
        System.out.println("5.)  About Me Menu");
        System.out.println("6.)  Messages Menu");
        System.out.println("7.)  Links Menu");
        System.out.println("8.)  Metadata Menu");
        System.out.println("9.)  Shut down Server");
        System.out.println("10.) Exit");
        String input = scan.nextLine();
        switch (input) {
            case "1":
                printBlogsMenu();
                break;
            case "2":
                printExperiencesMenu();
                break;
            case "3":
                printProjectsMenu();
                break;
            case "4":
                printSkillsMenu();
                break;
            case "5":
                printAboutMeMenu();
                break;
            case "6":
                printMessagesMenu();
                printContinue();
                break;
            case "7":
                printLinksMenu();
                printContinue();
                break;
            case "8":
                printMetadataMenu();
                printContinue();
                break;
            case "9":
                shutDownServer();
                printContinue();
                break;
            case "10":
                System.exit(0);
            default:
                System.out.println("Invalid input.");
                printContinue();            
        }
    }
    public static void main(String[] args) {
        try {
            properties.load(new FileInputStream("AdminPanel.properties"));
            if (properties.getProperty("SERVER_URL") == null) {
                System.out.println("ERROR: 'SERVER_URL' is null. Add the property 'SERVER_URL' to AdminPanel.properties.");
                printContinue();
                System.exit(1);
            } else {
                Matcher matcher = Pattern.compile("^(http|https):\\/\\/(.*)$").matcher(properties.getProperty("SERVER_URL"));
                if (!matcher.find()) {
                    System.out.println("ERROR: 'SERVER_URL' does not start with start with 'http://' or 'https://'.");
                    printContinue();
                    System.exit(1);
                }
            }
            if (properties.getProperty("ADMIN_PANEL_PASSWORD") == null) {
                System.out.println("ERROR: 'ADMIN_PANEL_PASSWORD' is null. Add the property 'ADMIN_PANEL_PASSWORD' to AdminPanel.properties.");
                printContinue();
                System.exit(1);
            }
        } catch (Exception e) {
            System.out.println("ERROR: "+e.getMessage());
            printContinue();
            System.exit(1);
        }
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