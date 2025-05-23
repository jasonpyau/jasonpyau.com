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
    const experiencesByType = json.experiences;
    Object.keys(experiencesByType).map((type) => {
        loadExperiences(experiencesByType[type], type);
    });
    if (window.location.hash) {
        const element = document.querySelector(window.location.hash);
        if (element) {
            element.scrollIntoView({behavior: "smooth"});
        }
    }
});

function loadExperiences(experiences, type) {
    const spinnerId = (type === "WORK_EXPERIENCE") ? "experienceSpinner" : "educationSpinner";
    const containerId = (type === "WORK_EXPERIENCE") ? "ExperienceContainer" : "EducationContainer";
    const spinner = document.getElementById(spinnerId);
    const container = document.getElementById(containerId);
    if (!experiences.length) {
        spinner.style.display = "none";
        return;
    }
    const lastExperience = experiences[experiences.length-1];
    experiences.map((experience) => {
        let experienceElement = document.createElement('div');
        if (type === "WORK_EXPERIENCE") {
            experienceElement.innerHTML = `
                <div class="Rounded Experience my-1 py-2 border border-white" id="Experience${experience.id}">
                    <div class="fs-3 fw-bold mx-4 my-2" id="Position">
                        ${experience.position}
                    </div>
                    <div class="d-flex mx-4">
                        <div class="w-100 mx-2">
                            <div class="fs-5 my-1" title="${experience.organization} - ${experience.location}">
                                ${experience.organizationLink ? `
                                    <a class="fw-bold text-decoration-underline text-white opaque_when_hovered" id="Organization" href="${experience.organizationLink}" target="_blank">${experience.organization}</a>
                                    `:`
                                    <span class="fw-bold text-decoration-underline" id="Organization">${experience.organization}</span>
                                `}
                                <span class="fw-semibold" id="Location">- ${experience.location}</span>   
                            </div>
                            <div class="fs-6 my-1 fw-semibold" id="DateContainer">
                                <span id="StartDate">${experience.startDate}</span>
                                <span>-</span>
                                <span id="EndDate">${experience.present ? "Present" : experience.endDate}</span>
                                <span>·</span>
                                <span class="fst-italic" id="TimeElasped">${getYrsAndMos(experience.startDate, experience.endDate)}</span>
                            </div>
                            <div class="fs-6 my-4 fw-semibold me-3" id="Body">
                                ${experience.body}
                            </div>
                            <div class="my-3" id="ExperienceSkillsContainer">
                            </div>
                        </div>
                        ${experience.organizationLink ? `<a href="${experience.organizationLink}" target="_blank" class="opaque_when_hovered">` : ""}
                            <img class="ExperienceLogo my-3" height="180px" width="180px" src="${experience.logoLink}" title="${experience.organization}" alt="${experience.organization} Logo" loading="lazy"/>
                        ${experience.organizationLink ? `</a>` : ""}
                        </div>
                </div>
            `;
        } else if (type === "EDUCATION") {
            experienceElement.innerHTML = `
                <div class="Rounded Experience my-1 py-2 border border-white" id="Experience${experience.id}">
                    <div class="fs-3 fw-bold mx-4 my-2 text-decoration-underline" title="${experience.organization} - ${experience.location}">
                        ${experience.organizationLink ? `
                            <a class="text-white opaque_when_hovered" id="Organization" href="${experience.organizationLink}" target="_blank">${experience.organization}</a>
                            `:`
                            <span id="Organization">${experience.organization}</span>
                        `}
                    </div>
                    <div class="d-flex mx-4">
                        <div class="w-100 mx-2">
                            <div class="fs-5 my-1">
                                ${experience.position}
                            </div>
                            <div class="fs-6 my-1 fw-semibold" id="DateContainer">
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
                        ${experience.organizationLink ? `<a href="${experience.organizationLink}" target="_blank" class="opaque_when_hovered">` : ""}
                            <img class="ExperienceLogo my-3" height="180px" width="180px" src="${experience.logoLink}" title="${experience.organization}" alt="${experience.organization} Logo" loading="lazy"/>
                        ${experience.organizationLink ? `</a>` : ""}
                    </div>
                </div>
            `;
        }
        // Replace the temp div with the div child.
        experienceElement = experienceElement.firstElementChild;
        loadSkills(experience.skills, experienceElement.querySelector("#ExperienceSkillsContainer"));
        container.appendChild(experienceElement);
        if (experience !== lastExperience) {
            let verticalLine = document.createElement("div");
            verticalLine.innerHTML = `
                <div class="d-flex justify-content-center">
                    <div class="VerticalLine"></div>
                </div>
            `;
            // Replace the temp div with the div child.
            verticalLine = verticalLine.firstElementChild;
            container.appendChild(verticalLine);
        }
    });
    spinner.style.display = "none";
}

function getYrsAndMos(startDate, endDate) {
    const startMonth = +startDate.substring(0, 2), startYear = +startDate.substring(3);
    const endMonth = +endDate.substring(0, 2), endYear = +endDate.substring(3);
    // If startDate = endDate, diffInMonths = 1.
    const diffInMonths = (endYear-startYear)*12+(endMonth-startMonth)+1;
    const years = Math.floor(diffInMonths/12), months = diffInMonths%12;
    if (years > 0) {
        if (months > 0) {
            return `${years} yr${years === 1 ? '' : 's'} ${months} mo${months === 1 ? '' : 's'}`;
        } else {
            return `${years} yr${years === 1 ? '' : 's'}`;
        }
    } else if (months > 0) {
        return `${months} mo${months === 1 ? '' : 's'}`;
    } else {
        return "0 mos";
    }
}