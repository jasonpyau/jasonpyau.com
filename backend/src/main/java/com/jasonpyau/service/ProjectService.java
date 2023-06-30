package com.jasonpyau.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jasonpyau.entity.Project;
import com.jasonpyau.repository.ProjectRepository;

@Service
public class ProjectService {

    public static final String PROJECT_ID_ERROR = "Invalid 'id', project not found.";
    public static final String PROJECT_NAME_ERROR = "'name' should be between 3-30 characters.";
    public static final String PROJECT_DESCRIPTION_ERROR = "'description' should be between 10-250 characters.";
    public static final String PROJECT_START_DATE_ERROR = "'startDate' should be in format 'MM/YYYY'.";
    public static final String PROJECT_END_DATE_ERROR = "'endDate' should be in format 'MM/YYYY'.";
    public static final String PROJECT_TECHNOLOGIES_ERROR = "'technologies' should have length between 1-10 and each technology between 1-15 characters.";
    public static final String PROJECT_LINK_ERROR = "'link' should be between 4-250 characters.";

    @Autowired
    private ProjectRepository projectRepository;

    // Returns error message if applicable, else null.
    public String newProject(Project project) {
        String errorMessage = checkProject(project);
        if (errorMessage != null) {
            return errorMessage;
        }
        project.createOrder();
        projectRepository.save(project);
        return null;
    }

    // Returns error message if applicable, else null.
    public String updateProject(Project updateProject, Integer id) {
        if (id == null) {
            return PROJECT_ID_ERROR;
        }
        Optional<Project> optional = projectRepository.findById(id);
        if (!optional.isPresent()) {
            return PROJECT_ID_ERROR;
        }
        Project project = optional.get();
        if (updateProject.getName() != null) {
            project.setName(updateProject.getName());
        }
        if (updateProject.getDescription() != null) {
            project.setDescription(updateProject.getDescription());
        }
        if (updateProject.getStartDate() != null) {
            project.setStartDate(updateProject.getStartDate());
        }
        if (updateProject.getEndDate() != null) {
            project.setEndDate(updateProject.getEndDate());
        }
        if (updateProject.getTechnologies() != null) {
            project.setTechnologies(updateProject.getTechnologies());
        }
        if (updateProject.getLink() != null) {
            project.setLink(updateProject.getLink());
        }
        return newProject(project);
    }

    // Returns error message if applicable, else null.
    public String deleteProject(Integer id) {
        Optional<Project> optional = projectRepository.findById(id);
        if (!optional.isPresent()) {
            return PROJECT_ID_ERROR;
        }
        projectRepository.delete(optional.get());
        return null;
    }

    public List<Project> getProjects() {
        return projectRepository.findAllByStartDateEndDate();
    }

    private String checkProject(Project project) {
        String name = project.getName();
        String description = project.getDescription();
        String startDate = project.getStartDate();
        String endDate = project.getEndDate();
        List<String> technologies = project.getTechnologies();
        String link = project.getLink();
        if (name == null || name.length() < 3 || name.length() > 30) {
            return PROJECT_NAME_ERROR;
        } else if (description == null || description.length() < 10 || description.length() > 250) {
            return PROJECT_DESCRIPTION_ERROR;
        } else if (!Project.checkDate(startDate)) {
            return PROJECT_START_DATE_ERROR;
        } else if (!Project.checkDate(endDate)) {
            return PROJECT_END_DATE_ERROR;
        } else if (technologies == null || technologies.size() < 1 || technologies.size() > 10) {
            return PROJECT_TECHNOLOGIES_ERROR;
        } else if (link == null || link.length() < 4 || link.length() > 250) {
            return PROJECT_LINK_ERROR;
        }
        for (String technology : technologies) {
            if (technology.length() < 1 || technology.length() > 15) {
                return PROJECT_TECHNOLOGIES_ERROR;
            }
        }
        return null;
    }
}
