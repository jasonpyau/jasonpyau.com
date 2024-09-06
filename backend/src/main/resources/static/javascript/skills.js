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

const iconJsonData = {};

addEventListener('DOMContentLoaded', async(e) => {
    const url = "https://raw.githubusercontent.com/simple-icons/simple-icons/6.23.0/_data/simple-icons.json";
    const result = await apiCall(url, "GET", null, null);
    const json = await result.json();
    Object.assign(iconJsonData, json);
});

export async function loadSkills(skills, container) {
    for (const skill of skills) {
        loadSkill(skill, container);
    }
}

async function loadSkill(skill, container) {
    const element = document.createElement('span');
    container.appendChild(element);
    let iconElement = "";
    if (skill.simpleIconsIconSlug) {
        iconElement = await getIconElement(`https://cdn.simpleicons.org/${skill.simpleIconsIconSlug}`);
        iconElement = iconElement || await getIconElement(`https://raw.githubusercontent.com/simple-icons/simple-icons/6.23.0/icons/${skill.simpleIconsIconSlug}.svg`);
        if (iconElement) {
            iconElement = new DOMParser().parseFromString(iconElement, "text/xml").firstChild;
            iconElement.classList.add("mx-1");
            iconElement.style.height = 16;
            iconElement.style.width = 16;
            // If this is true, we used simple-icons/6.23.0.
            if (!iconElement.getAttribute("fill")) {
                const svgTitle = iconElement.querySelector("title").textContent;
                iconElement.setAttribute("fill", getHexColor(svgTitle));
            }
        }
    }
    element.className = "m-1 btn btn-dark btn-sm";
    element.innerHTML = `
        ${(iconElement) ? iconElement.outerHTML : ""}
        <span>
            ${skill.name}
        </span>
    `;
}

async function getIconElement(path) {
    const result = await apiCall(path, "GET", null, null);
    if (result.status === 200) {
        return await result.text();
    }
    return "";
}

function getHexColor(svgTitle) {
    return `#${iconJsonData.icons.find((icon) => icon.title === svgTitle).hex}`;
}