package com.husker.weblafplugin.core.tools;

import com.intellij.codeInsight.completion.AllClassesGetter;
import com.intellij.codeInsight.completion.PlainPrefixMatcher;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.InheritanceUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.indexing.FileBasedIndex;
import com.intellij.util.ui.ImageUtil;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class Tools {

    public static PsiClass[] getAllClasses(Project project){
        ArrayList<PsiClass> classes = new ArrayList<>();
        AllClassesGetter.processJavaClasses(
                new PlainPrefixMatcher(""),
                project,
                GlobalSearchScope.allScope(project),
                (psiClass) -> true
        );
        return classes.toArray(new PsiClass[0]);
    }

    public static void getExtendedClassesInLibraries(Project project, String clazz, Consumer<PsiClass> consumer){
        AllClassesGetter.processJavaClasses(
                new PlainPrefixMatcher(""),
                project,
                GlobalSearchScope.allScope(project),
                psiClass -> {
                    if(clazz == null)
                        consumer.accept(psiClass);
                    else if(InheritanceUtil.isInheritor(psiClass, clazz))
                        consumer.accept(psiClass);
                    return true;
                }
        );
    }

    public static void getExtendedClassesInProject(Project project, String clazz, Consumer<PsiClass> consumer){
        AllClassesGetter.processJavaClasses(
                new PlainPrefixMatcher(""),
                project,
                GlobalSearchScope.projectScope(project),
                psiClass -> {
                    if(clazz == null)
                        consumer.accept(psiClass);
                    else if(InheritanceUtil.isInheritor(psiClass, clazz))
                        consumer.accept(psiClass);
                    return true;
                }
        );
    }

    public static PsiClass[] getExtendedClassesInLibraries(Project project, String clazz){
        ArrayList<PsiClass> classes = new ArrayList<>();
        getExtendedClassesInLibraries(project, clazz, classes::add);
        return classes.toArray(new PsiClass[0]);
    }

    public static PsiClass[] getExtendedClassesInProject(Project project, String clazz){
        ArrayList<PsiClass> classes = new ArrayList<>();
        getExtendedClassesInProject(project, clazz, classes::add);
        return classes.toArray(new PsiClass[0]);
    }

    public static PsiClass getClassByPath(Project project, Class<?> clazz){
        return getClassByPath(project, clazz.getName());
    }

    public static PsiClass getClassByPath(Project project, String clazz){
        if(clazz == null)
            return null;

        return JavaPsiFacade.getInstance(project).findClass(clazz, GlobalSearchScope.allScope(project));
    }

    public static VirtualFile[] getFilesByFileType(Project project, FileType fileType){
        return FileBasedIndex.getInstance().getContainingFiles(
                FileTypeIndex.NAME,
                fileType,
                GlobalSearchScope.allScope(project)
        ).toArray(new VirtualFile[0]);
    }


    public static PsiDirectory getSelectedDirectory(AnActionEvent event){
        return LangDataKeys.IDE_VIEW.getData(event.getDataContext()).getOrChooseDirectory();
    }

    public static void createAndOpen(AnActionEvent event, PsiFile file){
        if(event.getProject() == null)
            return;

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
        return FileTypeManager.getInstance().getFileTypeByExtension("png");
    }

    public static VirtualFile getVirtualFile(String path){
        if(path.contains(".jar!"))
            return JarFileSystem.getInstance().findLocalVirtualFileByPath(path);
        else {
            try {
                return VfsUtil.findFileByURL(new File(path).toURI().toURL());
            }catch (Exception ex){
                return null;
            }
        }
    }

    public static void waitForThread(Thread thread){
        try {
            thread.join();
        }catch (Exception ignored){}
    }

    public static JBScrollPane createScrollPane(JComponent component){
        JBScrollPane scroll = (JBScrollPane) ScrollPaneFactory.createScrollPane(component, true);

        scroll.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                JComponent component = (JComponent) scroll.getViewport().getView();
                if(scroll.getVerticalScrollBar().isShowing())
                    component.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, -14));
                else
                    component.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            }
        });
        return scroll;
    }

    public static <T> T[] ListToArray(List<T> list){
        if(list.size() == 0)
            return null;

        T[] stackArray = (T[]) Array.newInstance(list.get(0).getClass(), list.size());
        for (int i = 0; i < list.size(); i++)
            stackArray[i] = list.get(i);
        return stackArray;
    }

    public static String getClassResourcePath(PsiClass clazz){
        if(clazz == null)
            return null;
        String class_path = clazz.getContainingFile().getVirtualFile().getPath();
        return new File(class_path).getParent().replace("\\", "/");
    }

    public static Icon getModuleIcon(PsiClass psiClass){
        if(psiClass != null) {
            ProjectFileIndex fileIndex = ProjectRootManager.getInstance(psiClass.getProject()).getFileIndex();
            VirtualFile vFile = PsiUtilCore.getVirtualFile(psiClass);
            if (vFile != null && fileIndex.isInLibrary(vFile)) {
                return AllIcons.Nodes.PpLibFolder;
            } else {
                Module module = ModuleUtilCore.findModuleForPsiElement(psiClass);
                if (module != null)
                    return ModuleType.get(module).getIcon();
                else
                    return null;
            }
        }

        return null;
    }

    public static Icon getDarkerIcon(Icon icon){
        BufferedImage image = ImageUtil.createImage(icon.getIconWidth(), icon.getIconHeight(), TYPE_INT_ARGB);
        icon.paintIcon(null, image.getGraphics(), 0, 0);

        WritableRaster wr = image.getRaster();
        int[] pixel = new int[4];
        for(int i = 0; i < wr.getWidth(); i++){
            for(int j = 0; j < wr.getHeight(); j++){
                wr.getPixel(i, j, pixel);
                float percent = UIUtil.isUnderDarcula() ? 0.6f : 1.6f;
                pixel[0] = (int) (pixel[0] * percent);
                pixel[1] = (int) (pixel[1] * percent);
                pixel[2] = (int) (pixel[2] * percent);
                wr.setPixel(i, j, pixel);
            }
        }

        BufferedImage new_image = ImageUtil.createImage(icon.getIconWidth(), icon.getIconHeight(), TYPE_INT_ARGB);
        new_image.getGraphics().drawImage(image, 0, 0, null);

        return new ImageIcon(new_image);
    }
}
