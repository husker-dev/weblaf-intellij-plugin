package com.husker.weblafplugin.components.list.include;

import com.husker.weblafplugin.tools.XmlTools;
import com.intellij.openapi.project.Project;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;


public class IncludeElement {

    private String resource_path, passive_path, path, nearClass;
    private Project project;
    private Element element;

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

            return  Objects.equals(compare.getPath(), getPath()) &&
                    Objects.equals(compare.getNearClass(), getNearClass()) &&
                    Objects.equals(compare.getExtension(), getExtension()) &&
                    Objects.equals(compare.getPassivePath(), getPassivePath()) &&
                    Objects.equals(compare.getResourcePath(), getResourcePath());
        }else
            return false;
    }

    public String getFileText(){
        try {
            return String.join(System.lineSeparator(), Files.readAllLines(Paths.get(resource_path + "/" + path)).toArray(new String[0]));
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public Element getFileElement(){
        if(element == null) {
            try {
                return XmlTools.getElement(getFileText());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }else
            return element;
    }

    public Element generateElement(Namespace namespace){
        return new Element("include", namespace){{

            if(getNearClass() != null)
                setAttribute(new Attribute("nearClass", getNearClass()));
            setText(getPath());
        }};
    }
}
