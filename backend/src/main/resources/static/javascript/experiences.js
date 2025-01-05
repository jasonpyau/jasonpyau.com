import { apiCall } from "./apiCall.js";
import { loadSkills } from "./skills.js";

addEventListener('DOMContentLoaded', async(e) => {
    const url = `/experiences/get`;
    const result = await apiCall(url, "GET", null, null);
    const json = await result.json();
    if (result.status !== 200) {
        alert(`Error in loading experiences, refresh. If this problem persists, contact me.\n\nReason: ${json.reason}`);
        return;
    }
    const experiences = json.experiences;
    if (!experiences.length) {
        document.getElementById("experienceSpinner").style.display = "none";
        return;
    }
    const lastExperience = experiences[experiences.length-1];
    experiences.map((experience) => {
        let experienceElement = document.createElement('div');
        experienceElement.innerHTML = `
            <div class="Rounded Experience my-1 py-2 border border-white" id="Experience${experience.id}">
                <div class="fs-3 fw-bold mx-4 my-2 text-decoration-underline" id="Position">
                    ${experience.position}
                </div>
                <div class="d-flex mx-4">
                    <div class="w-100 mx-2">
                        <div class="fs-5 my-2">
                            ${experience.organizationLink ? `
                                <a class="fw-bold text-decoration-underline text-white" id="Organization" href="${experience.organizationLink}" target="_blank">${experience.organization}</a>
                                `:`
                                <span class="fw-bold text-decoration-underline" id="Organization">${experience.organization}</span>
                            `}
                            <span class="fw-semibold" id="Location">- ${experience.location}</span>   
                        </div>
                        <div class="fs-6 my-2 fw-semibold fst-italic" id="DateContainer">
                            <span id="StartDate">${experience.startDate}</span>
                            <span>-</span>
                            <span id="EndDate">${experience.present ? "Present" : experience.endDate}</span>
                        </div>
                        <div class="fs-6 my-4 fw-semibold me-3" id="Body">
                            ${experience.body}
                        </div>
                        <div class="my-3" id="ExperienceSkillsContainer">
                        </div>
                    </div>
                    <img class="ExperienceLogo my-3" height="180px" width="180px" src="${experience.logoLink}"/>
                </div>
            </div>
        `;
        // Replace the temp div with the div child.
        experienceElement = experienceElement.firstElementChild;
        loadSkills(experience.skills, experienceElement.querySelector("#ExperienceSkillsContainer"));
        document.getElementById("ExperienceContainer").appendChild(experienceElement);
        if (experience !== lastExperience) {
            let verticalLine = document.createElement("div");
            verticalLine.innerHTML = `
                <div class="d-flex justify-content-center">
                    <div class="VerticalLine"></div>
                </div>
            `;
            // Replace the temp div with the div child.
            verticalLine = verticalLine.firstElementChild;
            document.getElementById("ExperienceContainer").appendChild(verticalLine);
        }
    });
    document.getElementById("experienceSpinner").style.display = "none";
    if (window.location.hash) {
        const element = document.querySelector(window.location.hash);
        if (element) {
            element.scrollIntoView({behavior: "smooth"});
        }
    }
});