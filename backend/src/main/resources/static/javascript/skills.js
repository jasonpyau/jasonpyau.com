import { apiCall } from "./apiCall.js";

$(document).ready(async function() {
    const url = '/skills/get';
    const result = await apiCall(url, "GET", null, null);
    const json = await result.json();
    if (result.status !== 200) {
        alert(`Error in loading skills, refresh. If this problem persists, contact me.\n\nReason: ${json.reason}`);
        return;
    }
    const skillsByType = json.skills;
    Object.keys(skillsByType).forEach(function(key) {
        const rowHTML = document.getElementById("SkillsTypeRowTemplate").cloneNode(true);
        rowHTML.id = "";
        rowHTML.querySelector("#SkillType").textContent = key;
        const SkillsRowContainer = rowHTML.querySelector("#SkillsRowContainer");
        const skills = skillsByType[key];
        loadSkills(skills, SkillsRowContainer);
        document.getElementById("SkillsTypeRow").appendChild(rowHTML);
        rowHTML.style.display = "inline";
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