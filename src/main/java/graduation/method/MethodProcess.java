package graduation.method;

import graduation.entity.GitHubUser;
import graduation.entity.Project;

//@FunctionalInterface
public interface MethodProcess {
    double userProcess(GitHubUser user);
     double projectsProcess(Project project);
}