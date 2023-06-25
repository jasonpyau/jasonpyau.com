import { apiCall } from "./apiCall.js";

$(document).ready(function() {
    $.get("projects", async function(projectTemplate) {
        $("#ProjectRows").append(projectTemplate);
        const url = `/projects/get`;
        const result = await apiCall(url, "GET", null, null);
        if (result.status !== 200) {
            alert("Error in loading projects, refresh. If this problem persists, contact me.");
            return;
        }
        const json = await result.json();
        const projects = json.projects;
        for (let i = 0; i < projects.length; i++) {
            const project = projects[i];
            const projectElement = new Project(i);
            projectElement.setName(project.name);
            projectElement.setDescription(project.description);
            projectElement.setStartDate(project.startDate);
            projectElement.setEndDate(project.endDate);
            projectElement.setTechnologies(project.technologies);
            projectElement.setLink(project.link);
            projectElement.show();
        }
        document.getElementById("projectSpinner").style.display = "none";
    });
});

class Project {
    #element;
    #name;
    #description;
    #startDate;
    #endDate;
    #technologies = [];
    #link;
    constructor(id) {
        $("#ProjectTemplate").clone()
                                .prop('id', "Project"+id)
                                .appendTo("#ProjectRows");
        this.#element = document.querySelector(`#ProjectRows #Project${id}`);
    }

    setName(name) {
        this.#name = name;
    }

    setDescription(description) {
        this.#description = description;
    }

    setStartDate(startDate) {
        this.#startDate = startDate;
    }

    setEndDate(endDate) {
        this.#endDate = endDate;
    }

    setTechnologies(technologies) {
        this.#technologies = technologies;
    }

    setLink(link) {
        this.#link = link;
    }

    show() {
        this.#element.querySelector("#Name").innerHTML = this.#name;
        this.#element.querySelector("#Description").innerHTML = this.#description;
        this.#element.querySelector("#StartDate").innerHTML = this.#startDate;
        this.#element.querySelector("#EndDate").innerHTML = this.#endDate;
        this.#element.href = this.#link;
        const technologiesContainer = this.#element.querySelector("#TechnologiesContainer");
        for (const technology of this.#technologies) {
            const element = document.createElement('span');
            element.innerHTML = technology;
            element.className = "mx-1 mb-1 btn btn-dark btn-sm";
            technologiesContainer.appendChild(element);
        }
        this.#element.style.display = "block";
    }
};