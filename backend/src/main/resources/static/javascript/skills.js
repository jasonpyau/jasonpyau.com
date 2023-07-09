import { apiCall } from "./apiCall.js";

$(document).ready(async function() {
    const url = 'skills/get';
    const result = await apiCall(url, "GET", null, null);
        if (result.status !== 200) {
            alert("Error in loading skills, refresh. If this problem persists, contact me.");
            return;
        }
        const json = await result.json();
        const skillsByType = json.skills;
        Object.keys(skillsByType).forEach(function(key) {
            const rowHTML = document.getElementById("SkillsTypeRowTemplate").cloneNode(true);
            rowHTML.id = "";
            rowHTML.querySelector("#SkillType").innerHTML = key;
            const SkillsRowContainer = rowHTML.querySelector("#SkillsRowContainer");
            const skills = skillsByType[key];
            for (const skill of skills) {
                const element = document.createElement('span');
                element.innerHTML = skill;
                element.className = "mx-1 my-2 btn btn-dark btn-sm";
                SkillsRowContainer.appendChild(element);
            }
            document.getElementById("SkillsTypeRow").appendChild(rowHTML);
            rowHTML.style.display = "inline";
            document.getElementById("skillSpinner").style.display = "none";
        });
});