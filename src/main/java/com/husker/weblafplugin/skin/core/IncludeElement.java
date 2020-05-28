package com.husker.weblafplugin.skin.core;

import com.husker.weblafplugin.core.tools.Tools;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;


public class IncludeElement {

    private String resource_path, path, nearClass, nearClassPath;
    private Project project;

    public IncludeElement(Project project, String resource_path, String path, String nearClass){
        this.resource_path = resource_path;
        this.path = path.replace("\\", "/");
        this.project = project;
        this.nearClass = nearClass;

        if(nearClass != null && nearClass.isEmpty())
            nearClass = null;
        try {
            if (nearClass != null) {
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

    /**
     * Example: "resources/button.xml"
     *
     * @return Returns local path relative to nearClass
     */
    public String getLocalPath(){
        return path;
    }
    public void setLocalPath(String path){
        this.path = path;
    }

    /**
     * Indicates the class relative to which the file path
     * Example: "com.alee.skin.light.WebLightSkin"
     *
     * @return Returns near class path, "null" if absent
     */
    public String getNearClass() {
        return nearClass;
    }
    public void setNearClass(String nearClass) {
        this.nearClass = nearClass;
    }

    /**
     * Example: "resources/"
     *
     * @return Returns folder path, "null" if absent
     */
    public String getFolderPath() {
        if(getLocalPath().isEmpty() || !getLocalPath().contains("/"))
            return null;
        return getLocalPath().substring(0, getLocalPath().lastIndexOf("/")) + "/";
    }

    /**
     * Example: ".xml"
     *
     * @return File extension. Usually ".xml"
     */
    public String getExtension(){
        if(path.contains("."))
            return path.substring(path.lastIndexOf("."));
        else
            return "";
    }

    /**
     * Example: "C:/Users/User/Documents/Project/src/com/alee/skin/light/resources/button.xml"
     *
     * @return Full file path to file on disc
     */
    public String getFullPath(){
        return getResourcePath() + "/" + getLocalPath();
    }

    public String getName(){
        String name = getLocalPath();
        name = name.replaceFirst(getFolderPath(), "");
        if(name.contains("."))
            name = name.substring(0, name.lastIndexOf("."));
        return name;
    }

    public boolean equals(Object obj) {
        if(obj instanceof IncludeElement){
            IncludeElement compare = (IncludeElement)obj;

            return  Objects.equals(compare.getLocalPath(), getLocalPath()) &&
                    Objects.equals(compare.getNearClass(), getNearClass()) &&
                    Objects.equals(compare.getExtension(), getExtension()) &&
                    Objects.equals(compare.getFolderPath(), getFolderPath()) &&
                    Objects.equals(compare.getResourcePath(), getResourcePath());
        }else
            return false;
    }

    public int hashCode() {
        return Objects.hash(resource_path, path, nearClass, project);
    }

    public String getFileText(){
        try {
            return new BufferedReader(new InputStreamReader(Tools.getVirtualFile(getFullPath()).getInputStream()))
                    .lines().collect(Collectors.joining(System.lineSeparator()));
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

    public VirtualFile getVirtualFile(){
        return Tools.getVirtualFile(getFullPath());
    }

    public PsiFile getPsiFile(){
        VirtualFile file = getVirtualFile();
        if(file == null)
            return null;
        else
            return PsiManager.getInstance(getProject()).findFile(file);
    }

    public Icon getIcon(){
        PsiFile file = getPsiFile();
        if (file != null)
            return file.getIcon(0);
        return FileTypeManager.getInstance().getFileTypeByFileName(getFullPath()).getIcon();
    }

    public boolean isExist() {
        return getPsiFile() != null;
    }
}
