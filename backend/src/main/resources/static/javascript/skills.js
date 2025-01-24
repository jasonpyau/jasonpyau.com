import { apiCall } from "./apiCall.js";

addEventListener('DOMContentLoaded', async(e) => {
    const url = '/skills/get';
    const result = await apiCall(url, "GET", null, null);
    const json = await result.json();
    if (result.status !== 200) {
        alert(`Error in loading skills, refresh. If this problem persists, contact me.\n\nReason: ${json.reason}`);
        return;
    }
    const skillsByType = json.skills;
    Object.keys(skillsByType).map((type) => {
        const skillsRow = document.createElement('div');
        skillsRow.innerHTML = `
            <u class="fs-3 HeaderTextColor fw-bold" id="SkillType">${type}</u>
            <div class="Rounded my-3 py-2 SkillsRowContainer" id="SkillsRowContainer">

            </div>
        `;
        const skillsRowContainer = skillsRow.querySelector("#SkillsRowContainer");
        loadSkills(skillsByType[type], skillsRowContainer);
        document.getElementById("SkillsTypeRow").appendChild(skillsRow);
    });
    document.getElementById("skillSpinner").style.display = "none";
    if (window.location.hash) {
        const element = document.querySelector(window.location.hash);
        if (element) {
            element.scrollIntoView({behavior: "smooth"});
        }
    }
});

export function loadSkills(skills, container) {
    for (const skill of skills) {
        loadSkill(skill, container);
    }
}

function loadSkill(skill, container) {
    const element = document.createElement('span');
    element.className = "m-1 btn btn-dark btn-sm";
    element.innerHTML = `
        <a class="text-decoration-none text-white" title="${skill.name}" ${(skill.link) ? `href="${skill.link}" target="_blank"` : `href="javascript:void(0);"`}>
            ${skill.simpleIconsIconSlug ? `<img src="/skills/svg/${encodeURIComponent(skill.name)}" class="mx-1" height="16px" width="16px" alt="${skill.name} Icon" loading="lazy"/>` : ""}
            <span>
                ${skill.name}
            </span>
        </a>
    `;
    container.appendChild(element);
}