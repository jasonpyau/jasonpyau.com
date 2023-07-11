import { apiCall } from "./apiCall.js";
import { loadSkills } from "./skills.js";

$(document).ready(function() {
    $.get("/files/projects.html", async function(projectTemplate) {
        $("#ProjectRows").append(projectTemplate);
        const url = `/projects/get`;
        const result = await apiCall(url, "GET", null, null);
        const json = await result.json();
        if (result.status !== 200) {
            alert(`Error in loading projects, refresh. If this problem persists, contact me.\n\nReason: ${json.reason}`);
            return;
        }
        const projects = json.projects;
        for (const project of projects) {
            new Project(project);
        }
        document.getElementById("projectSpinner").style.display = "none";
    });
});

class Project {
    constructor(project) {
        $("#ProjectTemplate").clone()
                                .prop('id', "Project"+project.id)
                                .appendTo("#ProjectRows");
        const element = document.querySelector(`#ProjectRows #Project${project.id}`);
        element.querySelector("#Name").textContent = project.name;
        element.querySelector("#Description").textContent = project.description;
        element.querySelector("#StartDate").textContent = project.startDate;
        element.querySelector("#EndDate").textContent = project.endDate;
        const skillsContainer = element.querySelector("#SkillsContainer");
        loadSkills(project.skills, skillsContainer);
        element.href = project.link;
        element.style.display = "block";
    }
}