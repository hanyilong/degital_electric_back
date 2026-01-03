package com.device.manage.entity;


public class NodeTemplate {
    private Long id;
    private String shape;
    private String label;
    private String svgTemplate;
    private String imageDefault;
    private String imageStart;
    private String imageStop;
    private String svgDefaultStyle;
    private String svgStartStyle;
    private String svgStopStyle;
    private String folderName;
    private String imageIcon;
    private String systemComponent;
    private String systemComponentProps;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSvgTemplate() {
        return svgTemplate;
    }

    public void setSvgTemplate(String svgTemplate) {
        this.svgTemplate = svgTemplate;
    }

    public String getImageDefault() {
        return imageDefault;
    }

    public void setImageDefault(String imageDefault) {
        this.imageDefault = imageDefault;
    }

    public String getImageStart() {
        return imageStart;
    }

    public void setImageStart(String imageStart) {
        this.imageStart = imageStart;
    }

    public String getImageStop() {
        return imageStop;
    }

    public void setImageStop(String imageStop) {
        this.imageStop = imageStop;
    }

    public String getSvgDefaultStyle() {
        return svgDefaultStyle;
    }

    public void setSvgDefaultStyle(String svgDefaultStyle) {
        this.svgDefaultStyle = svgDefaultStyle;
    }

    public String getSvgStartStyle() {
        return svgStartStyle;
    }

    public void setSvgStartStyle(String svgStartStyle) {
        this.svgStartStyle = svgStartStyle;
    }

    public String getSvgStopStyle() {
        return svgStopStyle;
    }

    public void setSvgStopStyle(String svgStopStyle) {
        this.svgStopStyle = svgStopStyle;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(String imageIcon) {
        this.imageIcon = imageIcon;
    }

    public String getSystemComponent() {
        return systemComponent;
    }

    public void setSystemComponent(String systemComponent) {
        this.systemComponent = systemComponent;
    }

    public String getSystemComponentProps() {
        return systemComponentProps;
    }

    public void setSystemComponentProps(String systemComponentProps) {
        this.systemComponentProps = systemComponentProps;
    }
}
