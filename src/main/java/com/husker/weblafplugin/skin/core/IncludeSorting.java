package com.husker.weblafplugin.skin.core;

import java.util.*;

public class IncludeSorting{
    
    public static final ArrayList<String> dict = new ArrayList<>(Arrays.asList(
            "canvas",
            "image",
            "label",
            "styledlabel",
            "tooltip",
            "link",
            "button",
            "splitbutton",
            "togglebutton",
            "checkbox",
            "tristatecheckbox",
            "radiobutton",
            "switcher",
            "separator",
            "menubar",
            "menu",
            "popupmenu",
            "menuitem",
            "checkboxmenuitem",
            "radiobuttonmenuitem",
            "popupmenuseparator",
            "panel",
            "overlay",
            "tabbedpane",
            "splitpanedivider",
            "splitpane",
            "multisplitpanedivider",
            "multisplitpane",
            "popup",
            "collapsiblepane",
            "accordion",
            "rootpane",
            "toolbar",
            "toolbarseparator",
            "statusbar",
            "memorybar",
            "scrollbar",
            "scrollpane",
            "viewport",
            "textfield",
            "passwordfield",
            "formattedtextfield",
            "textarea",
            "editorpane",
            "textpane",
            "tableheader",
            "table",
            "progressbar",
            "slider",
            "spinner",
            "combobox",
            "list",
            "tree",
            "colorchooser",
            "filechooser",
            "desktoppane",
            "desktopicon",
            "internalframe",
            "dockablepane",
            "dockableframe",
            "optionpane",
            "datefield",
            "breadcrumb"
    ));

    private final HashMap<IncludeElement, StyleContainer> containers = new HashMap<>();
    private final HashMap<StyleInfo, IncludeElement> styles_map = new HashMap<>();
    private final ArrayList<StyleInfo> styles = new ArrayList<>();

    public List<IncludeElement> sort(List<IncludeElement> elements) {

        for(IncludeElement element : elements){
            StyleContainer container = new StyleContainer(element);

            String text = element.getFileText();
            String[] styles = text.split("<style");
            for(int i = 1; i < styles.length; i++){
                String style = styles[i];
                String info = style.split(">")[0];

                if(!info.contains("type=\""))
                    continue;

                String  type,
                        id = null,
                        extend = null;


                type = info.split("type=\"")[1].split("\"")[0];
                if(info.contains("id=\""))
                    id = info.split("id=\"")[1].split("\"")[0];
                if(info.contains("extends=\""))
                    extend = info.split("extends=\"")[1].split("\"")[0];

                StyleInfo styleInfo = new StyleInfo(element, type, id, extend);
                this.styles_map.put(styleInfo, element);
                this.styles.add(styleInfo);
                container.styles.add(styleInfo);
            }
            containers.put(element, container);
        }

        // Sorting skins
        styles.sort(new StyleInfoComparator());

        // Applying elements to skins
        ArrayList<String> loaded_types = new ArrayList<>();
        HashMap<String, ArrayList<String>> loaded_ids = new HashMap<>();

        ArrayList<IncludeElement> sorted_elements = new ArrayList<>();
        for(StyleInfo info : styles){

            IncludeElement element = styles_map.get(info);
            if(sorted_elements.contains(element))
                continue;

            HashMap<String, ArrayList<String>> container_ids = containers.get(element).getContainedIDs();
            ArrayList<String> container_types = containers.get(element).getContainedBaseTypes();

            boolean allContains = true;
            for(StyleInfo i : containers.get(element).styles) {
                if(i.type != null && !loaded_types.contains(i.type) && !container_types.contains(i.type)){
                    allContains = false;
                    break;
                }
                if (i.extend != null && !containsInHashMapArray(loaded_ids, i.type, i.extend) && !containsInHashMapArray(container_ids, i.type, i.extend)) {
                    allContains = false;
                    break;
                }
            }
            if(!allContains)
                continue;

            for(StyleInfo i : containers.get(element).styles) {
                if (i.id != null)
                    addToHashMapArray(loaded_ids, i.type, i.id);
                if(i.type != null)
                    loaded_types.add(i.type);
            }

            sorted_elements.add(element);
        }

        // Add unsorted elements
        for(IncludeElement element : elements)
            if(!sorted_elements.contains(element))
                sorted_elements.add(element);

        return sorted_elements;
    }

    public boolean containsInHashMapArray(HashMap<String, ArrayList<String>> map, String key, String value){
        if(map.get(key) == null)
            return false;
        else
            return map.get(key).contains(value);
    }

    public void addToHashMapArray(HashMap<String, ArrayList<String>> map, String key, String value){
        map.computeIfAbsent(key, k -> new ArrayList<>());
        map.get(key).add(value);
    }

    class StyleContainer {

        IncludeElement element;
        ArrayList<StyleInfo> styles = new ArrayList<>();

        public StyleContainer(IncludeElement element){
            this.element = element;
        }

        public HashMap<String, ArrayList<String>> getContainedIDs(){
            HashMap<String, ArrayList<String>> map = new HashMap<>();
            for(StyleInfo info : styles)
                addToHashMapArray(map, info.type, info.id);
            return map;
        }

        public ArrayList<String> getContainedBaseTypes(){
            ArrayList<String> out = new ArrayList<>();
            for(StyleInfo info : styles) {
                if(!out.contains(info.type) && info.id == null && info.extend == null)
                    out.add(info.type);
            }
            return out;
        }
    }

    class StyleInfoComparator implements Comparator<StyleInfo> {

        public int compare(StyleInfo info1, StyleInfo info2) {
            if(info1.type.equals(info2.type)){

                String id1 = info1.id;
                String id2 = info2.id;

                String extends1 = info1.extend;
                String extends2 = info2.extend;

                boolean ID1 = id1 != null;
                boolean ID2 = id2 != null;
                boolean EXT1 = extends1 != null;
                boolean EXT2 = extends2 != null;

                // For debug:
                //System.out.println(ID1 + " " + ID2 + " " + EXT1 + " " + EXT2);

                // Return upper skin!!!

                // (-1) <skin type="same">
                // ( 1) <skin type="same">
                if(!ID1 && !ID2 & !EXT1 && !EXT2){
                    Integer dict1 = dict.indexOf(info1.type);
                    Integer dict2 = dict.indexOf(info2.type);
                    return dict1.compareTo(dict2);
                }
                // (-1) <skin type="same" id="label">
                // ( 1) <skin type="same">
                if(ID1 && !ID2 & !EXT1 && !EXT2)
                    return 1;
                // (-1) <skin type="same">
                // ( 1) <skin type="same" id="label">
                if(!ID1 && ID2 & !EXT1 && !EXT2)
                    return -1;
                // (-1) <skin type="same" id="label>
                // ( 1) <skin type="same" id="label">
                if(ID1 && ID2 & !EXT1 && !EXT2)
                    return id1.compareTo(id2);
                // (-1) <skin type="same" extends="other">
                // ( 1) <skin type="same">
                if(!ID1 && !ID2 & EXT1 && !EXT2)
                    return 1;
                // (-1) <skin type="same" id="label" extends="other">
                // ( 1) <skin type="same">
                if(ID1 && !ID2 & EXT1 && !EXT2)
                    return 1;
                // (-1) <skin type="same" extends="other">
                // ( 1) <skin type="same" id="label">
                if(!ID1 && ID2 & EXT1 && !EXT2)
                    return compareSecondIdAndFirstExtends(id2, extends1);
                // (-1) <skin type="same" id="label" extends="other">
                // ( 1) <skin type="same" id="label">
                if(ID1 && ID2 & EXT1 && !EXT2)
                    return compareSecondIdAndFirstExtends(id2, extends1);
                // (-1) <skin type="same" id="label">
                // ( 1) <skin type="same" extends="other">
                if(ID1 && !ID2 & !EXT1)
                    return compareFirstIdAndSecondExtends(id1, extends2);
                // (-1) <skin type="same">
                // ( 1) <skin type="same" id="label" extends="other">
                if(!ID1 && ID2 & !EXT1)
                    return -1;
                // (-1) <skin type="same" id="label">
                // ( 1) <skin type="same" id="label" extends="other">
                if(ID1 && ID2 & !EXT1)
                    return compareFirstIdAndSecondExtends(id1, extends2);
                // (-1) <skin type="same" extends="other">
                // ( 1) <skin type="same" extends="other">
                if(!ID1 && !ID2 & EXT1)
                    return extends1.compareTo(extends2);
                // (-1) <skin type="same" id="label" extends="other">
                // ( 1) <skin type="same" extends="other">
                if(ID1 && !ID2 & EXT1)
                    return compareFirstIdAndSecondExtends(id1, extends2);
                // (-1) <skin type="same" extends="other">
                // ( 1) <skin type="same" id="label" extends="other">
                if(!ID1 && ID2 & EXT1)
                    return compareSecondIdAndFirstExtends(id2, extends1);
                // (-1) <skin type="same" id="label" extends="other">
                // ( 1) <skin type="same" id="label" extends="other">
                if(ID1 && ID2 & EXT1) {
                    if(id1.equals(extends2))
                        return -1;
                    if(id2.equals(extends1))
                        return 1;
                    return id1.compareTo(id2);
                }
            }else{
                Integer dict1 = dict.indexOf(info1.type);
                Integer dict2 = dict.indexOf(info2.type);
                return dict1.compareTo(dict2);
            }

            return 0;
        }

        int compareFirstIdAndSecondExtends(String id1, String extend2){
            return -1;
            /*
            if(id1.equals(extend2))
                return -1;
            else
                return 1;

             */
        }

        int compareSecondIdAndFirstExtends(String id2, String extend1){
            return 1;
            /*
            if(id2.equals(extend1))
                return 1;
            else
                return -1;

             */
        }
    }

    class StyleInfo{
        public String type;
        public String id;
        public String extend;

        public IncludeElement include;

        public StyleInfo(IncludeElement include, String type, String id, String extend){
            this.include = include;
            this.type = type;
            this.id = id;
            this.extend = extend;
        }

        public String toString() {
            return "<style " +
                    "type=\"" + type + "\"" +
                    (id != null ? " id=\"" + id + "\"" : "") +
                    (extend != null ? " extends=\"" + extend + "\"" : "") +
                    "/>";
        }
    }
}
