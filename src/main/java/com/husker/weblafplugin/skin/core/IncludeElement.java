package com.husker.weblafplugin.skin.core;

import com.husker.weblafplugin.core.tools.Tools;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;


public class IncludeElement {

    private String resource_path, passive_path, path, nearClass, nearClassPath;
    private Project project;

    public IncludeElement(Project project, String resource_path, String path, String nearClass){
        this.resource_path = resource_path;
        this.path = path.replace("\\", "/");
        this.project = project;
        this.nearClass = nearClass;
        try {
            if (nearClass != null && !nearClass.equals("")) {
                PsiClass clazz = Tools.getClassByPath(project, nearClass);
                nearClassPath = new File(clazz.getContainingFile().getVirtualFile().getPath()).getParent().replace("\\", "/");
            }
        }catch (Exception ex){
            nearClassPath = "";
        }
    }

    public String getResourcePath(){
        if(nearClass != null && !nearClass.isEmpty())
            return nearClassPath;
        return resource_path;
    }
    public void setResourcePath(String resource_path){
        this.resource_path = resource_path;
    }

    public Project getProject(){
        return project;
    }
    public void setProject(Project project){
        this.project = project;
    }

    public String getLocalPath(){
        return path;
    }
    public void setLocalPath(String path){
        this.path = path;
    }

    public String getNearClass() {
        return nearClass;
    }
    public void setNearClass(String nearClass) {
        this.nearClass = nearClass;
    }

    public String getPassivePath() {
        return getLocalPath().substring(0, getLocalPath().lastIndexOf("/")) + "/";
    }

    public String getExtension(){
        if(path.contains("."))
            return path.substring(path.lastIndexOf("."));
        else
            return "";
    }

    public String getFullPath(){
        return getResourcePath() + "/" + getLocalPath();
    }

    public boolean equals(Object obj) {
        if(obj instanceof IncludeElement){
            IncludeElement compare = (IncludeElement)obj;

            return  Objects.equals(compare.getLocalPath(), getLocalPath()) &&
                    Objects.equals(compare.getNearClass(), getNearClass()) &&
                    Objects.equals(compare.getExtension(), getExtension()) &&
                    Objects.equals(compare.getPassivePath(), getPassivePath()) &&
                    Objects.equals(compare.getResourcePath(), getResourcePath());
        }else
            return false;
    }

    public int hashCode() {
        return Objects.hash(resource_path, passive_path, path, nearClass, project);
    }

    public String getFileText(){
        try {
            return String.join(System.lineSeparator(), Files.readAllLines(Paths.get(resource_path + "/" + path)).toArray(new String[0]));
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public Element generateElement(Namespace namespace){
        return new Element("include", namespace){{
            if(getNearClass() != null)
                setAttribute(new Attribute("nearClass", getNearClass()));
            setText(getLocalPath());
        }};
    }
}
