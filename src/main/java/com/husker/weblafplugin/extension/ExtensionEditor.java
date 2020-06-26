package com.husker.weblafplugin.extension;

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.XmlSkin;
import com.husker.weblafplugin.core.WLF_TypeChecker;
import com.husker.weblafplugin.core.editor.SimpleXmlParameterEditor;
import com.husker.weblafplugin.core.managers.ParameterManager;
import com.husker.weblafplugin.core.tools.Tools;
import com.husker.weblafplugin.skin.components.IconViewer;
import com.husker.weblafplugin.skin.components.parameter.Parameter;
import com.husker.weblafplugin.skin.components.parameter.ParameterBlock;
import com.husker.weblafplugin.skin.parameters.TextParameter;
import com.husker.weblafplugin.skin.parameters.impl.*;
import com.husker.weblafplugin.skin.variables.impl.*;
import com.husker.weblafplugin.extension.parameters.impl.ExtendsParameter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.vfs.VirtualFile;
import icons.MyFileIcons;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class ExtensionEditor extends SimpleXmlParameterEditor {

    public static final int IMAGE_SIZE = 90;
    public static final int IMAGE_PARAMETER_SIZE = Parameter.DEFAULT_WIDTH - IMAGE_SIZE - 35;

    private TextButtonParameter p_id = new TextButtonParameter("Id", "Auto", IMAGE_PARAMETER_SIZE);
    private ClassChooserParameter p_class = new ClassChooserParameter("Class", XmlSkin.class, IMAGE_PARAMETER_SIZE){{
        addBlackListClass("com.alee.managers.style.XmlSkin");
    }};

    private TextParameter p_author = new TextParameter("Author", IMAGE_PARAMETER_SIZE);

    private ExtendsParameter p_extends = new ExtendsParameter("Extends");

    private IncludeListParameter p_include = new IncludeListParameter("Include");
    private ClassListParameter p_iconSet = new ClassListParameter("Icon sets");

    public ExtensionEditor(Project project, VirtualFile file) {
        super(project, file);

        setTitle("Extension parameters");
        setPreferredTabIndex(2);
        setIcon(MyFileIcons.EXTENSION);
        setFileFormattingType(WLF_TypeChecker.EXTENSION);

        add(new ParameterBlock("Settings"){{
            add(new JPanel(){{
                setLayout(new BorderLayout());
                setBorder(BorderFactory.createEmptyBorder(0, 0, -5, 0));

                add(new JPanel(){{
                    setLayout(new VerticalFlowLayout(0, 5));
                    add(p_id);
                    add(p_class);
                    add(p_author);
                }}, BorderLayout.WEST);
                add(new JLabel(WebLookAndFeel.getIcon(128)){{
                    Image icon = WebLookAndFeel.getIcon(128).getImage();
                    BufferedImage buffered = new BufferedImage(icon.getWidth(null), icon.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                    buffered.getGraphics().drawImage(icon, 0, 0 , null);

                    setIcon(new ImageIcon(buffered.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH)));

                    setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
                }});
            }});

            add(p_extends);
        }});





        add(new ParameterBlock("Resources"){{
            add(p_include);
            add(p_iconSet);
        }});

        ParameterManager.register(p_author, new AuthorVariable(this));

        ParameterManager.register(p_id, new IdVariable(this));
        ParameterManager.register(p_class, new ClassVariable(this));

        ParameterManager.register(p_extends, new ExtendsVariable(this));

        ParameterManager.register(p_include, new IncludeVariable(this));
        ParameterManager.register(p_iconSet, new IconSetsVariable(this));

        ParameterManager.init(this);

        p_id.addButtonListener(e -> p_id.setText(generateId()));
    }

    public String generateId(){
        return p_author.getText().toLowerCase() + "." + getVirtualFile().getName().replace(".xml", "").replaceAll("\\s", ".").replaceAll("-", ".").replaceAll("_", ".");
    }
}
