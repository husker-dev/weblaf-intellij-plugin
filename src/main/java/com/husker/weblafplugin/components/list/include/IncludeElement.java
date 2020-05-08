package com.husker.weblafplugin.components.list.include;

import com.intellij.openapi.project.Project;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;


public class IncludeElement {

    private String resource_path, passive_path, path, nearClass;
    private Project project;

    public IncludeElement(Project project, String resource_path, String path, String nearClass){
        this.resource_path = resource_path;
        this.path = path;
        this.project = project;
        this.nearClass = nearClass;
    }

    public String getResourcePath(){
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

    public String getPath(){
        return path;
    }
    public void setPath(String path){
        this.path = path;
    }

    public String getNearClass() {
        return nearClass;
    }
    public void setNearClass(String nearClass) {
        this.nearClass = nearClass;
    }

    public String getPassivePath() {
        return passive_path == null ? "" : passive_path;
    }
    public void setPassivePath(String passive_path) {
        this.passive_path = passive_path;
    }

    public String getExtension(){
        return path.substring(path.lastIndexOf("."));
    }

    public boolean equals(Object obj) {
        if(obj instanceof IncludeElement){
            IncludeElement compare = (IncludeElement)obj;
            return compare.getPath().equals(getPath()) &&
                    (compare.getNearClass() == null && getNearClass() == null || compare.getNearClass().equals(getNearClass())) &&
                    compare.getExtension().equals(getExtension()) &&
                    compare.getPassivePath().equals(getPassivePath()) &&
                    compare.getResourcePath().equals(getResourcePath());
        }else
            return false;
    }

    public Element getElement(Namespace namespace){
        return new Element("include", namespace){{

            if(getNearClass() != null)
                setAttribute(new Attribute("nearClass", getNearClass()));
            setText(getPath());
        }};
    }
}
