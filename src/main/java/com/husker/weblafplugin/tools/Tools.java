package com.husker.weblafplugin.tools;

import com.google.common.collect.Lists;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.InheritanceUtil;
import com.intellij.util.indexing.FileBasedIndex;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class Tools {

    public static PsiClass[] getClasses(Project project){
        ArrayList<VirtualFile> files = new ArrayList<>(Arrays.asList(getFilesByFileType(project, JavaFileType.INSTANCE)));

        ArrayList<String> paths = new ArrayList<>();
        for(VirtualFile file : files)
            paths.add(file.getPath());

        ArrayList<String> classes_path = new ArrayList<>();
        for(String path : paths){
            for(VirtualFile file : ProjectRootManager.getInstance(project).getContentSourceRoots())
                path = path.replace(file.getPath() + "/", "");
            path = path.replace("/", ".");
            path = path.substring(0, path.length() - 5);
            classes_path.add(path);
        }

        ArrayList<PsiClass> psi_classes = new ArrayList<>();
        for(String class_path : classes_path)
            psi_classes.add(JavaPsiFacade.getInstance(project).findClass(class_path, GlobalSearchScope.projectScope(project)));

        return psi_classes.toArray(new PsiClass[0]);
    }

    public static PsiClass[] getExtendedClasses(Project project, Class clazz){
        return getExtendedClasses(project, clazz.getName());
    }

    public static PsiClass[] getExtendedClasses(Project project, String clazz){
        ArrayList<PsiClass> classes = new ArrayList<>(Arrays.asList(getClasses(project)));
        ArrayList<PsiClass> new_classes = new ArrayList<>();

        for(PsiClass psiClass : classes)
            if(InheritanceUtil.isInheritor(psiClass, clazz))
                new_classes.add(psiClass);

        return new_classes.toArray(new PsiClass[0]);
    }

    public static PsiClass getClassByPath(Project project, Class clazz){
        return getClassByPath(project, clazz.getName());
    }

    public static PsiClass getClassByPath(Project project, String clazz){
        for(PsiClass psiClass : getClasses(project))
            if(psiClass.getQualifiedName().equals(clazz))
                return psiClass;
        return null;
    }

    public static VirtualFile[] getFilesByFileType(Project project, FileType fileType){
        return FileBasedIndex.getInstance().getContainingFiles(
                FileTypeIndex.NAME,
                fileType,
                GlobalSearchScope.projectScope(project)
        ).toArray(new VirtualFile[0]);
    }


    public static PsiDirectory getSelectedDirectory(AnActionEvent event){
        return LangDataKeys.IDE_VIEW.getData(event.getDataContext()).getOrChooseDirectory();
    }

    public static void createAndOpen(AnActionEvent event, PsiFile file){
        ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                // Create
                PsiDirectory directory = getSelectedDirectory(event);
                directory.add(file);

                // Open
                for (PsiFile psi_file : directory.getFiles()) {
                    if (psi_file.getName().equals(file.getName())) {
                        FileEditorManager.getInstance(event.getProject()).openFile(psi_file.getVirtualFile(), true);
                        break;
                    }
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });
    }

    public static PsiFile getPsi(Project project, VirtualFile file){
        return PsiManager.getInstance(project).findFile(file);
    }

    public static void writeText(VirtualFile file, String text){
        ApplicationManager.getApplication().runWriteAction(() -> {
            try (PrintWriter p = new PrintWriter(file.getOutputStream(null))) {
                p.print(text);
                p.flush();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });
    }

    public static FileType getImageFileType(){
        try {
            FileType fileType = FileTypeManager.getInstance().getFileTypeByExtension("png");

            return (FileType)fileType.getClass().getField("INSTANCE").get(fileType);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }

    }
}
