package ru.salerman.bitrixstorm;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import ru.salerman.bitrixstorm.bitrix.BitrixComponentManager;
import ru.salerman.bitrixstorm.bitrix.BitrixUtils;

/**
 * Created by r3c130n on 21.01.14.
 */
public class BitrixStormProjectComponent implements ProjectComponent {
    private Project _project;


    public BitrixStormProjectComponent(Project project) {
        _project = project;
        BitrixUtils.setProject(project);
    }

    public static BitrixStormProjectComponent getInstance(Project project) {
        return project.getComponent(BitrixStormProjectComponent.class);
    }

    public void initComponent() {
        // TODO: insert component initialization logic here
    }

    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @NotNull
    public String getComponentName() {
        return "BitrixStormProjectComponent";
    }

    public void projectOpened() {
        // called when project is opened
        BitrixUtils.setProject(_project);
    }

    public void projectClosed() {
        // called when project is being closed
    }
}
