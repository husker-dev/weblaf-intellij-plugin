package com.husker.weblafplugin.core.tools;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class XmlTools {

    public static Element getElement(String text) throws JDOMException, IOException {
        Document document = new SAXBuilder().build(new StringReader(text));

        return document.getRootElement();
    }

    public static String convertToString(Element element){
        Format format = Format.getRawFormat();
        format.setIndent("    ");
        format.setTextMode(Format.TextMode.TRIM);
        return new XMLOutputter(format).outputString(element);
    }

    public static Element setGroup(Element parent, String[] titles, String head_names){
        return setGroup(parent, titles, new String[]{head_names});
    }
    public static Element setGroup(Element parent, String titles, String head_names){
        return setGroup(parent, new String[]{titles}, new String[]{head_names});
    }
    public static Element setGroup(Element parent, String titles, String[] head_names){
        return setGroup(parent, new String[]{titles}, head_names);
    }
    public static Element setGroup(Element parent, String[] titles, String[] head_names){
        ArrayList<String> head_list = new ArrayList<>(Arrays.asList(head_names));
        ArrayList<String> title_list = new ArrayList<>(Arrays.asList(titles));

        ArrayList<Comment> comments = new ArrayList<>();
        ArrayList<Element> heads = new ArrayList<>();

        // Getting all title comments from parent element
        for(Content content : parent.getContent()) {
            if (content.getCType() == Content.CType.Comment) {
                Comment comment = (Comment) content;

                if (title_list.contains(comment.getText().trim())) {
                    title_list.remove(comment.getText().trim());

                    comments.add(comment);
                }
            }
        }

        // Removing existing comments
        for(Comment comment : comments)
            parent.removeContent(comment);

        // Creating missing comments
        for(String comment_text : title_list)
            comments.add(new Comment(comment_text));

        // Getting all heads
        for(Element element : parent.getChildren())
            if(head_list.contains(element.getName()))
                heads.add(element);

        // Removing heads from parent
        for(Element head : heads)
            parent.removeContent(head);

        if(heads.size() > 0) {
            // Adding comments and heads to bottom xml
            parent.addContent(new Text(""));
            parent.addContent(new Text(""));
            for (Comment comment : comments)
                parent.addContent(comment);
            for (Element head : heads)
                parent.addContent(head);
        }

        return parent;
    }

    public static String tabComments(String text){
        ArrayList<String> out = new ArrayList<>();

        String lines[] = text.split(System.lineSeparator());
        for(int i = 0; i < lines.length; i++){
            if(lines[i].contains("<!--"))
                if (i > 0 && !lines[i - 1].trim().equals("") && !lines[i - 1].contains("<!--"))
                    out.add(System.lineSeparator());
            out.add(lines[i] + System.lineSeparator());
        }

        return String.join("", out.toArray(new String[0]));
    }

    public static String formatElement(Element element){
        String skin_settings_group = "Skin settings";
        String skin_information_group = "Skin information";
        String icon_set_group = "Icon sets";
        String include_group = "Styles placed in strict initialization order";
        String styles_group = "Styles";

        setGroup(element, skin_settings_group, new String[]{"id", "class", "supportedSystems"});
        setGroup(element, skin_information_group, new String[]{"icon", "title", "description", "author"});
        setGroup(element, icon_set_group, "iconSet");
        setGroup(element, include_group, "include");
        setGroup(element, styles_group, "style");

        String text = convertToString(element);
        text = tabComments(text);

        return text;
    }

    public static boolean areEqual(Element element1, Element element2){
        if(element1 == null && element2 == null)
            return true;
        if(element1 == null || element2 == null)
            return false;

        // Namespace
        if(!element1.getNamespace().equals(element2.getNamespace()))
            return false;

        // Text
        if(!element1.getText().equals(element2.getText()))
            return false;

        // Attributes
        if(element1.getAttributes().size() != element2.getAttributes().size())
            return false;
        for(Attribute attribute : element1.getAttributes())
            if(!element2.getAttributes().contains(attribute))
                return false;

        // Elements
        if(element1.getChildren().size() != element2.getChildren().size())
            return false;

        // Sorting elements to compare
        Element[] e1_elements = element1.getChildren().toArray(new Element[0]);
        Element[] e2_elements = element2.getChildren().toArray(new Element[0]);
        Arrays.sort(e1_elements, new XmlComparator());
        Arrays.sort(e2_elements, new XmlComparator());

        for(int i = 0; i < e1_elements.length; i++)
            if(!areEqual(e1_elements[i], e2_elements[i]))
                return false;

        return true;
    }

    static class XmlComparator implements Comparator<Element> {

        public int compare(Element e1, Element e2) {
            if(e1.getName().compareTo(e2.getName()) == 0)
                return e1.getText().compareTo(e2.getText());
            else
                return e1.getName().compareTo(e2.getName());
        }
    };

}
