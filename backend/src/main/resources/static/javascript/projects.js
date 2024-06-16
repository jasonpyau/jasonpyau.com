import { apiCall } from "./apiCall.js";
import { loadSkills } from "./skills.js";

addEventListener('DOMContentLoaded', async(e) => {
    const url = `/projects/get`;
    const result = await apiCall(url, "GET", null, null);
    const json = await result.json();
    if (result.status !== 200) {
        alert(`Error in loading projects, refresh. If this problem persists, contact me.\n\nReason: ${json.reason}`);
        return;
    }
    const projects = json.projects;
    for (const project of projects) {
        let projectElement = document.createElement('span');
        projectElement.innerHTML = `
            <a class="Project move_when_hovered" id="Project${project.id}" href="${project.link}" target="_blank">
                <u class="HeaderTextColor fw-bold fs-4" id="Name">
                    ${project.name}
                </u>
                <div class="my-3 fs-6 fw-semibold" id="Description">
                    ${project.description}
                </div>
                <div class="fs-6 fw-semibold fst-italic" id="DateContainer">
                    <span id="StartDate">${project.startDate}</span>
                    <span>-</span>
                    <span id="EndDate">${project.endDate}</span>
                </div>
                <div class="my-3" id="SkillsContainer">
            
                </div>
            </a>
        `;
        // Replace the temp span with the a child.
        projectElement = projectElement.firstElementChild;
        loadSkills(project.skills, projectElement.querySelector("#SkillsContainer"));
        document.getElementById("ProjectRows").appendChild(projectElement);
    }
    document.getElementById("projectSpinner").style.display = "none";
});