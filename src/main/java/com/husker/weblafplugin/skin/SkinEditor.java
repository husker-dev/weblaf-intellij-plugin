package com.husker.weblafplugin.skin;

import com.alee.managers.style.XmlSkin;
import com.husker.weblafplugin.core.editor.SimpleXmlParameterEditor;
import com.husker.weblafplugin.core.managers.ParameterManager;
import com.husker.weblafplugin.skin.components.IconViewer;
import com.husker.weblafplugin.skin.components.parameter.Parameter;
import com.husker.weblafplugin.skin.components.parameter.ParameterBlock;
import com.husker.weblafplugin.skin.parameters.LabelParameter;
import com.husker.weblafplugin.skin.parameters.TextParameter;
import com.husker.weblafplugin.skin.parameters.impl.*;
import com.husker.weblafplugin.skin.variables.impl.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.vfs.VirtualFile;
import icons.MyFileIcons;

import javax.swing.*;
import java.awt.*;


public class SkinEditor extends SimpleXmlParameterEditor {

    public static final int IMAGE_SIZE = 90;
    public static final int IMAGE_PARAMETER_SIZE = Parameter.DEFAULT_WIDTH - IMAGE_SIZE - 35;

    private IconViewer p_large_icon = new IconViewer(IMAGE_SIZE){{
        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
    }};
    private TextParameter p_title = new TextParameter("Title", IMAGE_PARAMETER_SIZE);
    private TextParameter p_author = new TextParameter("Author", IMAGE_PARAMETER_SIZE);
    private ImageChooserParameter p_icon = new ImageChooserParameter("Icon", IMAGE_PARAMETER_SIZE);
    private TextParameter p_description = new TextParameter("Description");

    private ClassChooserParameter p_class = new ClassChooserParameter("Class", XmlSkin.class){{
        addBlackListClass("com.alee.managers.style.XmlSkin");
    }};
    private TextButtonParameter p_id = new TextButtonParameter("Id", "Auto");
    private SupportedSystemsParameter p_supported_systems = new SupportedSystemsParameter("OS support");

    private LabelParameter p_resources_path = new LabelParameter("Path");

    private IncludeListParameter p_include = new IncludeListParameter("Include");
    private ClassListParameter p_iconSet = new ClassListParameter("Icon sets");

    public SkinEditor(Project project, VirtualFile file) {
        super(project, file);

        setTitle("Skin parameters");
        setIcon(MyFileIcons.SKIN);
        setPreferredTabIndex(1);

        add(new ParameterBlock("Information"){{
            add(new JPanel(){{
                setLayout(new BorderLayout());
                setBorder(BorderFactory.createEmptyBorder(0, 0, -5, 0));

                add(new JPanel(){{
                    setLayout(new VerticalFlowLayout(0, 5));
                    add(p_title);
                    add(p_author);
                    add(p_icon);
                }}, BorderLayout.WEST);
                add(p_large_icon);
            }});


            add(p_description);
        }});

        add(new ParameterBlock("Settings"){{
            add(p_class);
            add(p_id);
            add(p_supported_systems);
        }});

        add(new ParameterBlock("Resources"){{
            add(p_resources_path);
            add(p_include);
            add(p_iconSet);
        }});

        // Binding
        ParameterManager.register(p_large_icon, new IconVariable(SkinEditor.this));
        ParameterManager.register(p_title, new TitleVariable(SkinEditor.this));
        ParameterManager.register(p_author, new AuthorVariable(SkinEditor.this));
        ParameterManager.register(p_icon, new IconVariable(SkinEditor.this));
        ParameterManager.register(p_description, new DescriptionVariable(SkinEditor.this));

        ParameterManager.register(p_class, new ClassVariable(SkinEditor.this));
        ParameterManager.register(p_id, new IdVariable(SkinEditor.this));
        ParameterManager.register(p_supported_systems, new SupportedSystemVariable(SkinEditor.this));

        ParameterManager.register(p_resources_path, new ResourcePathVariable(SkinEditor.this));
        ParameterManager.register(p_include, new IncludeVariable(SkinEditor.this));
        ParameterManager.register(p_iconSet, new IconSetsVariable(SkinEditor.this));

        ParameterManager.init(this);

        p_id.addButtonListener(e -> p_id.setText(generateId()));
    }

    public String generateId(){
        String id = "";
        if(!p_author.getText().replaceAll("\\s", "").equals(""))
            id = p_author.getText().toLowerCase().split(" ")[0] + ".";
        else
            id = "";
        id += p_title.getText().toLowerCase().replaceAll("\\s", ".");
        return id;
    }

}
