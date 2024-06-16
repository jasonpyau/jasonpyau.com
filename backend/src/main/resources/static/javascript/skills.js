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
    Object.keys(skillsByType).forEach((key) => {
        const skillsRow = document.createElement('div');
        skillsRow.innerHTML = `
            <u class="fs-3 HeaderTextColor fw-bold" id="SkillType">${key}</u>
            <div class="Rounded my-3 py-2 SkillsRowContainer" id="SkillsRowContainer">

            </div>
        `;
        const skillsRowContainer = skillsRow.querySelector("#SkillsRowContainer");
        loadSkills(skillsByType[key], skillsRowContainer);
        document.getElementById("SkillsTypeRow").appendChild(skillsRow);
        document.getElementById("skillSpinner").style.display = "none";
    });
});

export function loadSkills(skills, container) {
    for (const skill of skills) {
        const element = document.createElement('span');
        element.className = "m-1 btn btn-dark btn-sm";
        element.innerHTML = 
        ((skill.simpleIconsIconSlug) ? 
        `<img height="16" width="16" src="https://cdn.simpleicons.org/${skill.simpleIconsIconSlug}" class="mx-1"/>` : "") +
        `
            <span>
                ${skill.name}
            </span>
        `;
        container.appendChild(element);
    }
}